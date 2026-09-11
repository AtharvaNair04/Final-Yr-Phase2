import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;
 

public class Main{
          
    static int nowLoca=1;
     
     public static void main(String args[]){
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
         try{ String line;
             while((line=br.readLine())!=null){if(line.isEmpty())break;
               String[] spl=line.split(" ");
               Stack<Double> stk = new Stack<Double>();
               for(int i=0; i<spl.length;i++){
                   String str1=spl[i];
                   if(isNum(str1)){
                       stk.push(Double.valueOf(str1));
                   }else{
                       double n2=stk.pop(); double n1 = stk.pop(); double nRes=-1;
                       
                           if(str1.equals("+")){nRes=n1+n2;}
                           if(str1.equals("-"))nRes=n1-n2;
                           if(str1.equals("*"))nRes=n1*n2;
                           if(str1.equals("/"))nRes=n1/n2;
                       
                       stk.push(nRes);
                       
                   }
                   
               }System.out.println(stk.pop());
               
             } br.close();
            
         }catch(Exception e){e.printStackTrace();}         
     }  
     
    static boolean isNum(String n){
        try{ Double.parseDouble(n);return true;}
        catch(NumberFormatException e){return false;}
    }
     
}
