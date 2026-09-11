import java.io.*;
import java.util.*;


public class Main {
 
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));    
    private static StringTokenizer st;
 
    public static void main ( String [] args ) throws IOException
    {
    new Main().cal();
    }
 
  
    private void cal() throws IOException 
    {
    int N = Integer.parseInt(br.readLine());
    int [] n = new int[N];
    st = new StringTokenizer(br.readLine());
 
    for(int i = 0; i < N; i++)
        n[i] = Integer.parseInt(st.nextToken());
 
    printArray(n);
    for(int j = 1; j < n.length; j++) {
        int key = n[j];
        int i = j-1;
        while( i >= 0 && n[i] > key ) {
        n[i+1] = n[i];
        i--;
        }
        n[i+1] = key;
 
        printArray(n);
    }
    }
     
    private void printArray(int [] A) {
    for(int i = 0; i < A.length; i++) {
        if ( i > 0 ) System.out.print(" ");
        System.out.print(A[i]);
    }
    System.out.println("");
    }
 
}
