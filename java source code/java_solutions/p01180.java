import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;


public class Main{

	static class Circle {
		double r;
		double x;
		double y;

		public Circle(double r, double x, double y) {
			this.r = r;
			this.x = x;
			this.y = y;
		}

		public double getR() {
			return this.r;
		}

		public void set(double r) {
			this.r = r;
		}

		public double getX() {
			return this.x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return this.y;
		}

		public void setY(double y) {
			this.y = y;
		}

		@Override
		public String toString() {
			return this.x + ", " + this.y;
		}
	}

	static Circle[] circles = null;
	static double maxRadius = 0.0;
	public static void main(String[] args) throws Exception{
		//Scanner s = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			int N = Integer.parseInt(br.readLine());
			if(N == 0)
				break;
			circles = new Circle[N];
			for(int i=0 ; i<N ; i++) {
				String[] tmp = br.readLine().split(" ");
				circles[i] = new Circle(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]), Double.parseDouble(tmp[2]));
				maxRadius = Math.max(maxRadius, Double.parseDouble(tmp[0]));
			}

			Arrays.sort(circles, Comparator.comparing(Circle::getX));
			double res1 = solve(circles, 0, N-1);
			DecimalFormat fmt = new DecimalFormat("0.00000000");

			System.out.println(fmt.format(res1));
		}
	}

	static double solve(Circle[] c, int start, int end) {

		if(start == end || start > end)
			return Integer.MAX_VALUE;
		if(start == (end-1))
			return Math.hypot(c[start].x - c[end].x, c[start].y - c[end].y) - c[start].r - c[end].r;
		int middle = (start + end) / 2;

		double result = Math.sqrt((c[middle].x - c[middle+1].x)*(c[middle].x - c[middle+1].x) + (c[middle].y - c[middle+1].y)*(c[middle].y - c[middle+1].y)) - c[middle].r - c[middle+1].r;
		double result1 = solve(c, start, middle);
		double result2 = solve(c, middle+1, end);

		double distance =  Math.min(Math.min(result1, result2), result);
		int m = (start+end)/2;
		double middleP = (c[m-1].x + c[m].x) / 2;
		Circle[] cc = new Circle[end-start];
		int index = 0;
		for(int i=start ; i<end ;i++) {
			if(Math.abs(c[i].x - middleP) < distance+2*maxRadius) {
				cc[index++] = c[i];
			}
		}

		if(index <= 1)
			return distance;
		Circle[] cY = new Circle[index];
		for(int i=0 ; i<index ; i++) {
			cY[i] = cc[i];
		}
		Arrays.sort(cY, Comparator.comparing(Circle::getY));

		for(int j=0 ; j<index ; j++) {
			for(int k=j+1 ; k<index && (cY[k].y-cY[j].y < distance+2*maxRadius); k++) {
				distance = Math.min(distance, Math.hypot(cY[j].x - cY[k].x, cY[j].y - cY[k].y) - cY[j].r - cY[k].r);
			}
		}
		return distance;
	}

}
