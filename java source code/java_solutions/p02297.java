import java.math.BigDecimal;
import java.util.Scanner;

class Main {
	static double area(double[] x, double[] y){
		double area = 0;
		double crossp;
		
		for(int i = 0; i < x.length - 1; i ++){
			crossp = CrossPoint(x[i], y[i], x[i + 1], y[i + 1]);
			
			if(crossp == 200000){
				area += (y[i + 1] + y[i]) * (x[i + 1] - x[i]) / 2.0;
			}else{
				area += (crossp - x[i]) * y[i] / 2;
				area += (x[i + 1] - crossp) * y[i + 1] / 2;
			}
		}
		
		return Math.abs(area);
	}
	
	static double CrossPoint(double x0, double y0, double x1, double y1){ //notCros:return 20000
		if(y0 * y1 < 0){
			if(y1 == y0){
				return x0;
			}else{
				return x0 - y0 * (x1 - x0) / (y1 - y0);
			}
		}else{
			return 200000;
		}
	}
	
    public static void main(String[] args){
    	Scanner in = new Scanner(System.in);
    	int n = in.nextInt();
    	double[] x = new double[n + 1];
    	double[] y = new double[n + 1];
    	
    	for(int i = 0; i < n; i ++){
    		x[i] = in.nextInt();
    		y[i] = in.nextInt();
    	}

		x[n] = x[0];
		y[n] = y[0]; //??????????????£???????????????
    	
    	System.out.printf("%.1f\n", BigDecimal.valueOf(area(x, y)));
	}
}
