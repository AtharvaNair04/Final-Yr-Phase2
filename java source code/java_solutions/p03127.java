import java.util.Scanner;

//Scanner sc = new Scanner(System.in);

public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] A = new int[N];
//		int[][] R = new int[N][N];
		for(int i = 0; i < N; i++){
			A[i] = sc.nextInt();
		}
		
		int min = getMin(A[0], A[1]);
		if(min > 1){
			for(int i = 2; i < N; i++){
				min = getMin(min, A[i]);
				if(min == 1) break;
				
			}

		}
				
		System.out.println(min);
	}
	
	static int getMin(int a, int b){
		if(a == 0) return b;
		return getMin(b%a, a);
	}
		
		
}
