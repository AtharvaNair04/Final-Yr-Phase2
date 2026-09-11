import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class Main {

    public static  boolean canIn(int n, int a, int b, int c){
//return 1>2;        
return  ( 2*n > Math.sqrt(a*a+b*b) ) || (2*n > Math.sqrt(c*c+b*b)) || (2*n> Math.sqrt(a*a+c*c)) ;
    }
  
    public static void main(String[] args)  {

    	try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line=br.readLine())!=null){if(line.isEmpty())break;
                    String[] spl = line.split(" ");
                    int a = Integer.parseInt(spl[0]); 
                    int b = Integer.parseInt(spl[1]); 
                    int c = Integer.parseInt(spl[2]);
                    if(a==0&&b==0&&c==0){break;}
                    line = br.readLine();
                    int kazu = Integer.parseInt(line);
                    for(int i =0 ; i<kazu ; i++){
                        line = br.readLine();
                        int n= Integer.parseInt(line);
                        boolean flag = canIn(n,a,b,c);
                        if(flag)System.out.println("OK");
                        else System.out.println("NA");
                    }
                    
                    
                    
                    
                    
            }//End WHILE

        }catch(Exception e){e.printStackTrace();}

    }


}
