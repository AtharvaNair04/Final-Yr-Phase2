import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
 public static void main(String[] args)throws IOException {
  BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
     
     while(true){
         String str=br.readLine();
      if(str.equals("0"))break;
         int t=0;
         for(int i=0;i<str.length();i++){
           t+=Character.getNumericValue(str.charAt(i));
         }System.out.println(t);
     }
 }
}
