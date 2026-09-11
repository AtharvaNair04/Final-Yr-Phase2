import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int N = s.nextInt();
		int[] I = new int [101];
		for(int i =0;i<N;i++){
			int n = s.nextInt();
			I[n] += 1;
		}
		for(int j=100;j>0;j--){
			if(I[j]>=j){
				System.out.println(j);
				break;
			}else{
				I[j-1] += I[j];
			}
		}
		
	}
}
