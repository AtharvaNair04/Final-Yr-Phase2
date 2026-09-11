import java.util.*;
import java.io.*;
public class Main {
	
	static List<binarySet> ls=new ArrayList<>();
	static char[] U;
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		
		int n=in.nextInt(),k=in.nextInt();
		
		U=new char[n];
		Arrays.fill(U, '0');
		
		List<Integer> T=new ArrayList<>();
		for(int i=0;i<k;i++)T.add(in.nextInt());
		
		solveSet(new ArrayList<Integer>(),T);
		ls.sort(Comparator.comparingInt(binarySet::getDecimal));
		
		PrintWriter out=new PrintWriter(System.out);
		out.println("0:");
		for(int i=0;i<ls.size();i++) {
			List<Integer> A=ls.get(i).ls;
			out.print(ls.get(i).dec+":");
			
			for(int j=0;j<A.size();j++)out.print(" "+A.get(j));
			out.println();
			out.flush();
		}
		
		out.flush();
	}
	
	static void solveSet(List<Integer> A,List<Integer> S) {
		
		for(int i=0;i<S.size();i++) {
			if(A.isEmpty() || S.get(i)>A.get(A.size()-1)) {
				List<Integer> t=new ArrayList<>(A);
				t.add(S.get(i));
				ls.add(new binarySet(t,U));
				solveSet(t,S);
			}
		}
	}

}
class binarySet{
	List<Integer> ls;
	int dec;
	binarySet(List<Integer> A,char[] c){
		ls=A;
		char[] t=c.clone();
		for(int i=0;i<ls.size();i++) {
			t[c.length-1-A.get(i)]='1';
		}
		dec=Integer.parseInt(String.valueOf(t),2);
	}
	
	int getDecimal() {
		return dec;
	}
}
