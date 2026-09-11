import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while(true){
			final int q1 = sc.nextInt();
			
			if(q1 == 0){
				break;
			}
			
			final int b  = sc.nextInt();
			final int c1 = sc.nextInt();
			final int c2 = sc.nextInt();
			final int q2 = sc.nextInt();
			
			boolean solved = false;
			int max_buy = 0;
			
			for(int buy = 1; buy <= q2; buy++){
				final int rest_money = b - buy * c1;
				
				if(rest_money < 0){
					break;
				}
				
				final int buy_normal = rest_money / c2;
				
				if(buy_normal + buy < q1){
					continue;
				}
				
				solved = true;
				max_buy = buy;
			}
			
			if(solved){
				System.out.println(max_buy + " " + ((b - max_buy * c1) / c2));
			}else{
				System.out.println("NA");
			}
		}
	}
}
