import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a=sc.nextInt();
		int b=sc.nextInt();
		int[] c= new int[b];
		int d=a;
		int f=a;
		for(int i =0;i<b;i++) {
			c[i]=sc.nextInt();
		}
		for(int i =0;i<b;i++) {
			if(d==c[i]) {
				d++;
				i=-1;
			
			}
		}
		for(int i =0;i<b;i++) {
			if(f==c[i]&&f>0) {
				f--;
				i=-1;
			
			}
		}
		if(Math.abs(d-a)>=Math.abs(f-a)) {
		System.out.println(f);
		}else {
			System.out.println(d);
		}
		
	}

}
