import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int a =sc.nextInt();
		int b =sc.nextInt();
		int c=0;
		int[]k=new int[a];
		for(int i=0;i<a;i++) {
			 k[i]= sc.nextInt();
			
		}
		Arrays.sort(k);
		for(int i=0;i<b;i++) {
			c+=k[i];
		}
		System.out.println(c);
		
	}

}
