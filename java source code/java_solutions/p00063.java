import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
 

public class Main{
          
     
     public static void main(String args[]){
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
         try{
             String line;
             int count=0;
            while((line=br.readLine())!= null){if(line.isEmpty())break;
                char[] chars = line.toCharArray();
                char[] charsR=new char[chars.length];
                for(int i=0; i<chars.length;i++){
                    charsR[chars.length-1-i]=chars[i];
                }
                String r = String.valueOf(charsR);//System.out.println(r);
                if(r.equals(line)){count++;}
                
            }             br.close();
             System.out.println(count);
         }catch(Exception e){e.printStackTrace();}         
     }
     
}
