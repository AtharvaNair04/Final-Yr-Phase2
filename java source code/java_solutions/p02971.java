import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < n; i++) list.add(sc.nextInt());
        ArrayList<Integer> list1 = (ArrayList<Integer>)list.clone();
        list1.stream().distinct();
        Collections.sort(list1);
        Integer a = list1.get(list1.size() - 1);
        Integer b = list1.get(list1.size() - 2);
        for(int i = 0; i < n; i++){
            if(list.get(i) == a)    System.out.println(b);
            else if(list.get(i) == b)    System.out.println(a);
            else System.out.println(a);
        }
    }
}
