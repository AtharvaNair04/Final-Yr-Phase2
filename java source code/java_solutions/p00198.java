import java.util.*;
public class Main{
	public static void main(String[]agrs){
		Scanner sc = new Scanner(System.in);
		while(true) {
			int n = sc.nextInt();
			if(n == 0) break;
			String[][] list = new String[n][6];
			boolean[] a = new boolean[n];
			for(int i = 0; i < n; i++) {
				for(int j = 0; j < 6; j++) {
					list[i][j] = sc.next();
				}
			}
			for(int i = 0; i < n; i++) {
				IN:for(int j = i+1; j < n; j++) {
					if(a[i]) continue;
					for(int k = 0; k < 4; k++) {
							x(list[j]);
						for(int l = 0; l < 4; l++) {
							y(list[j]);
							for(int m = 0; m < 4; m++) {
								z(list[j]);
								if(c(list[i],list[j])) {
									a[j] = true;
									continue IN;
								}
							}
						}
					}
				}
			}
			int count = 0;
			for(int i = 0; i < n; i++) {
				if(a[i]) {
					count++;
				}
			}
			System.out.println(count);
		}
	}
	
	static void x(String[] a) {
		String tmp = a[1];
		a[1] = a[3];
		a[3] = a[4];
		a[4] = a[2];
		a[2] = tmp;
	}
	static void y(String[] a) {
		String tmp = a[0];
		a[0] = a[3];
		a[3] = a[5];
		a[5] = a[2];
		a[2] = tmp;
	}
	static void z(String[] a) {
		String tmp = a[0];
		a[0] = a[1];
		a[1] = a[5];
		a[5] = a[4];
		a[4] = tmp;
	}
	static boolean c(String[] a, String[] b) {
		for(int i = 0; i < 6; i++) {
			if(!a[i].equals(b[i])) {
				return false;
			}
		}
		return true;
	}
}
