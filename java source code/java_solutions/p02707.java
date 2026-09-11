import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a=sc.nextInt();
		int t=0;
		int b[]=new int[a-1];
		for(int i=0;i<a-1;i++) {
			int k =sc.nextInt();
			b[k-1]++;
		}
		for(int j=0;j<a-1;j++) {	
			System.out.println(b[j]);
		}
		System.out.println(0);


	}

}
