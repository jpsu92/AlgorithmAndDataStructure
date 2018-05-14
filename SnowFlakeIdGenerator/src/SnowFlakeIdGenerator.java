import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnowFlakeIdGenerator {
    //初试时间戳
    private static final long INITIAL_TIME_STAMP = 1514736000000L;

    //机器id所占的位数
    private static final long WORKER_ID_BTIS = 5L;

    //数据标识id所占的位数
    private static final long DATACENTER_ID_BITS = 5L;

    //支持的最大数据标识id，结果是31（这个移位算法可以很快计算出几位二进制数所能表示的最大十进制数）
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BTIS);

    //支持的最大数据标识id，结果为31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    //序列在id中所占的位数
    private static final long SEQUENCE_BITS = 12L;

    //机器id的偏移量（12）
    private static final long WORKERID_OFFSET = SEQUENCE_BITS;

    //数据中心偏移量（12+5）
    private static final long DATATCERTERID_OFFSET = SEQUENCE_BITS + WORKER_ID_BTIS;

    //时间戳偏移量（12+5+5）
    private static final long TIMESTAMP_OFFSET = SEQUENCE_BITS + WORKER_ID_BTIS + DATACENTER_ID_BITS;

    //生成序列的掩码，这里是4095
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    //工作节点id
    private long workerId;

    //数据中心id
    private long datacenterId;

    //毫秒内序列（0~4095）
    private long sequence = 0L;

    //上次生成ID的时间戳
    private long lastTimestamp = -1L;

    public SnowFlakeIdGenerator(long workerId, long datacenterId) {
        if(workerId > MAX_WORKER_ID || workerId < 0){
            throw new IllegalArgumentException(String.format("WorkerID 不能大于 %d 或小于 0", MAX_WORKER_ID));
        }
        if(datacenterId > MAX_DATACENTER_ID || datacenterId < 0){
            throw new IllegalArgumentException(String.format("DatacenterID 不能大于 %d 或小于 0", MAX_DATACENTER_ID));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个ID（用同步所保证线程安全）
     * @return SnowflakeId
     */
    public synchronized long nextId(){
        long timestamp = System.currentTimeMillis();

        //如果当前时间小于上一次生成的时间戳，说明系统时钟回退到这个时候应该抛出异常
        if(timestamp < lastTimestamp){
            throw new RuntimeException("当前时间小于上一次记录的时间戳");
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if(timestamp == lastTimestamp){
            sequence = (sequence + 1) & SEQUENCE_MASK;
            //如果sequence等于0， 说明毫秒内序列已经增长到最大值
            if(sequence == 0){
                //阻塞到下一秒，或得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else{
            sequence = 0L;
        }
        //上次生成ID的时间戳
        lastTimestamp = timestamp;

        //移位通过或运算拼到一起组成的64位的ID
        return ((timestamp-INITIAL_TIME_STAMP) << TIMESTAMP_OFFSET)
                | (datacenterId << DATATCERTERID_OFFSET)
                | (workerId << WORKERID_OFFSET)
                | sequence;
    }

    /**
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp){
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }


    public static void main(String[] args){
        final SnowFlakeIdGenerator idGenerator  = new SnowFlakeIdGenerator(1l, 1l);
        //线程池并行10000次ID生成
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10000; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    long id = idGenerator.nextId();
                    System.out.println(id);
                }
            });
        }
        executorService.shutdown();
    }
}
