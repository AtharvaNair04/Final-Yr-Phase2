import java.util.*;
public class Main {
	static int A[],Big[],Small[];
	static boolean bi=false,sm=false;
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		int n=Integer.parseInt(in.next());A=new int[n];
		for(int i=0;i<n;i++)A[i]=Integer.parseInt(in.next());
		Big=A.clone(); Small=A.clone();
		if(smaller(n-2)){
			for(int i=0;i<n-1;i++)System.out.print(Small[i]+" ");
			System.out.println(Small[n-1]);
		}
		
		for(int i=0;i<n-1;i++)System.out.print(A[i]+" ");
		System.out.println(A[n-1]);
		
		if(bigger(n-2)) {
			for(int i=0;i<n-1;i++)System.out.print(Big[i]+" ");
			System.out.println(Big[n-1]);
		}
	}
	
	static boolean smaller(int p) {
		//System.out.println(p);
		if(p<0)return false;
		int min=A[A.length-1];
		for(int i=A.length-2;i>p;i--) {
			min=Math.min(min, A[i]);
		}
		//System.out.println(min+" "+A[p]);
		if(min>A[p])return smaller(--p);
		else {
			List<Integer> T=new ArrayList<>();
			for(int i=A.length-1;i>=p;i--)T.add(A[i]);
			Collections.sort(T,Collections.reverseOrder());
			for(int i=0;i<T.size();i++) {
				if(T.get(i)==A[p]) {
					Small[p]=T.get(i+1); T.remove(i+1);
				}
			}
			for(int i=0;i<T.size();i++)Small[p+1+i]=T.get(i);
			return true;
		}
	}
	
	static boolean bigger(int p) {
		//System.out.println(p);
		if(p<0)return false;
		int max=A[A.length-1];
		for(int i=A.length-2;i>p;i--) {
			max=Math.max(max, A[i]);
		}
		//System.out.println(max+" "+A[p]);
		if(max<A[p])return bigger(--p);
		else {
			List<Integer> T=new ArrayList<>();
			for(int i=A.length-1;i>=p;i--)T.add(A[i]);
			Collections.sort(T);
			for(int i=0;i<T.size();i++) {
				if(T.get(i)==A[p]) {
					Big[p]=T.get(i+1); T.remove(i+1);
					break;
				}
			}
			for(int i=0;i<T.size();i++)Big[p+1+i]=T.get(i);
			return true;
		}
	}

}
