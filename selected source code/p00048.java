import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;



 class Main{
     
    
    
     public static void main(String args[]){
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
         try{
         //int n=1;
         //for(int i=0; i<n;i++){
         String line;
         while((line=br.readLine())!=null){
            double wei=Double.parseDouble(line);
             
            if(wei<=48.00)System.out.println("light fly");
            else if(wei<=51.00)System.out.println("fly");
            else if(wei<=54.00)System.out.println("bantam");
            else if(wei<=57.00)System.out.println("feather");
            else if(wei<=60.00)System.out.println("light");
            else if(wei<=64.00)System.out.println("light welter");
            else if(wei<=69.00)System.out.println("welter");
            else if(wei<=75.00)System.out.println("light middle");
            else if(wei<=81.00)System.out.println("middle");
            else if(wei<=91.00)System.out.println("light heavy");
            else System.out.println("heavy");
         }
         }catch(Exception e){}
         
     }
     
 }
