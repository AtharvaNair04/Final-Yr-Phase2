import java.io.*;

public class Main{
    public static void main(String[] args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str1;
        String str2;
        
        str1 = br.readLine();
        str1 += str1;

        str2 = br.readLine();
        
        if(str1.indexOf(str2) == -1){
            System.out.println("No");
        }else{
            System.out.println("Yes");
        }
        
    }
}
