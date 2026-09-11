import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
	private	static	BufferedReader	br = null;
	private	static	int				IX = 0;
	private	static	int				IY = 1;
	private	static	int				IZ = 2;
	private	static	double			ZR = 1e-6;

	static {
		br = new BufferedReader(new InputStreamReader(System.in));
	}
 
    /**
     * @param args
     */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(solve(parsePos())?"HIT":"MISS");
	}

	private static boolean solve(double[][] pos) {
		double[]	nv = getNormalVec(pos[2], pos[3], pos[4]);
		double[]	pv = new double[3];
		double		da = dot(sub(pos[2],pos[0]), nv);
		double		de = dot(sub(pos[1],pos[0]), nv);
		double		rt = da / de;

		// バリアと並行の向きに敵がいる
		if (Math.abs(de) < ZR) {
			return true;
		}

		// バリアのある平面とは逆の向きの敵がいる
		if (rt < -ZR) {
			return true;
		}

		// バリアのある平面より手前に敵がいる
		if (rt-ZR > 1.0) {
			return true;
		}

		// バリアのある平面との交点を求める
		for (int i = IX; i <= IZ; i++) {
			pv[i] = pos[0][i]*(1.0-rt)+pos[1][i]*rt;
		}

		// 交点がバリアの中にあるかチェックする
		{
			double[]	c1 = cross(sub(pos[3], pos[2]), sub(pv, pos[2]));
			double[]	c2 = cross(sub(pos[4], pos[3]), sub(pv, pos[3]));
			double[]	c3 = cross(sub(pos[2], pos[4]), sub(pv, pos[4]));
			double		d1 = dot(c1, c2);
			double		d2 = dot(c1, c3);

			if (d1 < -ZR || d2 < -ZR) {
				return true;
			}
		}

		return false;
	}

	private static double[] getNormalVec(double[] a, double[] b, double[] c) {
		double[]	ab = sub(b, a);
		double[]	ac = sub(c, a);
		double[]	n  = cross(ab, ac);

		return n;
	}
	
	private static double[] sub(double[] v1, double[] v2) {
		double[]	v3 = { v1[IX]-v2[IX], v1[IY]-v2[IY], v1[IZ]-v2[IZ] };
		return v3;
	}

	private static double[] cross(double[] v1, double[] v2) {
		double[]	v3 = { v1[IY]*v2[IZ]-v1[IZ]*v2[IY], v1[IZ]*v2[IX]-v1[IX]*v2[IZ], v1[IX]*v2[IY]-v1[IY]*v2[IX] };
		return v3;
	}

	private static double dot(double[] v1, double[] v2) {
		return v1[IX]*v2[IX]+v1[IY]*v2[IY]+v1[IZ]*v2[IZ];
	}

	private static double[][] parsePos() {
		double[][]	pos = new double[5][3];

		for (int i = 0; i < 5; i++) {
			String		str = parseStdin();
			String[]	lns = str.split(" ");

			for (int j = IX; j <= IZ; j++) {
				pos[i][j] = Double.parseDouble(lns[j]);
			}
		}

		return pos;
	}

	private static String parseStdin() {
        String  stdin = null;
        
        try {
        	String  tmp = br.readLine();
        	if (tmp != null) {
            	if (!tmp.isEmpty()) {
            		stdin = tmp;
            	}
        	}
        }
        catch (IOException e) {}
 
        return stdin;
	}
}
