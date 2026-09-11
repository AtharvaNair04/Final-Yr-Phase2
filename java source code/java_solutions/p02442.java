import java.util.Scanner;
public class Main{
    public static int compare(int A[], int B[]){
        int i = 0;
        while(i < A.length && i < B.length){
            if(A[i] == B[i]);
            else if(A[i] > B[i])
                return 0;
            else return 1;
            i++;
        }
        if(A.length == B.length || i == B.length)
            return 0;
        else
            return 1;
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int A[] = new int[n];
        for(int i = 0 ; i < n ; i++)
            A[i] = sc.nextInt();
        int m = sc.nextInt();
        int B[] = new int[m];
        for(int i = 0 ; i < m ; i++)
            B[i] = sc.nextInt();
        System.out.println(compare(A,B));
    }
}
