import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		/*<考察>
		 * (1,1)→(1,2)へ進むには奇数回必要(奇数回であれば絶対到達可能）
		 * ※(1,1)→(1,6)は5回以上の奇数回であれば到達可能であることを意味する
		 * 
		 * (1,1)→(1,1)に戻ったり(1,1)→(1,3)に行くには偶数回必要
		*/
		
		int n = stdIn.nextInt();
		
		int[] t = new int[n+1];
		int[] x = new int[n+1];
		int[] y = new int[n+1];
		
		t[0] = 0;
		x[0] = 0;
		y[0] = 0;
		
		for(int i = 1; i < n+1; i++) {
			 t[i] = stdIn.nextInt();
			 x[i] = stdIn.nextInt();
			 y[i] = stdIn.nextInt();
			
			
			int time = t[i] - t[i-1];
			int X = Math.abs(x[i] - x[i-1]);
			int Y = Math.abs(y[i] - y[i-1]);
			
			if(X+Y > time) { //到達不能条件
				System.out.println("No");
				return;
			}else if((X+Y) % 2 == 0) {
				if(time % 2 != 0) {
					System.out.println("No");
					return;
				}
				
				
			}else if((X+Y) % 2 != 0) {
				if(time% 2 == 0) {
					System.out.println("No");
					return;
				}
			}else {
				continue;
			}
		
			
		}
		
		System.out.println("Yes");
		
		
		
	}

}
