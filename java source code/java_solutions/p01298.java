import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	void run() {
		for (;;) {
			int n = sc.nextInt();
			int l = sc.nextInt();

			if(n ==0 && l==0){
				break;
			}
			int[] map = new int[86500];

			int[] s = new int[n];
			int[] t = new int[n];
			int[] u = new int[n];
			for (int i = 0; i < n; i++) {
				s[i] = sc.nextInt();
				t[i] = sc.nextInt();
				u[i] = sc.nextInt();
			}

			for (int i = 0; i < n; i++) {
				for (int j = s[i]; j < t[i]; j++) {
					map[j] = u[i];
				}
			}
//			System.out.println(Arrays.toString(map));
			double min = 0;
			double max = 1e7;
			for (; ;) {
				
				if(Math.abs(max - min) < 1e-8 ){
					break;
				}
//			min = max = 0.999;
				double med = (max+ min)/2;
				double f = l;
				boolean ok = true;
				double f1 = 0;
				for (int i = 0; i < 86400; i++) {
					
					f -= map[i];
					f += med;
					if(f > l){
						f =l;
					}
//					System.out.println(med+":"+i + " " + f);
					if(f < 0 ){
						ok = false;
						break;
					}
				}
				f1 = f;
				for (int i = 0; i < 86400; i++) {
					
					f -= map[i];
					f += med;
					if(f > l){
						f =l;
					}
//					System.out.println(med+":"+i + " " + f);
					if(f < 0 ){
						ok = false;
						break;
					}
				}
				if(f < f1){
					ok = false;
				}

				if(ok){
//					System.out.println("ok "  + med);
					max = med;
				}else{
//					System.out.println("ng " + med);
					min = med;
				}
			}
			System.out.printf("%.7f\n",max);
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}
