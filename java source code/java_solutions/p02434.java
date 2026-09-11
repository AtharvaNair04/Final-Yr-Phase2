import java.util.*;
import java.io.*;
public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		PrintWriter out=new PrintWriter(System.out);
		int n=Integer.parseInt(in.next()),q=Integer.parseInt(in.next());
		mylist A[]=new mylist[n];
		for(int i=0;i<n;i++)A[i]=new mylist();
		
		for(int i=0;i<q;i++){
			int query=Integer.parseInt(in.next());
			
			switch(query){
			case 0:
				int t1=Integer.parseInt(in.next()),x=Integer.parseInt(in.next());
				A[t1].ls.add(x);
				break;
				
			case 1:
				int t2=Integer.parseInt(in.next());
				if(A[t2].ls.isEmpty())out.println();
				else{
					for(int j=0;j<A[t2].ls.size()-1;j++)out.print(A[t2].ls.get(j)+" ");
					out.println(A[t2].ls.get(A[t2].ls.size()-1));
				}
				break;
				
			case 2:
				int t3=Integer.parseInt(in.next());
				A[t3].ls.clear();
				break;
			}
		}
		out.flush();
	}

}

class mylist{
	List<Integer> ls;
	
	mylist(){
		ls=new ArrayList<>();
	}
}
