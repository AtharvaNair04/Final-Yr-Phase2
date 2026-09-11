import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while(true){
			final int N = sc.nextInt();
			
			if(N == 0){
				break;
			}
			
			System.gc();
			
			int[] own = new int[N];
			//int[] ene = new int[N];
			ArrayList<Integer> ene = new ArrayList<Integer>();
			
			for(int i = 0; i < N; i++){
				own[i] = -sc.nextInt();
			}
			Arrays.sort(own);
			for(int i = 0; i < N; i++){
				own[i] = -own[i];
			}
			
			
			for(int i = 0; i < N; i++){
				ene.add(sc.nextInt());
			}
			Collections.sort(ene);
			/*
			for(int i = 0; i < N; i++){
				ene[i] = -ene[i];
			}
			*/
			/*
			int[] sum = new int[N];
			for(int i = 0; i < N; i++){
				sum[i] = own[i] > ene[i] ? 1 : 0;
			}
			for(int i = 1; i < N; i++){
				sum[i] += sum[i - 1];
			}
			*/
			
			//System.out.println(Arrays.toString(sum));
			
			
			boolean flag = false;
			int win_count = 0;
			for(int K = 1; K < N; K++){
				final int must = K / 2 + 1;
				final int own_num = own[K - 1];
				
				int pos = Collections.binarySearch(ene, own_num);
				
				if(pos < 0){
					pos = -(pos + 1);
				}
				
				if(pos >= ene.size()){
					win_count++;
				}else if(own_num > ene.get(pos)){
					win_count++;
				}
				
				if(win_count >= must){
					flag = true;
					System.out.println(K);
					break;
				}
				
				if(ene.size() > pos){
					ene.remove(pos);
				}else{
					ene.remove(0);
				}
			}
			
			if(!flag){
				System.out.println("NA");
			}
			
		}
		
		sc.close();
	}
}
