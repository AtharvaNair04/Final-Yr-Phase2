import java.util.*;

public class Main{

    int n,m;
    char[] c;
    int[][] dp;

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            n = sc.nextInt();
            if(n==0) break;
            m = sc.nextInt();
            c = sc.next().toCharArray();

            int idx = 0;
            while(c[idx]!='I') idx++;

            int ans = 0;
            int num = n*2+1;
            for(int i=idx; i<c.length; i++){
                if(i==c.length-1){
                    if(c[i]=='O'){
                        if(i-idx+1>=num) ans += (i-idx+1-num)/2+1;
                    }else{
                        if(i-idx+1>=num) ans += (i-idx+1-num)/2+1;
                    }
                }else if(c[i]=='O' && c[i+1]=='O'){
                    if(i-idx+1>=num) ans += (i-idx+1-num)/2+1;
                    idx = i;
                    while(idx<c.length && c[idx]!='I') idx++;
                    i = idx-1;
                }else if(c[i]=='I' && c[i+1]=='I'){
                    if(i-idx+1>=num) ans += (i-idx+1-num)/2+1;
                    idx = i+1;
                    i = idx-1;
                }
            }

            System.out.println(ans);
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
