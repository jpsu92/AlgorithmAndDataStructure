import java.util.ArrayList;
import java.util.List;

public class Test {
    public static boolean insert(int[] list, int t, int pos){

        int i = pos - 1;
        for (; i >= 0; i--){
            if(list[i] == t)
                return false;
            if(list[i] > t)
                list[i+1] = list[i];
            else
                break;
        }
        list[i+1] = t;
        return true;
    }

    public static void insert(List<Integer> list, int t){
        int i = list.size() - 1;
        for (; i >= 0 && list.get(i) > t; i--){
            list.set(i+1, list.get(i));
        }
        list.set(i+1, t);
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(3);
        list.add(5);
        insert(list, 4);
        for (int i = 0; i < list.size(); i++)
            System.out.println(list.get(i));
    }






}
