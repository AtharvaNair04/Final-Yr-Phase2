import java.util.*;

import static java.lang.System.*;
import static java.lang.Math.*;

class Main {
	public static Scanner sc = new Scanner(in);
	public static Random rand=new Random();

	public void run() {
		int val=sc.nextInt();

		char exp=sc.next().charAt(0);
		while(exp!='='){
			int exp2=sc.nextInt();
			switch(exp){
			case '+':val+=exp2;break;
			case '-':val-=exp2;break;
			case '*':val*=exp2;break;
			case '/':val/=exp2;break;
			}
			exp=sc.next().charAt(0);
		}
		ln(val);
	}
	public static void main(String[] args) {
		new Main().run();
	}

	public int[] nextIntArray(int n){
		int[] res=new int[n];
		for(int i=0;i<n;i++){
			res[i]=sc.nextInt();
		}
		return res;
	}
	public static void pr(Object o) {
		out.print(o);
	}
	public static void ln(Object o) {
		out.println(o);
	}
	public static void ln() {
		out.println();
	}
}
