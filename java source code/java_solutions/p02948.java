import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt();
        int m = sc.nextInt();
        List<List<Integer>> a = new ArrayList<>();
        for(int i=0;i<100000;i++){
            a.add(new ArrayList<Integer>(){});
        }
        for(int i=0;i<n;i++){
            int p = sc.nextInt();
            int q = sc.nextInt();
            a.get(p-1).add(q);
        }
        int sal = 0;
        PriorityQueue<Integer> sub = new PriorityQueue<>(new MyComparator());
        for(int i=0;i<m;i++){
            sub.addAll(a.get(i));
            if(sub.isEmpty())continue;
            sal += sub.poll();
        }
        System.out.println(sal);
        return;
    }

    static class MyComparator implements Comparator<Integer> {
        @Override
        public int compare (Integer arg0, Integer arg1) {
            int x = (int) arg0;
            int y = (int) arg1;
            return y-x;
        }
    }
}
