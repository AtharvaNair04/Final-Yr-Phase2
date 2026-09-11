import java.util.*;
class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        int[] a = new int[2019];
        int n = s.length();
        int ten = 1;
        int total = 0;
        a[0]++;
        for(int i = 1; i <= n; i++){
            total += (s.charAt(n-i) - '0') * ten;
            ten = ten * 10 % 2019;
            total %= 2019;
            a[total]++;
        }
        int ans = 0;
        for(int i = 0; i < 2019; i++){
            ans += a[i] * (a[i] - 1) / 2;
        }
        System.out.println(ans);
    }
}
