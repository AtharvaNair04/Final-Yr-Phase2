import java.util.*;

public class Main{

    boolean debug = false;

    char[][] f;
    int[] dx = {0,0,1,-1};
    int[] dy = {1,-1,0,0};
    int d;
    int rensa;

    void solve(){
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        while(n-->0){
            f = new char[12][6];
            for(int i=0; i<12; i++){
                f[i] = sc.next().toCharArray();
            }

            rensa = 0;
            delete();
            calc();
            System.out.println(rensa);
        }
    }

    void calc(){
        d = 1;
        while(d!=0){
            d = 0;
            for(int i=0; i<12; i++){
                for(int j=0; j<6; j++){
                    if(f[i][j]!='.' && f[i][j]!='O' && f[i][j]!='x'){
                        bfs(f[i][j],j,i);
                    }
                }
            }
            
            if(d!=0){
                delete();
                rensa++;
                if(debug){
                    for(int i=0; i<12; i++){
                        System.out.println(Arrays.toString(f[i]));
                    }
                }
            }else break;
        }
    }

    void bfs(char c, int sx, int sy){
        Queue<int[]> q = new LinkedList<int[]>();
        Queue<int[]> qq = new LinkedList<int[]>();
        q.add(new int[]{sx,sy});
        qq.add(new int[]{sx,sy});
        int cnt = 0;
        boolean[][] v = new boolean[12][6];
        v[sy][sx] = true;

        while(q.size()>0){
            int[] xy = q.poll();
            int x = xy[0], y = xy[1];

            for(int i=0; i<4; i++){
                int nx = x+dx[i], ny = y+dy[i];
                if(nx>=0 && nx<6 && ny>=0 && ny<12 
                   && f[ny][nx]==c && !v[ny][nx]){
                    q.add(new int[]{nx,ny});
                    qq.add(new int[]{nx,ny});
                    v[ny][nx] = true;
                }
            }
        }

        if(debug) System.out.println(c+" "+qq.size());

        if(qq.size()>=4){
            d++;
            while(qq.size()>0){
                int[] xy = qq.poll();
                f[xy[1]][xy[0]] = 'x';
                for(int i=0; i<4; i++){
                    int nx = xy[0]+dx[i], ny = xy[1]+dy[i];
                    if(nx>=0 && nx<6 && ny>=0 && ny<12 
                       && f[ny][nx]=='O'){
                        f[ny][nx] = 'x'; 
                    }
                }
            }
        }
    }

    void delete(){
        for(int i=0; i<6; i++){
            Queue<Character> q = new LinkedList<Character>();
            for(int j=11; j>=0; j--){
                if(f[j][i]!='x' && f[j][i]!='.'){
                    q.add(f[j][i]);
                }
            }
            for(int j=11; j>=0; j--){
                if(q.size()>0) f[j][i] = q.poll();
                else f[j][i] = '.';
            }
        }
    }
    public static void main(String[] args){
        new Main().solve();
    }
}
