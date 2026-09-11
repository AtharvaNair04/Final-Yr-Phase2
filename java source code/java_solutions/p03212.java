import java.util.*;
class Main{
    static long n;
    static List<Long> list;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        list = new ArrayList<>();
        n = scanner.nextLong();
        dfs("3");
        dfs("5");
        dfs("7");
        int count = 0;
        for(int i = 0; i < list.size(); i++){
            String cur = String.valueOf(list.get(i));
            if(cur.contains("3") && cur.contains("5") && cur.contains("7")){
                count++;
            }
        }
        System.out.println(count);
    }
    public static void dfs(String s){
        if(Long.parseLong(s) <= n){
            list.add(Long.parseLong(s));
            String[] target = {"3", "5", "7"};
            for(int i = 0; i < target.length; i++){
                String newS = s + target[i];
                dfs(newS);
            }
        }
    }
}
