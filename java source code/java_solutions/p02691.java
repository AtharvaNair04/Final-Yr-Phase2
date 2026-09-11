import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long a[] = new long[n];
        HashMap<Long, Integer> map = new HashMap<>();
        for(int i = 0; i < n; i++){
            a[i] = scanner.nextInt();
        }
        long ans = 0;
        for(int i = 0; i < n; i++){
            long req = i-a[i];
            ans += map.getOrDefault(req,0);
            map.put(i+a[i], map.getOrDefault(i+a[i],0)+1);
        }
        System.out.println(ans);
    }
}
