import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			int n = sc.nextInt();
			if(n == 0 )break;
			else calc(n);
			
		}
	}
	public void calc(int n){
		int[] Is = new int[n];
		for(int i = 0; i < n; i++){
			Is[i] = sc.nextInt();
		}
		int m = 256;
		
		int s = 16;
		int a = 16;
		int c = 16;
		double minH = 1000000000;
		for(int ns = 0; ns < 16; ns++){			
			for(int na = 0; na < 16; na++){
				for(int nc = 0; nc < 16; nc++){
					int[] count = new int[257];
					int r = ns;
					for(int i = 0; i < n; i++){
						r = (na * r + nc) % m;
						int o = (Is[i] + r) % m; 
						count[o] += 1;
					}
					double h = 0;
					for(int i = 0; i < 257; i++){
						if(count[i] != 0){
							double t = ((double)count[i]) /((double) n);
							h += -1 * t * Math.log(t);
						}
					}
					if(h+1e-10 < minH){
						s = ns;
						a = na;
						c = nc;
						minH = h;
					}
					else if(h == minH && (ns < s || (s == ns && na < a) || (s == ns && a == na && nc < c))){
						s = ns;
						a = na;
						c = nc;
					}
				}
			}
		}
		System.out.println(s+ " " + a + " " + c);
	}

	
	public static void main(String[] args) {
		new Main().run();
	}
}
