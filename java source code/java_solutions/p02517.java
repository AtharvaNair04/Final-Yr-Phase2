import java.util.Scanner;
import java.io.*;

class Rmq{
    int segt[] = new int[(1<<21)*2];
    public int inp[] =new int[(1<<21)];
    int MAX_VALUE=2147483647;
    int min(int a,int b){return a<b?a:b;}
    int max(int a,int b){return a>b?a:b;}
    void update(int value,int pos,int n){
	int index=n-1+pos;
	segt[index]=value;//[pos,pos]
	while(index>0){
	    index=(index-1)/2;
	    segt[index]=min(segt[index*2+1],segt[index*2+2]);
	}
    }
    int query(int l,int r,int now,int ql,int qr){
	if (l == ql && r == qr){
	    return segt[now];
	}
	int midr=(l+r)/2,midl=midr+1;
	int ret=MAX_VALUE;
	if (ql <= midr)ret=min(ret,query(l,midr,now*2+1,ql,min(qr,midr)));
	if (midl <= qr)ret=min(ret,query(midl,r,now*2+2,max(midl,ql),qr));
	return ret;
    }

    public void init(int r,int c,int lim){
	boolean isrowmajor=r<=c;
	for(int i=0;i<lim;i++){
	    segt[i]=MAX_VALUE;
	}
	if (isrowmajor){
	    for(int i=0;i<r;i++){
		for(int j=0;j<c;j++){
		    update(inp[i*c+j],i*c+j,lim);
		}
	    }
	}else {
	    for(int i=0;i<r;i++){
		for(int j=0;j<c;j++){
		    update(inp[i+j*r],i+j*r,lim);
		}
	    }
	}
    }
    int searchmin(int r1,int c1,int r2,int c2,int r,int c,int lim){
	boolean isrowmajor = r<=c;  
	int ret=MAX_VALUE;
	lim--;
	if (isrowmajor){
	    for(int i=r1;i<=r2;i++){
		ret=min(ret,query(0,lim,0,i*c+c1,i*c+c2));
	    }
	}else {
	    for(int j=c1;j<=c2;j++){
		ret=min(ret,query(0,lim,0,j*r+r1,j*r+r2));
	    }
	}
	return ret;
    }    

}

class Main{
    void run(){
	Scanner in = new Scanner(System.in);
	//System.setOut(new PrintStream(new BufferedOutputStream(System.out)));
	int r,c,q;
	Rmq rmq=new Rmq();
	while(true){
	    r=in.nextInt();
	    c=in.nextInt();
	    q=in.nextInt();
	    if (r == 0)break;
	    int lim=1;
	    boolean isrowmajor = r<=c;
	    while(true){
		if (lim >= r*c)break;
		lim*=2;
	    }
	    if (isrowmajor){
		for(int i=0;i<r;i++){
		    for(int j=0;j<c;j++){
			rmq.inp[i*c+j]=in.nextInt();
		    }
		}
	    }else {
		for(int i=0;i<r;i++){
		    for(int j=0;j<c;j++){
			rmq.inp[i+j*r]=in.nextInt();
		    }
		}
	    }
	    rmq.init(r,c,lim);
	    for(int i=0;i<q;i++){
		int r1,r2,c1,c2;
		r1=in.nextInt();
		c1=in.nextInt();
		r2=in.nextInt();
		c2=in.nextInt();
		System.out.println(rmq.searchmin(r1,c1,r2,c2,r,c,lim));
	    }
	}
    }
    public static void main(String args[]){
	Main a = new Main();
	a.run();
    }
}
