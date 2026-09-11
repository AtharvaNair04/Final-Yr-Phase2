import java.util.Arrays;
import java.util.Scanner;

public class Main {
	
	public static int hand(int con, char[] array, int aces){
		int sum = 0;
		
		int ace_count = 0;
		for(int i = 0; i < con; i++){
			if(array[i] == 'T' || array[i] == 'J' || array[i] == 'Q' || array[i] == 'K'){
				sum += 10;
			}else if(array[i] == 'A'){
				sum += (ace_count < aces ? 11 : 1);
				ace_count++;
			}else if('0' <= array[i] && array[i] <= '9'){
				sum += array[i] - '0';
			}
		}
		
		return sum;
	}
	
	public static boolean contain_ace(int con, char[] array){
		for(int i = 0; i < con; i++){
			if(array[i] == 'A'){
				return true;
			}
		}
		
		return false;
	}
	
	public static int score(int con, char[] array, int aces){
		int max = -1;
		for(int i = 0; i <= aces; i++){
			int cur = hand(con, array, i);
			if(cur <= 21 && max < cur){
				max = cur;
			}
		}
		
		return max;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		final int N = sc.nextInt();
		
		for(int tt = 0; tt < N; tt++){
			char[] hands = new char[10];
			
			int ace_count = 0;
			for(int i = 0; i < 10; i++){
				hands[i] = sc.next().charAt(0);
				if(hands[i] == 'A'){
					ace_count++;
				}
			}
			
			//System.out.println(Arrays.toString(hands));
			
			if(hand(2, hands, 1) == 21){
				System.out.println("blackjack");
				continue;
			}
			
			int cons = 2;
			while(true){
				if(cons > 10){
					cons = 10;
					break;
				}
				
				if(ace_count >= 1 && contain_ace(cons, hands) && hand(cons, hands, 1) == 17){
					cons++;
				}else{
					final int sco = score(cons, hands, ace_count);
					//System.out.println("debug :: " + sco);
					if(sco == -1){
						break;
					}else if(sco <= 16){
						cons++;
					}else{
						break;
					}
				}
			}
			
			final int sco = score(cons, hands, ace_count);
			if(sco == -1){
				System.out.println("bust");
			}else{
				System.out.println(sco);
			}
		}
	}

}
