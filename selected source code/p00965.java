import java.io.*;
import java.util.*;


class Main {
    public static void main(String[] args) {
        MyScanner sc = new MyScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int n=sc.nextInt();
        int[]a=new int[n],b=new int[n];
        //policy-2
        int[]p2=new int[2*n];
        for(int i=0;i<n;++i){
            a[i]=sc.nextInt();
            b[i]=sc.nextInt();
            p2[2*i]=2*a[i]+1;
            p2[2*i+1]=2*b[i];
        }
        //policy-2
        Arrays.sort(p2);
        int ans2=0;
        int c=0;
        for(int i=0;i<2*n;++i){
            if(p2[i]%2==0)
                c--;
            else
                c++;
            ans2=Math.max(ans2,c);
        }
        //policy-1
        int ans1=0;
        PlusSegmentTree segL=new PlusSegmentTree();
        PlusSegmentTree segR=new PlusSegmentTree();
        for(int i=0;i<n;++i){
            segL.update(a[i],segL.query(a[i],a[i]+1)+1);
            segR.update(b[i],segR.query(b[i],b[i]+1)+1);
        }
        for(int i=0;i<n;++i){
            int u=(int)segL.query(1,b[i]);
            u-=(int)segR.query(1,a[i]+1);
            ans1=Math.max(ans1,u);
        }
        if(ans1<ans2)throw new Error();
        out.println(ans1+" "+ans2);
        out.close();
    }
    // http://codeforces.com/blog/entry/7018
    //-----------PrintWriter for faster output---------------------------------
    public static PrintWriter out;
    //-----------MyScanner class for faster input----------
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;
        public MyScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        int nextInt() {
            return Integer.parseInt(next());
        }
        long nextLong() {
            return Long.parseLong(next());
        }
        double nextDouble() {
            return Double.parseDouble(next());
        }
        String nextLine(){
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }
}
class PlusSegmentTree {
    static final int SIZE = 1 << 18;
    long[] seg;
    PlusSegmentTree() {
	this.seg = new long[2 * SIZE];
    }
    void update(int x, long value) {
	x += SIZE - 1;
	this.seg[x] = value;
	while (x > 0) {
	    x = (x - 1) / 2;
	    this.seg[x] = this.seg[2 * x + 1] + this.seg[2 * x + 2];
	}
    }
    long query(int l, int r) {
	l += SIZE - 1;
	r += SIZE - 1;
        long y = 0;
	while (l < r) {
	    if ((l & 1) == 0) {
		y = y + this.seg[l];
	    }
	    if ((r & 1) == 0) {
		y = y + this.seg[r - 1];
	    }
	    l /= 2;
	    r = (r - 1) / 2;
	}
	return y;
    }
}
