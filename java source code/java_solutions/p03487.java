import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < n; i++){
            int tmp = scanner.nextInt();
            map.put(tmp,map.getOrDefault(tmp,0)+1);
        }
        int ans = 0;
        for(Integer key: map.keySet()){
            int val = map.get(key) - key;
            if(val < 0){
                ans += map.get(key);
            }else if(val > 0){
                ans += Math.min(map.get(key) - key, map.get(key));
            }
        }
        System.out.println(ans);
    }
}
