import java.util.Scanner;
 
public class Main {
	public static void main(String[]args) {
		try(Scanner scan = new Scanner(System.in)){
			
			int N = scan.nextInt();
			long[]A = new long[N];
			for(int i = 0;i<N;i++)A[i] = scan.nextInt();
			
			long [] D = new long[N];
			
			
			for(int i = 0;i<N;i++) {
				if(i%2==0) {
					D[0]+=A[i];
				}else {
					D[0]+=-A[i];
				}
			}
			
			D[N-1] = 2*A[N-1]-D[0];
			
			for(int i = N-2;i>=1;i--) {
				D[i] = 2*A[i]-D[i+1];
			}
			
			for(int i = 0;i<N;i++) {
				System.out.println(D[i]);
			}
			
			
		}
		
		
	}
		
 
}
