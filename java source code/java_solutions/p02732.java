import java.util.*;

class Main{
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        Map<Integer, Long> maps = new HashMap<>();
        for(int i=0;i<n;i++){
            a[i] = sc.nextInt();
            if(maps.containsKey(a[i])){
                maps.put(a[i], maps.get(a[i])+1L);
            }
            else{
                maps.put(a[i], 1L);
            }
        }

        long total = 0;
        for(Integer key : maps.keySet()){
            Long count = maps.get(key);
            // System.out.print(key);
            // System.out.println(" " + count);
            total += (count*(count-1)/2);
        }

        for(int i=0;i<n;i++){
            System.out.println(total-(maps.get(a[i])-1));
        }

    }

}
