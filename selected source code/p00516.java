import java.util.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int M = sc.nextInt();
		int kyougi[] = new int[N];
		int shinsain[] = new int[M];
		int vote[] = new int[N];
		
		for(int j = 0; j < N; j++){
			kyougi[j] = sc.nextInt();
		}
		
		for(int j = 0; j < M; j++){
			shinsain[j] = sc.nextInt();
		}
		
		
		for(int i = 0; i < M; i++){
			for(int j = 0; j < N; j++){
				if(shinsain[i] >= kyougi[j]){
					vote[j]++;
					break;
				}
			}
		
		}
		int MAX = 0;
		for(int i = 0; i < N; i++){
			if(vote[MAX] < vote[i]){
				MAX = i;
			}
		}
		System.out.println(MAX+1);
		
		
		
	}
				
}
