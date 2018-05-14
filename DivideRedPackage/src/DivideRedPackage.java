import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DivideRedPackage {


    public static List<Integer> algorithm1(int money, int people){
        List<Integer> result = new ArrayList<Integer>();
        int resultMoney = money;
        int resultPeople = people;
        Random rand = new Random();
         for(int i = 0; i < people - 1; i++){
             int tmp = rand.nextInt(resultMoney / resultPeople * 2 - 1) + 1;
             result.add(tmp);
             resultMoney -= tmp;
             resultPeople--;
         }
         result.add(resultMoney);
        return result;

    }


    public static int[] algorithm2(int money, int people){
        int[] result = new int[people-1];
        Random random = new Random();
        int dividePoint = 0;
        while (dividePoint < people-1){
            int tmp = random.nextInt(money - 1) + 1;
            if (!isContain(result, tmp, dividePoint)){
                insert(result, tmp, dividePoint);
                dividePoint++;
            }
        }
        return result;
    }

    public static boolean isContain(int[] array, int value, int pos){
        int low = 0;
        int high = pos - 1;
        int mid = (low + high) / 2;
        while(low <= high){
            if (value > array[mid])
                low = mid + 1;
            else if(value < array[mid])
                high = mid - 1;
            else
                return true;
            mid = (low + high) / 2;
        }
        return false;
    }

    public static void insert(int[] array, int t, int pos){
        int i = pos - 1;
        for (; i >= 0 && array[i] > t; i--){
            array[i+1] = array[i];
        }
        array[i+1] = t;
    }


    public static void main(String[] args) {
        int money = 10000;
        int people = 30;
//        List<Integer> moneyList = algorithm1(10, 5);
//        for (int i : moneyList){
//            System.out.println(i);
//        }
        int[] divideList = algorithm2(money, people);
        int lastValue = 0;
        int sum = 0;
        for (int i = 0; i < divideList.length; i++){
            System.out.println(divideList[i] - lastValue);
            sum += divideList[i] - lastValue;
            lastValue = divideList[i];
        }
        System.out.println(money - lastValue);
        sum += money - lastValue;
        System.out.println("sum=" + sum);
    }
}
