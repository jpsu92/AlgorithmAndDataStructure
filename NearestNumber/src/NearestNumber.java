import java.util.Arrays;

/**
 * 获得最近换位数
 * 1.从后向前查看逆序区域，找到逆序区域的前一位，也就是数字置换的边界
 * 2.把逆序区域的前一位和逆序区域中刚刚大于它的数字交换位置
 * 3.把原来的逆序区域转为顺序
 */
public class NearestNumber {
    public static int[] findNearestNumber(int[] numbers){
        int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);
        int reverseOrderPointer = numbers.length - 1;
        //从后向前查看逆序区域，找到逆序区域的前一位，也就是数字置换的边界
        while(reverseOrderPointer >0 && numbersCopy[reverseOrderPointer-1] > numbersCopy[reverseOrderPointer]){
            reverseOrderPointer--;
        }
        //如果数字置换边界是0，说明整个数组已经逆序，无法得到更大的相同数字组成的整数，返回自身
        if (reverseOrderPointer == 0)
            return numbersCopy;

        //把逆序区域的前一位和逆序区域中刚刚大于它的数字交换位置
        exchange(numbersCopy, reverseOrderPointer);

        //把原来的逆序区域转为顺序
        reverseArray(numbersCopy, reverseOrderPointer, numbersCopy.length-1);

        return numbersCopy;
    }

    public static void exchange(int[] array, int reverseOrderPointer){
        int p = array.length - 1;
        while (p >= 0 && array[p] < array[reverseOrderPointer-1]){
            p--;
        }
        int tmp = array[reverseOrderPointer-1];
        array[reverseOrderPointer-1] = array[p];
        array[p] = tmp;
    }

    public static void reverseArray(int[] array, int start, int end){
        while(start <= end){
            int tmp = array[start];
            array[start] = array[end];
            array[end] = tmp;
            start++;
            end--;
        }
    }

    public static void outputArray(int[] array){
        for (int i : array)
            System.out.print(i);
        System.out.println();
    }


    public static void main(String[] args) {
        int[] numbers = {1,2,3,4,5};
        for(int i = 0; i < 10; i++){
            numbers = findNearestNumber(numbers);
            outputArray(numbers);
        }
    }
}
