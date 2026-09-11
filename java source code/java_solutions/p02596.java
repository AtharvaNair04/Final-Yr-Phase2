//I AM THE CREED
/* //I AM THE CREED
/* package codechef; // don't place package name! */
import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.util.StringTokenizer; 
import java.util.*;
public class Main{
    public static void main(String[] args) throws IOException 
    { 
  
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            int n=input.nextInt();
            int s=7;
            int digits=1;
            HashSet<Integer> remainders=new HashSet<>();
            while(n>s){
                s=(s*10)+7;
                digits++;
            }
            boolean possible=true;
            int prev=s%n;
            remainders.add(prev);
            while(prev!=0){
                int mod=((prev*10)+7)%n;
                digits++;
                if(mod==0){
                    break;
                }
                if(remainders.contains(mod)){
                    possible=false;
                    break;
                }
                remainders.add(mod);
                prev=mod;
            }
            if(possible){
                System.out.println(digits);
                continue;
            }
            System.out.println(-1);
            
            
        }
        
    }
    


}
