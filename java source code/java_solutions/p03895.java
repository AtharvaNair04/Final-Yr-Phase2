import java.util.*;
import java.io.*;

import static java.lang.System.in;

public class Main {
    public static void main(String[] args)throws IOException{
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int mod = 86400;
        int[] rec = new int[mod+1];
        int cur = 0;
        for(int i=0;i<n;i++){
            int a = sc.nextInt(), b = sc.nextInt();
            cur = (cur+a)%mod;
            rec[cur%mod+1]++;
            cur = (cur+b)%mod;
        }
        int[] sum = new int[mod+1];
        for(int i=1;i<=mod;i++){
            sum[i] = sum[i-1]+rec[i];
        }
        int len = 10800+1;
        int ans = 0;
        for(int start=1;start+len-1<=mod;start++){
            int now = sum[start+len-1]-sum[start-1];
            ans = Math.max(ans,now);
        }
        for(int start = mod+2-len;start<=mod;start++){
            int tail = mod-start+1;
            int now = sum[mod]-sum[start-1]+sum[len-tail];
            ans =Math.max(ans,now);
        }
        System.out.println(ans);
    }
}
