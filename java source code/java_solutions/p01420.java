import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		final int N = sc.nextInt();
		final int M = sc.nextInt();
		final int L = sc.nextInt();
		
		long[][] combs = new long[M + 1][M + 1];
		combs[0][0] = 1;
		for(int i = 0; i < M; i++){
			for(int j = 0; j < M; j++){
				if(combs[i][j] == 0){
					continue;
				}
				
				combs[i + 1][j    ] += combs[i][j];
				combs[i + 1][j + 1] += combs[i][j];
			}
		}
		
		double[][] probs = new double[N][M + 1];
		double[][] probs_sums = new double[N][M + 1];
		double[][] times = new double[N][M + 1];
		
		
		for(int i = 0; i < N; i++){
			final double P = sc.nextDouble() / 100;
			final int T = sc.nextInt();
			final int V = sc.nextInt();
			
			//init_time
			double time = (double)(L) / V;
			
			for(int j = 0; j <= M; j++){
				times[i][j] = time + j * T;
			}
			
			//init_prob
			for(int j = 0; j <= M; j++){
				probs[i][j] = combs[M][j];
			}
			
			double rest_p = 1.0;
			for(int j = 0; j <= M; j++){
				probs[i][j] *= rest_p;
				rest_p *= P;
			}
			
			double unrest_p = 1.0;
			for(int j = 0; j <= M; j++){
				probs[i][M - j] *= unrest_p;
				unrest_p *= 1 - P;
			}
			
			//sum
			for(int j = 0; j <= M; j++){
				probs_sums[i][j] = (j == 0 ? 0 : probs_sums[i][j-1]) + probs[i][j];
			}
		}
		
		for(int fst = 0; fst < N; fst++){
			double fst_prob = 0;
			
			for(int rest = 0; rest <= M; rest++){
				final double rested_time = times[fst][rest];
				double to_fst_prob = probs[fst][rest];
				
				for(int other = 0; other < N; other++){
					if(fst == other){
						continue;
					}
					
					int pos = Arrays.binarySearch(times[other], rested_time);
					if(pos < 0){
						pos = -(pos + 2);
					}
					
					double win_prob = 0;
					if(pos < 0){
						win_prob = 1;
					}else{
						win_prob = 1 - probs_sums[other][pos];
						win_prob = Math.max(win_prob, 0);
					}
					
					to_fst_prob *= win_prob;
				}
				
				fst_prob += to_fst_prob;
			}
			
			System.out.printf("%.8f\n", fst_prob);
		}
	}

}
