//I AM THE CREED
/* //I AM THE CREED
/* package codechef; // don't place package name! */
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.util.StringTokenizer; 
import java.util.*;
import java.awt.Point;
public class Main{
    public static void main(String[] args) throws IOException 
    { 
  
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            long x=input.nextLong();
            long k=input.nextLong();
            long d=input.nextLong();
            x=Math.abs(x);
            if(k<=x/d){
                System.out.println(x-k*d);
                continue;
            }
            long min=Long.MAX_VALUE;
            if((k-(x/d))%2==0){
                min=x%d;
            }
            else{
                min=d-(x%d);
            }
            System.out.println(min);
        }
        
    }
    


}
