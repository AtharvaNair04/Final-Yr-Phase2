import java.util.*;
import java.io.*;
public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		
		int n=in.nextInt(),m=(int)Math.pow(2, n)-1;
		PrintWriter out=new PrintWriter(System.out);
		for(int i=0;i<=m;i++){
			String str=Integer.toBinaryString(i);
//			System.out.println(str);
			
			List<Integer> ls=new ArrayList<>();
			for(int j=0;j<str.length();j++){
				if(str.charAt(str.length()-1-j)=='1')ls.add(j);
			}
			
			out.print(i+":");
			for(int j=0;j<ls.size();j++)out.print(" "+ls.get(j));
			out.println();
		}
		out.flush();
	}

}
