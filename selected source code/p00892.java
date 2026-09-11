import java.util.*;
import java.lang.*;
import java.math.*;

public class Main {
	Scanner sc = new Scanner(System.in);

	class Z {
		int x;
		double z;

		Z(int x, double z) {
			this.x = x;
			this.z = z;
		}
	}

	class Y {
		int x;
		double y;

		Y(int x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	void run() {
		for (;;) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}

			Y[] y = new Y[n];
			Z[] z = new Z[m];

			boolean xd[] = new boolean[204];

			int ymin = n + 1;
			int xminy = 300;
			int ymax = n + 1;
			int xmaxy = 0;
			
			for (int i = 0; i < n; i++) {
				int x = sc.nextInt() + 101;
				if(xminy == x){
					if(ymin == i-1){
						ymin++;
					}
				}
				if(xmaxy == x){
					if(ymax == i-1){
						ymax++;
					}
				}
				if (xminy > x) {
					ymin = i;
					xminy = x;
				}
				if (xmaxy < x) {
					ymax = i;
					xmaxy = x;
				}
				xd[x] = true;
				y[i] = new Y(x, sc.nextDouble());
			}

			int zmin = n + 1;
			int xminz = 300;
			int zmax = n + 1;
			int xmaxz = 0;
			for (int i = 0; i < m; i++) {
				int x = sc.nextInt() + 101;
				
				if(xminz == x){
					if(zmin == i-1){
						zmin++;
					}
				}
				if(xmaxz == x){
					if(zmax == i-1){
						zmax++;
					}
				}

				
				if (xminz > x) {
					zmin = i;
					xminz = x;
				}
				if (xmaxz < x) {
					zmax = i;
					xmaxz = x;
				}
				xd[x] = true;
				z[i] = new Z(x, sc.nextDouble());
			}

			int xmin = Math.max(xminy, xminz);
			int xmax = Math.min(xmaxz, xmaxy);

			int v = 0;

			for (int i = xmin; i <= xmax; i++) {
				if (xd[i]) {
					v++;
				}
			}

			int x[] =new int[v];

			{
				int j = 0;
				for (int i = xmin; i <= xmax; i++) {
					if (xd[i]) {
						x[j] = i;
						j++;
					}
				}
			}

			double y_m[] = new double[v];
			{
				int j = ymin;
				for (int i = 0; i < v; i++) {

//					System.out.println(x[i] - 101 +" "+j+"("+y[j].y+")"+"----");
					if (y[j].x == x[i]) {
						y_m[i] = y[j].y;
						continue;
					}
					for (; y[(j + 1) % n].x < x[i]; j = (j + 1) % n) {
	//					 System.out.println(y[(j+1)%n].x+" "+x[i]);
					}

					if (y[(j + 1) % n].x == x[i]) {
						j++;
						j = j % n;
						y_m[i] = y[j].y;
						continue;
					}

					double ry = (y[(j + 1) % n].y - y[j].y) * (x[i] - y[j].x)
							/ (y[(j + 1) % n].x - y[j].x) + y[j].y;
					y_m[i] = ry;
				}
			}

			double z_m[] = new double[v];
			{
				int j = zmin;
				for (int i = 0; i < v; i++) {

					if (z[j].x == x[i]) {
						z_m[i] = z[j].z;
						continue;
					}
					for (; z[(j + 1) % m].x < x[i]; j = (j + 1) % m)
						;

					if (z[(j + 1) % m].x == x[i]) {
						j++;
						j = j % m;
						z_m[i] = z[j].z;
						continue;
					}

					double rz = (z[(j + 1) % m].z - z[j].z) * (x[i] - z[j].x)
							/ (z[(j + 1) % m].x - z[j].x) + z[j].z;
					z_m[i] = rz;
				}
			}

			double y_k[] = new double[v];
			{
				int j = ymax;
				for (int i = v - 1; i >= 0; i--) {

					if (y[j].x == x[i]) {
						y_k[i] = y[j].y;
						continue;
					}
					for (; y[(j + 1) % n].x > x[i]; j = (j + 1) % n)
						;

					if (y[(j + 1) % n].x == x[i]) {
						j++;
						j = j % n;
						y_k[i] = y[j].y;
						continue;
					}

					double ry = (y[(j + 1) % n].y - y[j].y) * (x[i] - y[j].x)
							/ (y[(j + 1) % n].x - y[j].x) + y[j].y;
					y_k[i] = ry;
				}
			}

			double z_k[] = new double[v];
			{
				int j = zmax;
				for (int i = v - 1; i >= 0; i--) {

					if (z[j].x == x[i]) {
						z_k[i] = z[j].z;
						continue;
					}
					for (; z[(j + 1) % m].x > x[i]; j = (j + 1) % m)
						;

					if (z[(j + 1) % m].x == x[i]) {
						j++;
						j = j % m;
						z_k[i] = z[j].z;
						continue;
					}

					double rz = (z[(j + 1) % m].z - z[j].z) * (x[i] - z[j].x)
							/ (z[(j + 1) % m].x - z[j].x) + z[j].z;
					z_k[i] = rz;
					
				}
			}

			double ans = 0;
			{
				int i = 0;
//				System.out.println((x[i] - 101) + " " + y_k[i] + " " + y_m[i]
//						+ " " + z_k[i] + " " + z_m[i]);
			}
			for (int i = 0; i < v - 1; i++) {

//				System.out.println((x[i + 1] - 101) + " " + y_k[i + 1] + " "
//						+ y_m[i + 1] + " " + z_k[i + 1] + " " + z_m[i + 1]);

				double dx = x[i + 1] - x[i];
				double y2 = y_k[i + 1] - y_m[i + 1];
				double y0 = y_k[i] - y_m[i];

				double yl = Math.max(y2, y0);
				double ys = Math.min(y2, y0);

				double z2 = z_k[i + 1] - z_m[i + 1];
				double z0 = z_k[i] - z_m[i];

				double zl = Math.max(z2, z0);
				double zs = Math.min(z2, z0);
				
				zl = z0;
				zs = z2;
				yl = y0;
				ys = y2;

				double a = (yl - ys) / dx;
				double b = (zl - zs) / dx;
				double v2 = (a * b * dx * dx * dx / 3. + (a * zs + b * ys) *dx* dx / 2. + ys
						* zs * dx) ;
//				System.out.println(" "+a+ " "+b+" "+ys+" "+zs+" "+ v2);
				ans+=Math.abs(v2);
			}

			System.out.println(ans);
		}

	}

	public static void main(String[] args) {
		Main m = new Main();
		m.run();
	}
}
