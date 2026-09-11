import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        int N = s.nextInt();
        int[] n = new int[3];
        HashSet<String> set = new HashSet<String>(); 
        
        for(int i=0;i<N;i++){
            n[0] = s.nextInt();
            n[1] = s.nextInt();
            n[2] = s.nextInt();
            
            Arrays.sort(n);
            String s1 = String.valueOf(n[0]);
            String s2 = String.valueOf(n[1]);
            String s3 = String.valueOf(n[2]);
            
            String str = s1 + s2 + s3;
            set.add(str);
        }
        System.out.println(N - set.size());
    }
}
