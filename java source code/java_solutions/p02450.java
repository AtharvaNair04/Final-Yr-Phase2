import java.util.*;
import java.io.*;

public class Main {
	static PrintWriter out=new PrintWriter(System.out);
	
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		int n=Integer.parseInt(in.next());
		List<Integer> A=new ArrayList<>(),B=new ArrayList<>();
		for(int i=1;i<=n;i++)A.add(i);
		parm(A,B,-1,-1);
		out.flush();
	}
	
	static void parm(List<Integer> A,List<Integer> B,int u,int v) {
		List<Integer> ls=new ArrayList<>(A),res=new ArrayList<>(B);
		if(u!=-1)ls.remove(u);
		if(v!=-1)res.add(v);
		if(ls.size()==1) {
			res.add(ls.get(0));
			for(int i=0;i<res.size()-1;i++)out.print(res.get(i)+" ");
			out.println(res.get(res.size()-1));
			return;
		}
		for(int i=0;i<ls.size();i++) {
			parm(ls,res,i,ls.get(i));
		}
	}

}
