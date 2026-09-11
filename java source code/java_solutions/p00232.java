import java.util.Scanner;
public class Main {
	public static int MMAX = 5000 + 1;
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int z = sc.nextInt();
			if (x+y+z==0) {
				break;
			}
			int v[] = new int[x];
			for (int i=0;i<x;i++) {
				v[i] = sc.nextInt();
			}
			double p[][] = new double[y+1][MMAX];
			p[0][0]=1.0d;
			int event[] = new int[y+1];
			int eventa[] = new int[y+1];
			for (int i=0;i<z;i++) {
				int n = sc.nextInt();
				int e = sc.nextInt();
				int a = sc.nextInt();
				event[n]=e;
				eventa[n]=a;
			}
			//System.out.println("----");
			for (int i=0;i<y;i++) {
				for (int m=0;m<MMAX;m++) {
					if (p[i][m]==0.0) {
						continue;
					}
					//System.out.println(i+","+m+","+p[i][m]);
					for (int j=0;j<x;j++) {
						int money = m;
						int target = i+v[j];
						if (target>y) {
							target=y;
						}
						switch (event[target]) {
						case 1:
							target+=eventa[target];
							if (target>y) {
								target=y;
							}
							break;
						case 2:
							money+=eventa[target];
							break;
						case 3:
							money-=eventa[target];
							if (money<0) {
								money = 0;
							}
							break;
						}
						p[target][money]+=p[i][m]/x;
						//System.out.println(i+" To "+target+" :p="+p[i][m]/x+" : money=" + m + " To "+money);
					}
				}
			}
			double ans = 0.0;
			for (int i=0;i<MMAX;i++) {
				ans+=p[y][i]*i;
			}
			System.out.println((int) ans);
		}
	}
}
