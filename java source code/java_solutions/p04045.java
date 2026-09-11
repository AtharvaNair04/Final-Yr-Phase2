import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < k; i++){
            int num = scanner.nextInt();
            set.add(num);
        }
        for(int i = n; i < 100000; i++){
            String t = String.valueOf(i);
            boolean flag = true;
            for(int j = 0; j < t.length(); j++){
                if(set.contains(t.charAt(j) - '0')){
                    flag = false;
                    break;
                }
            }
            if(flag){System.out.println(i);return;}
        }
    }
}
