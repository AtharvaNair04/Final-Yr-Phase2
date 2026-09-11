import java.util.*;
import java.io.PrintWriter;
public class Main{
	public static void main(String[] args){
		Scanner sc=new Scanner(System.in);
		String s=sc.next();
		String t=sc.next();
		int[] sb=new int[s.length()+1];
		int[] tb=new int[t.length()+1];
		for(int i=1; i<=s.length(); i++){
			if(s.charAt(i-1)=='A'){
				sb[i]=sb[i-1]+2;
			}else{
				sb[i]=sb[i-1]+1;
			}
		}
		for(int i=1; i<=t.length(); i++){
			if(t.charAt(i-1)=='A'){
				tb[i]=tb[i-1]+2;
			}else{
				tb[i]=tb[i-1]+1;
			}
		}
		int q=sc.nextInt();
		PrintWriter p=new PrintWriter(System.out);
		for(int i=0; i<q; i++){
			int a=sc.nextInt();
			int b=sc.nextInt();
			int c=sc.nextInt();
			int d=sc.nextInt();
			if((sb[b]-sb[a-1]-(tb[d]-tb[c-1]))%3==0){
				p.println("YES");
			}else{
				p.println("NO");
			}
		}
		p.flush();
	}
}
