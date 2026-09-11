import java.util.Scanner;


public class Main {
	
	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			int n = sc.nextInt();
			int a = sc.nextInt();
			int b = sc.nextInt();
			int c = sc.nextInt();
			int x = sc.nextInt();
			if(n == 0 && a == 0 && b == 0 && c == 0 && x == 0) break;
			else calc(n,a,b,c,x);
		}
	}
	public void calc(int n, int a, int b, int c, int x){
		int count = 0;
		int y[] = new int[n];
		for(int i = 0; i < n; i++) y[i] = sc.nextInt();
		int i = 0;
		while(i < n){
			if(i == 0 && y[i] == x) i++;
			else{
				count++;
				int t = a * x + b;
				x = t % c;
				if(x == y[i]) i++; 
				if(count > 10000)	{
					count = -1;
					break;
				}
			}
		}
		System.out.println(count);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
