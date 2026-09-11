import java.io.*;
import java.util.*;


//I have to use hashset
public class Main{
    static int count =0;
    public static void main(String[] args)throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        
        str = br.readLine();
        int n = Integer.parseInt(str);

        str=br.readLine();
        String[] line = str.split("\\s");

        int[] A = new int[n];
        for(int i=0;i<n;i++){
            A[i] = Integer.parseInt(line[i]);
        }
        String[] B = partition(A,0,n-1);
        for(int i=0;i<n;i++){
            System.out.print(B[i]);
            if(i != B.length-1){
                System.out.print(" ");
            }else{
                System.out.print("\n");
            }
        }
    }
//Errorn case 1 3 2
    public static String[] partition(int[]A,int p,int r){
        int x = A[r];
        int i= p -1;
        for(int j= p;j<r;j++){
            if(A[j] <= x){
                i++;
                int tmp = A[i];
                A[i] = A[j];
                A[j] = tmp;
            }
        }
        String[] B = new String[A.length];
        for(int j=0;j<B.length;j++){
            B[j] = String.valueOf(A[j]);
        }
        String y = "["; 
        y+=String.valueOf(A[r]);
        y+= "]";
        B[i+1] = y;
        B[r] = String.valueOf(A[i+1]);
        return B;
    }
   
     
}
