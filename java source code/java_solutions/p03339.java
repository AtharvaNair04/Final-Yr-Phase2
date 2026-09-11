import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        char[] c = sc.next().toCharArray();
        int min = 300001;
        int w[] = new int[n];
        int e[] = new int[n];
        if(c[0]=='W'){
            w[0] = 1;
        }
        if(c[n-1]=='E'){
            e[n-1]=1;
        }

            for(int i =1; i<n; i++){
                if(c[i]=='W') {
                    w[i] += 1;
                }
                w[i] += w[i-1];
            }
            for(int i=n-2; i>=0; i--){
                if(c[i]=='E') {
                    e[i] += 1;
                }
                e[i] += e[i+1];
            }
            if(c[0]=='E'){
                e[0]++;
            }
            if(c[n-1]=='w'){
                w[n-1]++;
            }
            min = Math.min(w[n-1],e[0]);
            for(int i=1; i<n-1; i++){
                min = Math.min(w[i-1]+e[i+1],min);
            }
            System.out.println(min);
    }
}
