import java.io.*;
import java.util.*;


// æ­Ôá¤Æ±ëB
// âè¶É¢ÄÈ­ÄàâèZbgÍ¡Åæªª0ÅI¹Å éB
// âèZbgÌJèÔµÉú»Rê

// 2011/10/17

//@2016 v[ÌÄõ
public class Main {

	class Double2 {
		
		public double x, y;
		
		public Double2() {/**/}
		public Double2( double xx, double yy ) { x=xx; y=yy; }
		public Double2( Double2 s ) { x=s.x; y=s.y; }

		public double length2() {
			return x*x + y*y;
		}

		public double length() {
			return Math.sqrt(x * x + y * y);
		}
		public double length(Double2 d2) {
			return Math.sqrt((d2.x - x) * (d2.x - x) + (d2.y - y) * (d2.y - y));
		}
	
		/* (ñ Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("[%g %g]", x, y);
		}
	}
	
	
	// A§ûö®ðð­
	private Double2 houteisiki(double a, double b, double c, double d, double e, double f) {
	
		double r = (a * d - b * c);
		if (r == 0)
			return null; // ðÈµ
		r = 1 / r;
		double na = r * d;
		double nb = -r * b;
		double nc = -r * c;
		double nd = r * a;
		
		double x = na * e + nb * f;
		double y = nc * e + nd * f;
		
		return new Double2(x, y);
	}
	
	double v1; // nãÌ¬³
	double v2; // Ì¬³
	
	/**
	 * üÜ_ðßé
	 * @param p0 Ó
	 * @param p1 Ó
	 * @param t  ÚWn_
	 * @param sign  1 or -1
	 * @return ðªÈ¢Æ«Ínull
	 */
	private Double2 sub(Double2 p0, Double2 p1, Double2 t, double sign) {
		
		double bx = p1.x - p0.x;
		double by = p1.y - p0.y;
		
		// ñ]px
		double cos = v2 / v1;
		double sin = Math.sqrt(1 - cos * cos) * sign;
		
		// ñ]ãÌxNg
		double kx = cos * bx - sin * by;
		double ky = sin * bx + cos * by;
		
		log.printf("kxky=%f %f\n", kx, ky);
		
		double a = by;
		double b = -bx;
		double e = a* p0.x + b* p0.y;
		double c = ky;
		double d = -kx;
		double f = c* t.x + d* t.y; 

		Double2 p = houteisiki(a, b, c, d, e, f);
		return p; // ðÈµÍnull
	}
	
	// 1ü²¸·é
	double calc(Double2[] poul, Double2 start, Double2 goal) {
		// ÜÁ·®j®
		double min = start.length(goal) / v2;
		
		double ruit = 0; // ÝÏÔ
		
		for(int i = 0; i < poul.length - 1; i++) {
			Double2 h0 = poul[i];
			Double2 h1 = poul[i+1];
			
			//ruit += h0.length(h1) / v1;

			Double2 p11 = sub(h0, h1, start, 1);
			log.printf("üÜ_%s\n", p11);
			Double2 p12 = sub(h0, h1, start, -1);
			log.printf("üÜ_%s\n", p12);
			Double2 p21 = sub(h0, h1, goal, 1);
			log.printf("üÜ_%s\n", p21);
			Double2 p22 = sub(h0, h1, goal, -1);
			log.printf("üÜ_%s\n", p22);
			
			// h0 ©çÚInÖ
			if (p21 != null)
			{			
				double len = 99999999999.;
//				if (p21 == null)
//					log.printf("d");
				len = Math.min(len, h0.length(p21));
				len = Math.min(len, h0.length(p22));
				double t = ruit + len / v1 + goal.length(p21) / v2;
				
				min = Math.min(min, t);
			}
			
			
			// h1ÌÝÏ£ h0->h1ÖÓðà­©
			double newRuit = ruit + h0.length(h1) / v1;
			
			//ÅÌÊu©çj¢ÅÓÉÌÁÄh1Ös­
			if (p11 != null) {
				double len = 99999999999.;
				len = Math.min(len, p11.length(h1));
				len = Math.min(len, p12.length(h1));
				double t = len / v1 + start.length(p11) / v2;

				// ¬³¢Ù¤
				newRuit = Math.min(newRuit, t);
			}
			ruit = newRuit;
			
			// ÁèÌÓ¾¯ðéû@
			if (p11 == null || p12 == null || p21 == null || p22 == null)
				continue;
			
			double len = 99999999999.;
			if (p11 != null && p21 != null)
				len = Math.min(len, p11.length(p21));
			if (p11 != null && p22 != null)
				len = Math.min(len, p11.length(p22));
			if (p12 != null && p21 != null)
				len = Math.min(len, p12.length(p21));
			if (p12 != null && p22 != null)
				len = Math.min(len, p12.length(p22));
			log.printf("len=%f\n", len);
		
			double t = len / v1 + (start.length(p11) + goal.length(p21)) / v2;
			log.printf("t=%f\n", t);

			min = Math.min(min, t);
		}
		
//		// Óðñé
//		for(i = 0; i < N + 1; i++) {
//			Double2 h0 = t2[i];
//			Double2 h1 = t2[i+1];
//
//			Double2 p11 = sub(h0, h1, h0, 1);
//			log.printf("üÜ_%s\n", p11);
//			Double2 p12 = sub(h0, h1, h0, -1);
//			log.printf("üÜ_%s\n", p12);
//			Double2 p21 = sub(h0, h1, s2, 1);
//			log.printf("üÜ_%s\n", p21);
//			Double2 p22 = sub(h0, h1, s2, -1);
//			log.printf("üÜ_%s\n", p22);
//
//			if (p11 == null || p12 == null || p21 == null || p22 == null) {
//			
//			}
//			else {
//			
//				double len = 99999999999.;
//				len = Math.min(len, p11.length(p21));
//				len = Math.min(len, p11.length(p22));
//				len = Math.min(len, p12.length(p21));
//				len = Math.min(len, p12.length(p22));
//				log.printf("len=%f\n", len);
//			
//				double t = ruit + len / v1 + s2.length(p21) / v2;
//				log.printf("t=%f\n", t);
//	
//				min = Math.min(min, t);
//			
//			}
//			ruit += h0.length(h1) / v1;
//		}
//		
//		
//		ruit = 0; // ÝÏÔ
//		for(i = 0; i < N + 1; i++) {
//			Double2 h0 = t2[N+1 -i];
//			Double2 h1 = t2[N+1-(i+1)];
//
//			Double2 p11 = sub(h0, h1, h0, 1);
//			log.printf("üÜ_%s\n", p11);
//			Double2 p12 = sub(h0, h1, h0, -1);
//			log.printf("üÜ_%s\n", p12);
//			Double2 p21 = sub(h0, h1, s2, 1);
//			log.printf("üÜ_%s\n", p21);
//			Double2 p22 = sub(h0, h1, s2, -1);
//			log.printf("üÜ_%s\n", p22);
//
//			if (p11 == null || p12 == null || p21 == null || p22 == null) {
//			
//			}
//			else {
//			
//				double len = 99999999999.;
//				len = Math.min(len, p11.length(p21));
//				len = Math.min(len, p11.length(p22));
//				len = Math.min(len, p12.length(p21));
//				len = Math.min(len, p12.length(p22));
//				log.printf("len=%f\n", len);
//			
//				double t = ruit + len / v1 + s2.length(p21) / v2;
//				log.printf("t=%f\n", t);
//	
//				min = Math.min(min, t);
//			
//			}
//			ruit += h0.length(h1) / v1;
//		}
		return min;
	}
	
	// WüÍæè@1sªÌXy[XæØèÌ®lðÇÞ
	private int[] readIntArray() throws IOException {
		
		String s = reader.readLine();
		String[] sp = s.split(" ");
		int[] a = new int[sp.length];
		for(int i = 0; i < sp.length; i++) {
			a[i] = Integer.parseInt(sp[i]);
		}
		return a;
	}
	
	
	// C return falseÅ¨µÜ¢
	boolean main() throws IOException {

		int N = readIntArray()[0];
		if (N == 0)
			return false;

		// ½p`Ì¸_
		int[] tx = new int[N];
		int[] ty = new int[N];
		
		String s = reader.readLine();
		String[] sp = s.split(" ");
		for(int i = 0; i < N; i++) {
			tx[i] = Integer.parseInt(sp[i * 2 + 0]);
			ty[i] = Integer.parseInt(sp[i * 2 + 1]);
		}
		int tg = readIntArray()[0]; // nãÌ¬³Ìt
		v1 = 1. / tg;
		int tw = readIntArray()[0]; // Ì¬³Ìt
		v2 = 1. / tw;
		int[] ir = readIntArray();
		int xs = ir[0]; // ÄõÊu
		int ys = ir[1];
		ir = readIntArray();
		int xt = ir[0]; // ­Êu
		int yt = ir[1];

		
		// ÄõÍÇÌÓãÉ¢é©H
		int i;
		for(i = 0; i < N; i++) {
			if ((tx[(i+1)%N] - xs) * (ty[i] - ys) == (tx[i] - xs) * (ty[(i+1)%N] - ys)) { // ®Z
				break;
			}
		}
		int on = i;
		assert on < N;
		log.printf("on=%d\n", on); 

		Double2 start = new Double2(xs, ys);
		Double2 goal = new Double2(xt, yt);

		
		//@½p`f[^ìè¼µ  ÄõÊuðJn_É·é
		Double2[] t2 = new Double2[N + 2];

		t2[0] = start;
		t2[N + 1] = start;
		for(i = 0; i < N; i++) {
			t2[i + 1] = new Double2(tx[(on + 1 + i) % N], ty[(on + 1 + i) % N]);
		}
		double min1 = calc(t2, start, goal);
		
		// tüè
		t2[0] = start;
		t2[N + 1] = start;
		for(i = 0; i < N; i++) {
			t2[N - i] = new Double2(tx[(on + 1 + i) % N], ty[(on + 1 + i) % N]);
		}
		double min2 = calc(t2, start, goal);

		double min = Math.min(min1, min2);
		
		// ð\¦
		System.out.printf("%.14f\n", min);
		
		return true;
	}
	
	static PrintStream log;
	static BufferedReader reader;
	
//	private final static boolean DEBUG = true;  // debug
	private final static boolean DEBUG = false; // release
	
	public static void main(String[] args) throws IOException {

		if (DEBUG) {
			log = System.out;
			
			String inputStr = "4\n0 0 10 0 10 10 0 10\n10\n12\n0 5\n9 5\n0\n"; // 108.0
//			String inputStr = "4\n0 0 10 0 10 10 0 10\n10\n12\n0 0\n9 1\n0\n"; // 96.63324958071081
//			String inputStr = "4\n0 0 10 0 10 10 0 10\n10\n12\n0 1\n9 1\n0\n"; // 103.2664991614216
//			String inputStr = "8\n2 0 4 0 6 2 6 4 4 6 2 6 0 4 0 2\n10\n12\n3 0\n3 5\n0\n"; // 60.0

			reader = new BufferedReader(new StringReader(inputStr)); 

		}
		else {
			log = new PrintStream(new OutputStream() { public void write(int b) {} } ); // «ÌÄ
			reader = new BufferedReader(new InputStreamReader(System.in)); // R\[©ç
		}

		for(int loop = 0;; loop++) {
			boolean b = new Main().main();
			if (!b)
				break;
		}		
		
		reader.close();
	}

}
