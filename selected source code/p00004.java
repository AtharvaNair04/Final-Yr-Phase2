import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner ab = new Scanner(System.in);
		
		while(ab.hasNext())
		{
		
		double a = ab.nextDouble();
		
		double b = ab.nextDouble();
		
		double c = ab.nextDouble();
		
		double d = ab.nextDouble();
		
		double e = ab.nextDouble();
		
		double f = ab.nextDouble();
		
		double x = (c*e - f*b)/ (a*e - b*d);
		
		double y = (c - a*x)/b;
		
		if( x == 0 || x == -0 ) x = 0;
		if( y == 0 || y == -0 ) y = 0;
		
		System.out.printf("%.3f %.3f\n",x,y);
		
		
		
		}
	}

}
