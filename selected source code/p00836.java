import java.util.*;

public class Main{

    int n;
    int[] p;
    int MAX = 10001;

    void solve(){
        Scanner sc = new Scanner(System.in);

        p = new int[MAX];
        for(int i=2; i<p.length; i++) p[i] = 1;
        for(int i=2; i<Math.sqrt(MAX)+1; i++){
            for(int j=i*2; j<MAX; j+=i){
                p[j] = 0;
            }
        }

        while(true){
            n = sc.nextInt();
            if(n==0) break;

            int cnt = 0;
            for(int i=0; i<=n; i++){
                if(p[i]==0) continue;
                int sum = 0;
                for(int j=i; j<=n; j++){
                    if(p[j]==0) continue;
                    sum += j;
                    if(sum==n){
                        cnt++;
                        break;
                    }
                    if(sum>n) break;
                }
            }

            System.out.println(cnt);
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
