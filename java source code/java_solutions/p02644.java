import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(System.out);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int k = sc.nextInt();
        TreeSet<Integer>[]rows= new TreeSet[n],cols= new TreeSet[m];
        for (int i =0;i<n;i++){
            rows[i]=new TreeSet<>();
            for (int j=0;j<m;j++){
                if (cols[j]==null)cols[j]=new TreeSet<>();
                rows[i].add(j);
                cols[j].add(i);
            }
        }
        char[][]a= new char[n][m];
        int []s= sc.nextIntArr(2);
        int []e= sc.nextIntArr(2);
        s[0]--;s[1]--;e[0]--;e[1]--;
        for (int i =0;i<n;i++)
            a[i]=sc.nextLine().toCharArray();
        rows[s[0]].remove(s[1]);
        cols[s[1]].remove(s[0]);
        int[]q= new int[2*n*m+10];
        int p=0,l=0;
        q[l++]=s[0];
        q[l++]=s[1];
        int[][]dis= new int[n][m];
        for (int[]x:dis)Arrays.fill(x,-1);
        dis[s[0]][s[1]]=0;
        while (p<l){
            int x=q[p++];
            int y=q[p++];
            while (true){
                Integer nxt = rows[x].ceiling(y);
                if (nxt==null||Math.abs(y-nxt)>k||a[x][nxt]=='@')
                    break;
                rows[x].remove(nxt);
                cols[nxt].remove(x);
                q[l++]=x;
                q[l++]=nxt;
                dis[x][nxt]=1+dis[x][y];
            }
            while (true){
                Integer nxt = rows[x].floor(y);
                if (nxt==null||Math.abs(y-nxt)>k||a[x][nxt]=='@')
                    break;
                rows[x].remove(nxt);
                cols[nxt].remove(x);
                q[l++]=x;
                q[l++]=nxt;
                dis[x][nxt]=1+dis[x][y];
            }
            while (true){
                Integer nxt = cols[y].floor(x);
                if (nxt==null||Math.abs(x-nxt)>k||a[nxt][y]=='@')
                    break;
                cols[y].remove(nxt);
                rows[nxt].remove(y);
                q[l++]=nxt;
                q[l++]=y;
                dis[nxt][y]=1+dis[x][y];
            }
            while (true){
                Integer nxt = cols[y].ceiling(x);
                if (nxt==null||Math.abs(x-nxt)>k||a[nxt][y]=='@')
                    break;
                cols[y].remove(nxt);
                rows[nxt].remove(y);
                q[l++]=nxt;
                q[l++]=y;
                dis[nxt][y]=1+dis[x][y];
            }
        }
        pw.println(dis[e[0]][e[1]]);
        pw.flush();
    }

    static class Scanner {
        StringTokenizer st;
        BufferedReader br;

        public Scanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }

        public Scanner(FileReader r) {
            br = new BufferedReader(r);
        }

        public String next() throws IOException {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        public long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        public String nextLine() throws IOException {
            return br.readLine();
        }

        public double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        public int[] nextIntArr(int n) throws IOException {
            int[] arr = new int[n];
            for (int i = 0; i < arr.length; i++)
                arr[i] = nextInt();
            return arr;
        }

        public boolean ready() throws IOException {
            return br.ready();
        }

    }
}
