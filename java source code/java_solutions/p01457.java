import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		
			long q = sc.nextLong();
			calc(q);
			
		
	}
	public void calc(long q){
		long sum = 0;
		for(long i = 0; i < q; i++){
			long p = sc.nextLong();
			String c = sc.next();
			long n = sc.nextLong();
			
			int t = 0;
			if(c.equals("(")) t = 1;
			if(c.equals(")")) t = -1;
			
			sum += t * n;
			if(sum == 0) System.out.println("Yes");
			else System.out.println("No");
		}
		
	}
	public void calc2(int q){
		int sum = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < q; i++){
			int p = sc.nextInt();
			String c = sc.next();
			int n = sc.nextInt();


			if(c.equals("(")){
				if(list.size() == 0) list.add(n);
				else if(list.get(0) > 0) list.add(n);
				else{
					while(n > 0){
						if(list.size() == 0) {
							list.add(n);
							n = 0;
						}
						else if(list.get(0) * -1 <= n){
							n = n + list.get(0);
							list.remove(0);
						}
						else{
							list.add(list.get(0) + n);
							list.remove(0);
							n = 0;
						}
					}
				}
			}
			if(c.equals(")")) {
				if(list.size() == 0) list.add(n * -1);
				else if(list.get(0) < 0) list.add(n * -1);
				else{
					n = n * -1;
					while(n < 0){
						if(list.size() == 0){ 
							list.add(n);
							n = 0;
						}
						else if(list.get(0) <= n * -1){
							n = n + list.get(0);
							list.remove(0);
						}
						else {
							list.add(list.get(0) + n);
							list.remove(0);
							n = 0;
						}
					}
				}
			}
					
			if(list.size() == 0) System.out.println("Yes");
			else System.out.println("No");
		}
	}

	
	public static void main(String[] args) {
		
		new Main().run();
	}
}
