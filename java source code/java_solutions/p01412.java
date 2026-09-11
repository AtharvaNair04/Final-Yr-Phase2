import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Main{
	Scanner sc=new Scanner(System.in);

	int INF=1<<28;
	double EPS=1e-12;

	int n;
	int[] a;

	void run(){
		n=sc.nextInt();
		a=new int[n];
		int n0=0, n1=0;
		for(int i=0; i<n; i++){
			a[i]=sc.nextInt();
			if(a[i]==0){
				n0++;
			}else if(a[i]==1){
				n1++;
			}
		}
		sort(a);
		int m=n-(n0+n1);
		int[] b=new int[m];
		for(int i=0; i<m; i++){
			b[i]=a[i+n0+n1];
		}

		if(n1==0&&n0%2==1){

			if(m>=3&&b[m-2]==2&&b[m-1]==3){
				b[m-2]=3;
				b[m-1]=2;
			}

			for(int i=1; i<m; i++){
				println(b[i]+"");
			}

			for(int i=0; i<n0-1; i+=2){
				println("0");
				println("0");
			}
			if(m>0){
				println(b[0]+"");
			}
			println("0");

		}else{

			if(m>=2&&b[m-2]==2&&b[m-1]==3){
				b[m-2]=3;
				b[m-1]=2;
			}

			for(int i=0; i<m; i++){
				println(b[i]+"");
			}

			// debug(n0,n1);

			for(int i=0; i<n0-1; i+=2){
				println("0");
				println("0");
			}
			if(n1>0){
				println("1");
			}
			if(n0%2==1){
				println("0");
			}
			for(int i=0; i<n1-1; i++){
				println("1");
			}
		}
	}

	void debug(Object... os){
		System.err.println(Arrays.deepToString(os));
	}

	void print(String s){
		System.out.print(s);
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		// System.setOut(new PrintStream(new BufferedOutputStream(System.out)));
		new Main().run();
	}
}
