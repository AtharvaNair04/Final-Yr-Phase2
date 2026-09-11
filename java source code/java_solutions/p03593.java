import javax.print.DocFlavor;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;
import static java.lang.Math.*;
public class Main implements Runnable {

    static ArrayList<Integer> adj[];

    static void Check2(int n) {
        adj = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }

    }
    static void add(int i, int j)
    {
        adj[i].add(j);
        adj[j].add(i);
    }
    public static void main(String[] args) {
        new Thread(null, new Main(), "Check2", 1 << 28).start();// to increse stack size in java
    }
    static long mod = (long) (1e9 + 7);
    public void run() {
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);

        int n=in.nextInt();
        int m=in.nextInt();
        char c[][]=new char[n][m];
        int f[]=new int[26];

        for(int i=0;i<n;i++){
            c[i]=in.next().toCharArray();
            for(int j=0;j<m;j++)f[c[i][j]-97]++;
        }
        if(n%2==0&&m%2==0){

            for(int i=0;i<26;i++) {

                if (f[i] % 4 != 0) flag = 1;
            }


        }
        else if(n%2!=0&&m%2==0){

            col(f,n,m);

        }
        else if(m%2!=0&&n%2==0){

            row(f,n,m);

        }
        else{

            int flag4=0;
            int ind=-1;
            for(int i=0;i<26;i++){
                if(f[i]%2!=0){
                    flag4++;
                    ind=i;
                }
            }
            if(flag4==1) {
                f[ind]--;
                rowcol(f, n, m);
            }
            else{
                flag=1;
            }

        }
        if(flag==0)w.println("Yes");
        else
            w.println("No");





        w.close();

    }
    void rowcol(int f[],int n,int m){

        int ans=0;
        for(int i=0;i<26;i++){
            if(f[i]%4==2){
                ans++;
                f[i]-=2;
            }
        }

        ans=ans*2;
        if(ans>n+m-2)flag=1;
        if((n+m-ans-2)%4!=0)flag=1;
        for(int i=0;i<26;i++){
            if(f[i]%4!=0)flag=1;
        }


    }
    int flag=0;
    void row(int f[],int n,int m){
        int ans=0;
        for(int i=0;i<26;i++){
            if(f[i]%4==2){
                ans++;
                f[i]-=2;
            }
        }

        ans=ans*2;
        if(ans>n)flag=1;
        if((n-ans)%4!=0)flag=1;
        for(int i=0;i<26;i++){
            if(f[i]%4!=0)flag=1;
        }
    }
    void col(int f[],int n,int m){

        int ans=0;
        for(int i=0;i<26;i++){
            if(f[i]%4==2){
                ans++;
                f[i]-=2;
            }
        }

        ans=ans*2;
        if(ans>m)flag=1;
        if((m-ans)%4!=0)flag=1;
        for(int i=0;i<26;i++){
            if(f[i]%4!=0)flag=1;
        }
    }
    static class pair2 implements  Comparable<pair2> {

        int a;
        int b;
        pair2(int a,int b){
            this.a=a;
            this.b=b;

        }
        public int compareTo(pair2 o){

            if(o.a!=this.a)return this.a-o.a;
            else
                return this.b-o.b;



        }

    }
    static void diji(int s,long dist[],ArrayList <pair> adj[]){
        dist[s]=0;
        PriorityQueue <pair> q=new PriorityQueue<>(new cmp());
        q.add(new pair(s,0));
        while (!q.isEmpty())
        {
            pair p1=q.poll();
            for(pair ne:adj[p1.a]){

                if((long)p1.b+(long)ne.b<dist[ne.a]){
                    dist[ne.a]=(long)p1.b+(long)ne.b;
                    pair p2=new pair(ne.a,dist[ne.a]);
                    q.add(p2);

                }

            }
        }

    }
    static class cmp implements Comparator<pair>{

        public  int compare(pair o1,pair o2)
        {
            return o1.b<o2.b?-1:o1.b>o2.b?1:0;
        }

    }

    static class pair {
        int a;
        long b;
        pair(int a,long b){
            this.a=a;
            this.b=b;
        }
        public boolean equals(Object obj) {      //  override equals method for object to remove tem from arraylist and sets etc.......
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            pair other = (pair) obj;
            if (b!= other.b||a!=other.a)
                return false;
            return true;
        }
    }

    static long modinv(long a,long b)
    {
        long p=power(b,mod-2);

        p=a%mod*p%mod;
        p%=mod;

        return p;

    }

    static long power(long x,long y){
        if(y==0)return 1%mod;
        if(y==1)return x%mod;


        long res=1;
        x=x%mod;
        while(y>0){


            if((y%2)!=0){
                res=(res*x)%mod;
            }


            y=y/2;
            x=(x*x)%mod;
        }


        return res;

    }
    static  long gcd(long a,long b){

        if(b==0)return a;
        return gcd(b,a%b);
    }

    static  void sev(int a[],int n){

        for(int i=2;i<=n;i++)a[i]=i;
        for(int i=2;i<=n;i++){

            if(a[i]!=0){
                for(int j=2*i;j<=n;){

                    a[j]=0;
                    j=j+i;
                }
            }

        }

    }



    static class InputReader
    {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private SpaceCharFilter filter;

        public InputReader(InputStream stream)
        {
            this.stream = stream;
        }

        public int read()
        {
            if (numChars==-1)
                throw new InputMismatchException();

            if (curChar >= numChars)
            {
                curChar = 0;
                try
                {
                    numChars = stream.read(buf);
                }
                catch (IOException e)
                {
                    throw new InputMismatchException();
                }

                if(numChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public String nextLine()
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            try
            {
                str = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return str;
        }
        public int nextInt()
        {
            int c = read();

            while(isSpaceChar(c))
                c = read();

            int sgn = 1;

            if (c == '-')
            {
                sgn = -1;
                c = read();
            }

            int res = 0;
            do
            {
                if(c<'0'||c>'9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));

            return res * sgn;
        }

        public long nextLong()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-')
            {
                sgn = -1;
                c = read();
            }
            long res = 0;

            do
            {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            while (!isSpaceChar(c));
            return res * sgn;
        }

        public double nextDouble()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            int sgn = 1;
            if (c == '-')
            {
                sgn = -1;
                c = read();
            }
            double res = 0;
            while (!isSpaceChar(c) && c != '.')
            {
                if (c == 'e' || c == 'E')
                    return res * Math.pow(10, nextInt());
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = read();
            }
            if (c == '.')
            {
                c = read();
                double m = 1;
                while (!isSpaceChar(c))
                {
                    if (c == 'e' || c == 'E')
                        return res * Math.pow(10, nextInt());
                    if (c < '0' || c > '9')
                        throw new InputMismatchException();
                    m /= 10;
                    res += (c - '0') * m;
                    c = read();
                }
            }
            return res * sgn;
        }

        public String readString()
        {
            int c = read();
            while (isSpaceChar(c))
                c = read();
            StringBuilder res = new StringBuilder();
            do
            {
                res.appendCodePoint(c);
                c = read();
            }
            while (!isSpaceChar(c));

            return res.toString();
        }

        public boolean isSpaceChar(int c)
        {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next()
        {
            return readString();
        }

        public interface SpaceCharFilter
        {
            public boolean isSpaceChar(int ch);
        }
    }







}
