import java.util.*;

public class Main{

    int n,m;
    int[][] c;

    void solve(){
        Scanner sc = new Scanner(System.in);

        while(true){
            n = sc.nextInt();
            m = sc.nextInt();
            if(n==0 && m==0) break;

            c = new int[n][n];
            for(int i=0; i<m; i++){
                int a = sc.nextInt()-1, b = sc.nextInt()-1;
                int cc = sc.nextInt();
                c[a][b] = cc;
                c[b][a] = cc;
            }

            System.out.println(getMin());
        }
    }

    int getMin(){
        //pos,cost,ticket
        PriorityQueue<int[]> q = 
            new PriorityQueue<int[]>(n,new Comparator<int[]>(){
                    public int compare(int[] a, int[] b){
                        return a[1] - b[1];
                    }
                });
        q.add(new int[]{0,0,0});
        int[][] v = new int[101][3];
        for(int i=0; i<101; i++) Arrays.fill(v[i],Integer.MAX_VALUE);

        while(q.size()>0){
            int[] qq = q.poll();
            int pos = qq[0], cost = qq[1], ticket = qq[2];

            //System.out.println(Arrays.toString(qq));

            if(pos==n-1 && ticket!=1) return cost;
            if(v[pos][ticket]<=cost) continue;
            v[pos][ticket] = cost;

            for(int i=0; i<n; i++){
                if(c[pos][i]==0) continue;
                if(ticket==0){
                    q.add(new int[]{i,cost,1});
                    q.add(new int[]{i,cost+c[pos][i],0});
                }else if(ticket==1){
                    q.add(new int[]{i,cost,2});
                }else if(ticket==2){
                    q.add(new int[]{i,cost+c[pos][i],2});
                }
            }
        }

        return -1;
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
