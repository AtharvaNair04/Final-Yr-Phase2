import java.awt.Point;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static int r, n;
	static int[] xl, xr, h;
	
	private static void start()
	{
		int i, j;
		
		Point[] height = new Point[41];
		for(i = 0;i < 41;i++)
			height[i] = new Point(0, 0);
		
		for(i = 0;i < n;i++)
			for(j = xl[i];j <= xr[i];j++)
			{
				if(j != xl[i]) height[j].x = Math.max(height[j].x, h[i]);
				if(j != xr[i]) height[j].y = Math.max(height[j].y, h[i]);
			}
		
		double min = Double.MAX_VALUE;
		for(i = (20-r);i <= (20+r);i++)
		{
			if(i != (20-r))
			{
				double w = Math.abs(i - 20);
				double p = Math.sqrt(r*r - w*w);
				double t = (double)height[i].x - p + (double)r;
				min = Math.min(min, t);
			}
			
			if(i != (20+r))
			{
				double w = Math.abs(i - 20);
				double p = Math.sqrt(r*r - w*w);
				double t = (double)height[i].y - p + (double)r;
				min = Math.min(min, t);
			}
		}
		
		System.out.printf("%.04f\n", min);
	}
	
	public static void main(String[] args)
	{
		
		Scanner sca = new Scanner(System.in);
		
		while(true)
		{
			r = sca.nextInt();
			n = sca.nextInt();
			if(r == 0) break;
			
			xl = new int[n];
			xr = new int[n];
			h = new int[n];
			for(int i = 0;i < n;i++)
			{
				xl[i] = sca.nextInt() + 20;
				xr[i] = sca.nextInt() + 20;
				h[i] = sca.nextInt();
			}
			
			start();
		}
	}
}
