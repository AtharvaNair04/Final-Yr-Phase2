import java.util.Scanner;


public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			final int N = sc.nextInt();
			final int K = sc.nextInt();
			
			if(N == 0 && K == 0){
				break;
			}
			
			int[] drinks = new int[K];
			for(int i = 0; i < K; i++){
				drinks[i] = sc.nextInt();
			}
			
			boolean flag = true;
			for(int i = 0; i < N; i++){
				for(int j = 0; j < K; j++){
					drinks[j] -= sc.nextInt();
					if(drinks[j] < 0){
						flag = false;
					}
				}
			}
			
			System.out.println(flag ? "Yes" : "No");
		}

	}

}
