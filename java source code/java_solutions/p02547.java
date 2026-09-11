import java.util.*;

public class Main {
    public static void main(String[] args){
        // Your code here!
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[][] a = new int[110][5];
        
        for(int i = 0 ; sc.hasNext() ; i++){
            for(int j = 0 ; j < 2 ; j++){
                a[i][j] = sc.nextInt();
            }
        }
        
        int count = 0;
        boolean ans = false;
                
        for(int i = 0 ; i < N ; i++){
            

            if((a[i][0] == a[i][1])){
                count++;
                if(count >= 3){
                    ans = true;
                }
            }else{
                count = 0;
            }
        }
        
        if(ans){
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }
    }
}
