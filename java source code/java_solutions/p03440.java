import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int n;
    static int[] val, par, rank, min, num;
    static boolean[] use;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int m = sc.nextInt();
        val = new int[n];
        par = new int[n];
        rank = new int[n];
        min = new int[n];
        use = new boolean[n];
        num = new int[n];
        long ans = 0;
        int ntree = 0;
        int none = 0;
        int neda = 0;

        for(int i=0; i<n; i++){
            val[i] = sc.nextInt();
            par[i] = i;
            rank[i] = 0;
            min[i] = i;
            use[i] = false;
            num[i] = 1;
        }

        for(int i=0; i<m; i++){
            int mx = sc.nextInt();
            int my = sc.nextInt();
            unite(mx, my);
        }

        for(int i=0; i<n; i++){
            find(i);
        }

        for(int i=0; i<n; i++){
            if(val[i] < val[min[find(i)]]) min[find(i)] = i;
        }

        for(int i=0; i<n; i++){
            if(find(i) == i){
                ans += val[min[i]];
                use[min[i]] = true;
                ntree++;
                if(num[i] == 1){
                    none++;
                }else if(num[i] > 2){
                    neda += num[i] - 2;
                }
            }
        }

        if(none - 2 > neda){
            System.out.println("Impossible");
            return;
        }

        if(ntree == 1){
            System.out.println(0);
            return;
        }

        int[] rem = new int[n - ntree];
        int c = 0;
        for(int i=0; i<n; i++){
            if(!use[i]){
                rem[c] = val[i];
                c++;
            }
        }

        Arrays.sort(rem);

        for(int i=0; i<ntree-2; i++){
            ans += rem[i];
        }

        System.out.println(ans);
    }

    public static int find(int x){
        if(par[x] == x){
            return x;
        }else{
            return par[x] = find(par[x]);
        }
    }

    public static void unite(int x, int y){
        x = find(x);
        y = find(y);
        if(x == y) return;

        if(rank[x] < rank[y]){
            par[x] = y;
            num[find(y)] += num[x];
        }else{
            par[y] = x;
            if(rank[x] == rank[y]) rank[x]++;
            num[find(x)] += num[y];        
        }        
    }

    public static boolean same(int x, int y){
        return find(x) == find(y);
    }
}
