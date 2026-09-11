import java.util.*;

class Main {
    static final long I=1L<<40;
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n=Integer.parseInt(scan.next());
        int q=Integer.parseInt(scan.next());
        long[]a=new long[2*n],b=new long[2*n];
        for(int i=0;i<2*n;++i)a[i]=Integer.parseInt(scan.next());
        for(int i=0;i<2*n;++i)b[i]=Integer.parseInt(scan.next());
        long t=0;
        MaxSegmentTree sd=new MaxSegmentTree();
        MaxSegmentTree sdp=new MaxSegmentTree();
        long tpos=0;
        int cpos=0;
        for(int i=0;i<2*n;++i)t+=a[i];
        for(int i=1;i<2*n-1;++i){
            long c=b[i]-a[i];
            tpos+=Math.max(c,0);
            if(c>=0)cpos++;
            sd.update(i,c>=0?-I:c);
            sdp.update(i,c<0?-I:-c);
        }
        for(int i=0;i<q;++i){
            int p=Integer.parseInt(scan.next())-1;
            long x=Integer.parseInt(scan.next());
            long y=Integer.parseInt(scan.next());
            long c=b[p]-a[p];
            t-=a[p];
            if(1<=p&&p<=2*n-2&&c>=0){
                tpos-=c;
                cpos--;
            }
            a[p]=x;
            b[p]=y;
            c=b[p]-a[p];
            t+=a[p];
            if(1<=p&&p<=2*n-2&&c>=0){
                tpos+=c;
                cpos++;
            }
            if(1<=p&&p<=2*n-2) {
                sd.update(p,c>=0?-I:c);
                sdp.update(p,c<0?-I:-c);
            }
            long ans;
            if(cpos%2==0)
                ans=tpos;
            else{
                ans=Math.max(tpos+sd.query(1,2*n-1),tpos+sdp.query(1,2*n-1));
            }
            System.out.println(ans+t);
        }
    }
}
class MaxSegmentTree {
    static final int SIZE = 1 << 18;
    static final long INFINITY = (1L << 62) - 1;
    long[] seg;
    MaxSegmentTree() {
	this.seg = new long[2 * SIZE];
    }
    void update(int x, long value) {
	x += SIZE - 1;
	this.seg[x] = value;
	while (x > 0) {
	    x = (x - 1) / 2;
	    this.seg[x] = Math.max(this.seg[2 * x + 1], this.seg[2 * x + 2]);
	}
    }
    long query(int l, int r) {
	l += SIZE - 1;
	r += SIZE - 1;
        long y = -INFINITY;
	while (l < r) {
	    if ((l & 1) == 0) {
		y = Math.max(y, this.seg[l]);
	    }
	    if ((r & 1) == 0) {
		y = Math.max(y, this.seg[r - 1]);
	    }
	    l /= 2;
	    r = (r - 1) / 2;
	}
	return y;
    }
}
