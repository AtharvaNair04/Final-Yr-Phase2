import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
      	sc.nextLine();
		String s=sc.nextLine();
		char[] c=s.toCharArray();
		int[] r=new int[n+1];
		for(int i=0;i<n;i++){
			if(c[i]=='R'){
				r[i+1]=r[i]+1;
			}else{
				r[i+1]=r[i];
			}
		}
		int min=n;
		int cost;
		int r2w;
		int w2r;
		for(int i=0;i<=n;i++){
			r2w=r[n]-r[i];
			w2r=i-r[i];
			if(r2w<w2r){
				cost=w2r;
			}else{
				cost=r2w;
			}			
			if(cost<min){
				min=cost;
			}
		}		
		System.out.println(min);
	}
}
