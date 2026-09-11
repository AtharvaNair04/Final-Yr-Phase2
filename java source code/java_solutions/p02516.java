//6.12
import java.util.Scanner;
import java.io.*;

class Main{
    int num[]=new int[30000];
    int l[]=new int[30000],r[] =new int[30000];
    int max(int a,int b){return a>b?a:b;}
    boolean islower_line(double x,double y,double a,double b){
	return y < a*x+b;
    } 
    void put_xy(int m,int w,double x,double y){
	int tl=0,tr=m;
	int mid=0,ans=-1;
	while(tl <= tr){
	    mid =(tl+tr)/2;
	    if (islower_line(x,y,(r[mid]-l[mid])/(double)w,l[mid])){
		ans=mid;
		tr=mid-1;
	    }else tl=mid+1;
	}
	//System.out.println(islower_line(x,y,(r[ans]-l[ans])/(double)w,
	//l[ans]));
	num[ans]++;
	return;
    }
    int solve(int m,int s,int w){
	int ret=0,now=0;
	int head=0,tail=0;
	int lbase=0,rbase=0;
	while(true){
	    //back++
	    while(tail < m){
		if ((long)w*(long)(l[tail]-lbase+r[tail]-rbase) > 2*s)break;
		now+=num[tail];
		tail++;
	    }
	    ret=max(ret,now);
	    if (tail == m)break;
	    //head++;
	    head++;
	    lbase=l[head-1];
	    rbase=r[head-1];
	    now-=num[head-1];
	}
	return ret;
    }

    /*
    void run(){
	Scanner in = new Scanner(System.in);
	//System.setOut(new PrintStream(new BufferedOutputStream(System.out)));
	int n,m,w,h;
	int s;
	while(true){
	    n=in.nextInt();
	    m=in.nextInt();
	    w=in.nextInt();
	    h=in.nextInt();
	    s=in.nextInt();
	    if (n == 0)break;
	    for(int i=0;i<m;i++)num[i]=0;
	    for(int i=0;i<m;i++){
		int tmp;
		l[i]=in.nextInt();
		r[i]=in.nextInt();
	    }
	    for(int i=0;i<n;i++){
		double x,y;
		x=in.nextDouble();
		y=in.nextDouble();
		put_xy(m,w,x,y);
	    }
	    s=w*h-s;
	    System.out.println(n-solve(m,s,w));
	}
    }
    */

    void run(){
	Scanner in = new Scanner(System.in);
	//System.setOut(new PrintStream(new BufferedOutputStream(System.out)));
	int n,m,w,h;
	int s;
	while(true){
	    n=Integer.parseInt(in.next());
	    m=Integer.parseInt(in.next());
	    w=Integer.parseInt(in.next());
	    h=Integer.parseInt(in.next());
	    s=Integer.parseInt(in.next());
	    if (n == 0)break;
	    for(int i=0;i<m;i++)num[i]=0;
	    for(int i=0;i<m;i++){
		int tmp;
		l[i]=Integer.parseInt(in.next());
		r[i]=Integer.parseInt(in.next());
	    }
	    for(int i=0;i<n;i++){
		double x,y;
		x=Double.parseDouble(in.next());
		y=Double.parseDouble(in.next());
		put_xy(m,w,x,y);
	    }
	    s=w*h-s;
	    System.out.println(n-solve(m,s,w));
	}
    }

    public static void main(String args[]){
	Main a = new Main();
	a.run();
    }
}
