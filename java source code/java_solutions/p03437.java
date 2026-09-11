import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner stdin = new Scanner(System.in);

		String line = stdin.nextLine();
		stdin.close();
		String[] input = line.split(" ");
		int num = Integer.parseInt(input[0]);
		int not = Integer.parseInt(input[1]);

		if(num == not || (num % 2 == 0 && not  == 2) || not == 1 || not == 0 || num % not == 0){
			System.out.println(-1);
			return;
		}else if(num < not){
			System.out.println(num);
			return;
		}
		for(long i = 0; i < 1000000000000000000l; i += num){
			if(i % not != 0){
				System.out.println(i);
				return;
			}
		}
		System.out.println(-1);
	}

}
