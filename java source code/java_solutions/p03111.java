import java.util.*;
import static java.lang.Math.*;

public class Main{

    static int A,B,C,n;
    static int l[];
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        A = sc.nextInt();
        B = sc.nextInt();
        C = sc.nextInt();
        l = new int[n];
        for(int i=0; i<n; i++){
            l[i] = sc.nextInt();
        }
        System.out.println(dfs(0,0,0,0));
    }

    static int dfs(int i, int a, int b, int c){
        if(i==n){
            return (min(min(a,b),c) > 0) ? abs(A-a) + abs(B-b) + abs(C-c) -30 : 9999999;
        }

        int mp0 = dfs(i+1, a, b, c);
        int mp1 = dfs(i+1, a+l[i], b, c) + 10;
        int mp2 = dfs(i+1, a, b+l[i], c) + 10;
        int mp3 = dfs(i+1, a, b, c+l[i]) + 10;

        return min(min(mp0,mp1),min(mp2,mp3));
    }
}
