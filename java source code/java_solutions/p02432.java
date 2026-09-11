import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		PrintWriter out=new PrintWriter(System.out);
		//List<Integer> DQ=new ArrayList<>();
		MyDQ DQ=new MyDQ();
		int q=in.nextInt();
		
		for(int i=0;i<q;i++) {
			int query=Integer.parseInt(in.next());
			
			switch(query) {
			case 0:
				int d=Integer.parseInt(in.next()),x=Integer.parseInt(in.next());
				DQ.push(d, x);
				break;
				
			case 1:
				int p=Integer.parseInt(in.next());
				out.println(DQ.get(p));
				break;
				
			case 2:
				int e=Integer.parseInt(in.next());
				DQ.pop(e);
				break;
			}
		}
		out.flush();
	}

}

class MyDQ{
	int DQ[];
	int size,top,btm;
	
	MyDQ(){
		DQ=new int[1000000];
		size=0;
		top=500000; btm=499999;
	}
	
	void push(int d,int x) {
		if(d==0) {
			top--;
			DQ[top]=x;
		}
		else {
			btm++;
			DQ[btm]=x;
		}
		size++;
	}
	
	void pop(int d) {
		if(d==0) top++;
		else btm--;
		size--;
	}
	
	int get(int i) {
		return DQ[top+i];
	}
}
