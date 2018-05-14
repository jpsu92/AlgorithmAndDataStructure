import java.sql.Timestamp;

public class Test {
    public static void main(String[] args){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsStr = "2018-01-01 00:00:00";
        try {
            ts = Timestamp.valueOf(tsStr);
            System.out.println(ts.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
