import java.util.Scanner;


public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String S = sc.next();
        
        int Blength = S.length();
        boolean[] B = new boolean[Blength];
        
        for(int i=0;i<Blength;i++){
            if(S.charAt(i) == '0'){
                B[i] = true;
            }else{
                B[i] = false;
            }
        }
        
        int i = (Blength/2) -1;
        int j = Blength/2;
        
        int ans = Blength;
        
        while(i > -1 || j+1 < Blength){
            if(i > -1 && B[i]^B[i+1]){
                ans = Blength-1-i;
                break;
            }
            if(j+1 < Blength && B[j]^B[j+1]){
                ans = j+1;
                break;
            }
            i--;
            j++;
        }
        System.out.println(ans);
    }  
}
