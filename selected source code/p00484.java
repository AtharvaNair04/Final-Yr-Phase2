import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		final int N = sc.nextInt();
		final int K = sc.nextInt();
		
		int[] g_count = new int[10];
		Arrays.fill(g_count, 1);
		
		int[][] data_tmp = new int[10][N+1];
		
		for(int i = 0; i < N; i++){
			final int c = sc.nextInt();
			final int g = sc.nextInt() - 1;
			
			data_tmp[g][g_count[g]++] = c;
		}
		
		int[][] data = new int[10][];
		for(int i = 0; i < 10; i++){
			data[i] = new int[g_count[i]];
			System.arraycopy(data_tmp[i], 0, data[i], 0, g_count[i]);
		}
		
		for(int i = 0; i < data.length; i++){
			Arrays.sort(data[i]);
			
			for(int start = 1; start < (data[i].length + 1) / 2; start++){
				int tmp = data[i][start];
				data[i][start] = data[i][data[i].length - start];
				data[i][data[i].length - start] = tmp;
			}
		}
		/*
		for(int g = 0; g < 10; g++){
			System.out.print(g + " :");
			for(int i : data[g]){
				System.out.printf(" %3d", i);
			}
			System.out.println();
		}
		System.out.println("--------------------------------------");
		*/
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < g_count[i]; j++){
				if(j == 0){
					continue;
				}
				
				data[i][j] += data[i][j-1];
			}
		}
		
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < g_count[i]; j++){
				data[i][j] += j * (j - 1);
			}
		}
		/*
		for(int g = 0; g < 10; g++){
			System.out.print(g + " :");
			for(int i : data[g]){
				System.out.printf(" %3d", i);
			}
			System.out.println();
		}
		System.out.println("--------------------------------------");
		*/
		int[][] DP = new int[K + 1][10];
		
		for(int g = 0; g < 10; g++){
			if(g == 0){
				final int min = Math.min(g_count[g], DP.length);
				for(int k = 0; k < min; k++){
					DP[k][g] = data[g][k];
				}
			}else{
				for(int k = 0; k < DP.length; k++){
					for(int pos = k - (g_count[g] - 1); pos <= k; pos++){
						if(pos < 0){
							continue;
						}
						
						DP[k][g] = Math.max(DP[k][g], DP[pos][g-1] + data[g][k - pos]);
					}
				}
			}
			/*
			for(int k = 0; k < DP.length; k++){
				System.out.print(k + " :");
				for(int i = 0; i < 10; i++){
					System.out.printf(" %3d", DP[k][i]);
				}
				System.out.println();
			}
			System.out.println("------------------------------------");
			*/
		}
		
		System.out.println(DP[K][9]);
		
	}

}
