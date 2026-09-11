import java.io.*;
import java.util.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	void run() {
		for (int c = 0;; c++) {
			int n = sc.nextInt();
			int t = sc.nextInt();
			if (n + t == 0) {
				break;
			}
			int m[] = new int[n];
			int l[] = new int[n];
			int k[] = new int[n];

			for (int i = 0; i < n; i++) {
				m[i] = sc.nextInt();
				l[i] = sc.nextInt();
				k[i] = sc.nextInt();
			}

			for (int z = 1;; z++) {
				boolean ok = false;
				int f = 0;
				int m2[] = new int[n];
				int st[] = new int[n]; // l,m , k
				int ope = z;
				int map[] = new int[n];
			
				for (int i = 0; i < n; i++) {
					m2[i] = l[i];
					map[i] = i;
				}
				
				for (int a = 0;;) {
	/*				System.out.println(a+":");
					for(int i = 0 ; i < n ; i++){
					System.out.println(" "+st[i]+" "+m2[i]);
					}*/
					if (f == n) {
						ok = true;
						break;
					}
					if (a >= t + 1) {

						break;
					}
					int mini = t + 1;
					int mini2 = t + 1;

					int j = 0;
					int n2 = n-f;
					
					for (int ii = 0; ii < n2; ii++ , j++) {
						int i = map[ii];
						if (st[i] == 1) {
							if (m2[i] <= a) {
								f++;
								if(n==f){
									break;
									
								}
								st[i] = 3;
								ope++;
								j--;
								continue;
							} else {
								mini = Math.min(m2[i], mini);
							}
						}
						if (st[i] == 0) {
							int za = a % (l[i] + k[i]);
							if (za <= l[i]) {
								if (ope > 0) {
									ope--;
									st[i] = 1;
									m2[i] = m[i] + a;
									mini = Math.min(mini, m2[i]);
								}
							} else {
								m2[i] =l[i]+k[i] - za + a;
								mini2 = Math.min(mini2, m2[i]);
							}
						}
						map[j] = i;
					}
					if (f == n) {
						ok = true;
						break;
					}

					if (ope > 0) {
						mini = Math.min(mini2, mini);
					}

					a = mini;

				}
				if (ok) {
					System.out.println( z);
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		new Main().run();

	}

}
