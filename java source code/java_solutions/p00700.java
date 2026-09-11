import java.awt.Point;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Main {
     
    static ArrayList<Point> delta;
     
    static void start()
    {
        int x = 0;
        int y = 0;
         
        int max = 0;
        int mx = 0, my = 0;
         
        for(Point d : delta)
        {
            x += d.x;
            y += d.y;
             
            int len = x*x + y*y;
            if(max < len)
            {
                max = len;
                mx = x;
                my = y;
            }
            else if(max == len)
            {
            	if(x > mx)
            	{
            		mx = x;
            		my = y;
            	}
            }
        }
         
        System.out.printf("%d %d\n", mx, my);
    }
     
    public static void main(String[] args)
    {
         
        Scanner sca = new Scanner(System.in);
         
        while(true)
        {
            int n = sca.nextInt();
 
            for(;n > 0;n--)
            {
                delta = new ArrayList<Point>();
                 
                while(true)
                {
                    int dx = sca.nextInt();
                    int dy = sca.nextInt();
                     
                    if(dx == 0 && dy == 0) break;
                     
                    delta.add(new Point(dx, dy));
                }
                 
                start();
            }
             
            break;
        }
    }
}
