import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * AIZU ONLINE JUDGE
 * 2705 Kuru Kuru Door
 *    2018/02/11
 */
public class Main {

    class Graphics2D {
        void draw(Shape s) {
        }
        void setColor(Color c) {
        }
    }

    static class Double2 {

        public double x, y;

        public Double2() {/**/}
        public Double2( double xx, double yy ) { x=xx; y=yy; }
        public Double2( Double2 s ) { x=s.x; y=s.y; }

        @Override
        public String toString() {
            return String.format("[%g %g]", x, y);
        }

        // 極座標から構築
        public static Double2 createFromPolar(double r, double theta) {
            return new Double2(r * Math.cos(theta), r* Math.sin(theta));
        }

        public double length2() {
            return x*x + y*y;
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }
        public double length2(Double2 d2) {
            return (d2.x - x) * (d2.x - x) + (d2.y - y) * (d2.y - y);
        }
        public double length(Double2 d2) {
            return Math.sqrt((d2.x - x) * (d2.x - x) + (d2.y - y) * (d2.y - y));
        }
        public double atan2() {
            return Math.atan2(y, x);
        }
        public double atan2(Double2 d2) {
            return Math.atan2(d2.y - y, d2.x - x);
        }

        public Double2 add(Double2 d) {
            x += d.x;
            y += d.y;
            return this;
        }
        public Double2 sub(Double2 d) {
            x -= d.x;
            y -= d.y;
            return this;
        }

        // 自分を反転する
        public void inv() {
            x = -x;
            y = -y;
        }
    }

    // ang正規化 -Math.PI <= th <= Math.PI
    static double angNorm(double th) {
        if (th > Math.PI) {
            int k = (int)Math.floor((th + Math.PI) / (Math.PI * 2));
            th -= k * (Math.PI * 2);
        }
        if (th < -Math.PI) {
            int k = (int)Math.floor((-th + Math.PI) / (Math.PI * 2));
            th += k * (Math.PI * 2);
        }
        return th;
    }

    // 距離
    class Dist {
        String str;
        double dist;
        public Dist(String str, double dist) {
            this.str = str;
            this.dist = dist;
            if (dist < 0) {
                log.printf("!!!Dist < 0 %s %f\n", str, dist);
                assert dist >= 0;
            }
        }
    }

    // 片側
    class Kata {
        int n;
        int f;
        double r;
        double R;
        Double2 S;
        Double2 S1;
        Double2 S2;
        Double2 D1;
        Double2 D2;
        Double2 A1;
        Double2 A2;
        double ang_S1;
        double ang_S2;
        double ang_A1;
        double ang_A2;
        double ang_D1;
        double ang_D2;
        double ang_C1;
        boolean existS1; // A側
        boolean existS2; // D側
        boolean existD;
        boolean existA;

        Double2 t; // 最終点 getLength()の戻り

        List<Dist> getLength() {
            List<Dist> dist = new ArrayList<Dist>();
            t = S;
            if (existD) {
                if (existS2) {
                    dist.add(new Dist("S-S2", S.length(S2)));
                    dist.add(new Dist("S2-D1", angNorm(ang_S2 - ang_D1) * f * (R + r)));
                }
                else {
                    dist.add(new Dist("S-D1", S.length(D1)));
                }
                dist.add(new Dist("D1-D2", angNorm(ang_D1 - ang_D2) * f * r));
                t = D2;
            }
            if (existA) {
                if (existS1) {
                    dist.add(new Dist("S-S1", S.length(S1)));
                    dist.add(new Dist("S1-A1", angNorm(ang_A1 - ang_S1) * f * (R + r)));
                }
                else {
                    dist.add(new Dist("t-A1", t.length(A1)));
                }
                dist.add(new Dist("A1-A2", angNorm(ang_A2 - ang_A1) * f * r));
                t = A2;
            }
            return dist;
        }

        // 片側計算 f = 1 or -1
        void kata(Graphics2D g, int n, double r, double R, Double2 S, int f) {

            this.n = n;
            this.r = r;
            this.R = R;
            this.S = S;
            this.f = f;
            double r_c = r / Math.sin(Math.PI / n / 2);

            double ang_A;
            double ang_D;
            Double2 A;
            Double2 D;

            ang_A = Math.PI - Math.PI / 2 / n;
            A = Double2.createFromPolar(R, ang_A);
            ang_D = Math.PI + Math.PI / 2 / n;
            D = Double2.createFromPolar(R, ang_D);
            if (f < 0) {
                ang_A = Math.PI / 2 / n;
                A = Double2.createFromPolar(R, ang_A);
                ang_D = - Math.PI / 2 / n;
                D = Double2.createFromPolar(R, ang_D);
            }

            // S2 D側の回転ドアの外側
            ang_S2 = S.atan2() - Math.acos((R + r) / S.length()) * f;
            ang_D1 = ang_D;
            if (S.y < 0 && angNorm(ang_S2 - ang_D1) * f > 0) {
                // S2あり
                existS2 = true;
                g.setColor(Color.GREEN);
                S2 = Double2.createFromPolar(R + r, ang_S2);
                g.draw(new Ellipse2D.Double(S2.x - r, S2.y - r, r * 2 , r * 2));
            }
            else {
                // S2なし
                // S-D
                ang_D1 = D.atan2(S) - Math.acos(r / D.length(S)) * f;
            }

            // ang_D2は候補が２つある
            double ang_D2_C = ang_D - Math.acos((r_c - r) / A.length()) * f;
            double ang_D2_A = Math.PI /2+ Math.acos((r * 2) / (A.y * 2)) * f;
            ang_D2 = ang_D2_C;
            if (angNorm(ang_D2_C - ang_D2_A) * f > 0) {
                ang_D2 = ang_D2_A;
            }
            if (angNorm(ang_D1 - ang_D2) * f < 0) {
                // Dなし
            }
            else {
                // Dあり
                existD = true;
                D1 = Double2.createFromPolar(r, ang_D1); D1.add(D);
                g.setColor(Color.BLUE);
                g.draw(new Ellipse2D.Double(D1.x - r, D1.y - r, r * 2 , r * 2));
            }

            // S1 A側の回転ドアの外側
            ang_S1 = S.atan2() + Math.acos((R + r) / S.length()) * f;
            ang_A1 = ang_A;
            if (!existD && angNorm(ang_A1 - ang_S1) * f > 0) {
                // S1あり
                existS1 = true;
                S1 = Double2.createFromPolar(R + r, ang_S1);
                g.setColor(Color.GREEN);
                g.draw(new Ellipse2D.Double(S1.x -r, S1.y - r, r * 2 , r * 2));
            }
            else {
                // S1なし
                if (existD) {
                    // D-A
                    ang_D2 = ang_D2_A;
                    ang_A1 = ang_D2 - Math.PI;
                }
                else {
                    // S-A
                    ang_A1 = A.atan2(S) + Math.acos(r / A.length(S)) * f;
                }
            }

            // A-C
            ang_A2 = ang_A + Math.PI - Math.acos((r_c + r) / R) * f;
            if (angNorm(ang_A2 - ang_A1) * f < 0) {
                // Aなし
                if (existD) {
                    // Dあり
                    // D-C
                    ang_D2 = ang_D2_C;
                    ang_C1 = ang_D2;
                }
                else {
                    ang_C1 = S.atan2() - Math.acos(r_c / S.length()) * f;
                }
            }
            else {
                // Aあり
                existA = true;
                ang_C1 = ang_A2 + Math.PI;
            }
            if (existD) {
                D2 = Double2.createFromPolar(r, ang_D2); D2.add(D);
                g.setColor(Color.RED);
                g.draw(new Ellipse2D.Double(D2.x - r, D2.y - r, r * 2 , r * 2));
            }
        }
    }


    double calc(int n, double r, double R, Double2 S, Double2 T, int ff) {
        Graphics2D g = new Graphics2D();

        double total = 0;
        double r_c = r / Math.sin(Math.PI / n / 2);

        if (r_c + r > R) {
            total = -1; // 到達不能
        }
        else {
            double ang_A = Math.PI - Math.PI / 2 / n;
            Double2 A = Double2.createFromPolar(R, ang_A);
            double ang_Ad = Math.PI / 2 / n;
            Double2 Ad = Double2.createFromPolar(R, ang_Ad);

            Kata k0 = new Kata();
            Kata k1 = new Kata();
            k0.kata(g, n, r, R, S, 1);
            k1.kata(g, n, r, R, T, -1);

            Double2 C1 = null;
            Double2 C2 = null;

            if (angNorm(k0.ang_C1 - k1.ang_C1) < 0) {
                // Cなし

                boolean mod = false;
                for(int i = 0; i < 10; i++) {
                    mod = false;
                    log.printf("i = %d\n", i);
                if (k1.existA) {
                    k0.ang_A2 = -Math.PI / 2;
                }
                else {
                    Double2 t = new Double2(T);
                    t.sub(A);
                    k0.ang_A2 = t.atan2() - Math.acos(r / t.length());
                }
                boolean e = (angNorm(k0.ang_A2 - k0.ang_A1) > 0);
                if (e != k0.existA) {
                    mod = true;
                    k0.existA = e;
                }

                if (k0.existA) {
                    k1.ang_A2 = -Math.PI / 2;
                }
                else {
                    Double2 t = new Double2(S);
                    t.sub(Ad);
                    k1.ang_A2 = t.atan2() + Math.acos(r / t.length());
                }
                e = (angNorm(k1.ang_A2 - k1.ang_A1)*-1 > 0);
                if (e != k1.existA) {
                    mod = true;
                    k1.existA = e;
                }
                if (!mod)
                    break;
                }
            }
            else {
                // Cあり
                C1 = Double2.createFromPolar(r_c, k0.ang_C1);
                g.setColor(Color.BLUE);
                g.draw(new Ellipse2D.Double(C1.x -r, C1.y - r, r * 2 , r * 2));
                C2 = Double2.createFromPolar(r_c, k1.ang_C1);
                g.setColor(Color.RED);
                g.draw(new Ellipse2D.Double(C2.x -r, C2.y - r, r * 2 , r * 2));
            }

            if (k0.existA) {
                k0.A1 = Double2.createFromPolar(r, k0.ang_A1); k0.A1.add(A);
                g.setColor(Color.BLUE);
                g.draw(new Ellipse2D.Double(k0.A1.x -r, k0.A1.y - r, r * 2 , r * 2));
                k0.A2 = Double2.createFromPolar(r, k0.ang_A2); k0.A2.add(A);
                g.setColor(Color.RED);
                g.draw(new Ellipse2D.Double(k0.A2.x -r, k0.A2.y - r, r * 2 , r * 2));
            }
            if (k1.existA) {
                k1.A1 = Double2.createFromPolar(r, k1.ang_A1); k1.A1.add(Ad);
                g.setColor(Color.BLUE);
                g.draw(new Ellipse2D.Double(k1.A1.x -r, k1.A1.y - r, r * 2 , r * 2));
                k1.A2 = Double2.createFromPolar(r, k1.ang_A2); k1.A2.add(Ad);
                g.setColor(Color.RED);
                g.draw(new Ellipse2D.Double(k1.A2.x -r, k1.A2.y - r, r * 2 , r * 2));
            }

            List<Dist> dist = new ArrayList<Dist>();
            dist.addAll(k0.getLength());
            dist.addAll(k1.getLength());
            for(Dist d: dist) {
                log.printf("%6s %f\n", d.str, d.dist);
                total += d.dist;
            }
            if (C1 != null) {
                total += k0.t.length(C1);
                total += k1.t.length(C2);
                total += angNorm(k0.ang_C1 - k1.ang_C1) * r_c;
            }
            else {
                total += k0.t.length(k1.t);
            }
        }

        log.printf("total = %f\n", total);
        return total;
    }

    int no = 0;

	boolean main() throws IOException {

		int[] ir = readIntArray();
		int n = ir[0];
		if (n == 0)
		    return false;

        ir = readIntArray();
        int r = ir[0];
        int R = ir[1];
        ir = readIntArray();
        Double2 S = new Double2(ir[0], ir[1]);
        ir = readIntArray();
        Double2 T = new Double2(ir[0], ir[1]);

        ++no;
        log.printf("\n----------\nNo.%d\n", no);
		log.printf("n = %d\n", n);
        log.printf("r, R = %d, %d\n", r, R);
        log.printf("S = %s\n", S);
        log.printf("T = %s\n", T);

        double r_c = r / Math.sin(Math.PI / n / 2);
        log.printf("r_c = %f\n",  r_c); // OからCの中心までの距離

        double total = 0;

        if (r_c + r > R) {
            total = -1; // 到達不能
            result.printf("-1\n");
        }
        else {
            double total0 = 0;
            double total1 = 0;
            if (S.y >= 0 || T.y >= 0) {
                total0 = calc(n, r, R, S, T, 0);
            }

            // y方向反転
            if (S.y <= 0 || T.y <= 0) {
                total1 = calc(n, r, R, new Double2(S.x, -S.y), new Double2(T.x, -T.y), 1);
            }
            log.printf("total0 = %f total1 = %f\n",  total0, total1);
            if (total0 == 0)
                total = total1;
            else if (total1 == 0)
                total = total0;
            else
                total = Math.min(total0, total1);
            //result.printf("%.10f %f %f\n", total, total0, total1);
            result.printf("%.10f\n", total);
        }
		return true; // 正常終了 次へ
	}



	static long time0;

    PrintStream log;
    PrintStream result;
    BufferedReader systemin;

    static Main instance = new Main();

    Main() {
        systemin = new BufferedReader(new InputStreamReader(System.in));
        result = System.out;
        log = new PrintStream(new OutputStream() { public void write(int b) {} } );
    }

	public static void main(String[] args) throws IOException {

		int N = Integer.MAX_VALUE;
		//int N = readIntArray()[0];

		for(int i = 0; i < N; i++) {
			boolean b = instance.main();
			if (!b)
				break;
		}

        instance.systemin.close();
	}

	// 標準入力より1行分の区切り文字区切りでの整数値を読む
	// EOFの場合はnullを返す
	private int[] readIntArray() throws IOException {

		String s = null;
		for(;;) {
			s = systemin.readLine();
			if (s == null)
				return null;
			s = s.trim();
			if (s.length() != 0) // 突然空行を読むことがある。読み飛ばすとうまくいくらしい
				break;
		}

		String[] sp = s.split("[ ,]"); // 区切り文字はスペースかカンマ
		int[] a = new int[sp.length];
		for(int i = 0; i < sp.length; i++) {
			a[i] = Integer.parseInt(sp[i]);
		}
		return a;
	}

}
