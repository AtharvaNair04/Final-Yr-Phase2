import java.util.Scanner;
import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.*;
import static java.lang.Math.*;

public class Main implements Runnable{

    static ArrayList <Integer> adj[];
    static int co=0,f=0;

    static void Check2(int n){
        adj=new ArrayList[n+1];
        for(int i=0;i<=n;i++){
            adj[i]=new ArrayList<>();
        }

    }
    static void add(int i,int j){

        adj[i].add(j);
        adj[j].add(i);
    }
    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "Check2", 1<<26).start();// to increse stack size in java
    }
    static long mod=(long)(1e9+7);
    public void run(){
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        //Scanner in=new Scanner(System.in);
        InputReader in=new InputReader(System.in);
        PrintWriter w=new PrintWriter(System.out);
        int n=in.nextInt();
        int a[]=new int[n];
        for(int i=0;i<n;i++){
            a[i]=in.nextInt();
        }
     int b[]=new int[8];

        int oth=0;
        for(int i=0;i<n;i++){


            if(a[i]<400)b[0]++;
            else if(a[i]<800)b[1]++;
            else if(a[i]<1200)b[2]++;
            else if(a[i]<1600)b[3]++;
            else if(a[i]<2000)b[4]++;
            else if(a[i]<2400)b[5]++;
            else if(a[i]<2800)b[6]++;
            else if(a[i]<3200)b[7]++;
            else
                oth++;


        }



        int co=0;
        for(int i=0;i<8;i++)co=co+(b[i]>0?1:0);


        int ce=co+oth;
        ce=max(ce,1);
        co=max(co,1);
        w.println(co+" "+ce);




                    w.close();
    }

    static class pair  {

        int x,y;
        pair(int c,int d){
            x=c;
            y=d;
        }




    }

    static class node{

        int y;
        int val;

        node(int a,int b){

            y=a;
            val=b;

        }


    }
    static  void rec(String s,int a,int b,int n){

        if(b==n){
            System.out.println(s);
            return ;
        }
        String p=s;
        if(a>b){
            s=p+")" ;
            rec(s,a,b+1,n);
        }
        if(a<n){
            s=p+"(";
            rec(s,a+1,b,n);
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
