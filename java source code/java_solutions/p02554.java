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
            long n=input.nextLong();
            long len=n;
            long mod=1000000007;
            long ans=0;
            long tot=1;
            int ct=0;
            while(ct!=n){
                tot=(tot*10)%mod;
                ct++;
            }
            ct=0;
            long no_zero=1;
            while(ct!=n){
                no_zero=(no_zero*9)%mod;
                ct++;
            }
            ct=0;
            long none=1;
            while(ct!=n){
                none=(none*8)%mod;
                ct++;
            }
            System.out.println((tot-no_zero-no_zero+none+2*mod)%mod);
        }
    }
 
}
