import java.util.Scanner;

public class Main {

    private static final int r     =  0,
    		                 c     =  1,
    		                 EMPTY = -1;

    private static int[]   p;
    private static int[][] m;

    public static void main(String[] args) {

        //input
         Scanner sc = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));
         int N = sc.nextInt();
         p = new int[N+1];
         m = new int[N][N];
         String[] s = null;
       	 for(int i=0;i<N;i++){
       		 s = sc.next().split("\\s");
       		 p[i] = Integer.parseInt(s[r]);
       	 }
       	 p[N] = Integer.parseInt(s[c]);

         //calculation
       	 int i,j,k,l,q;
       	 for(l=1;l<N;l++){
       		 for(i=0;i<N-l;i++){
       			 j = i + l;
       			 m[i][j] = EMPTY;
       			 for(k=i;k<j;k++){
       				 q = m[i][k] + m[k+1][j] + p[i]*p[k+1]*p[j+1];
       				 if(q < m[i][j] || m[i][j] == EMPTY){
       					 m[i][j] = q;
       				 }
       			 }
       		 }
       	 }

         //output
       	 System.out.println(m[0][N-1]);
    }
}
