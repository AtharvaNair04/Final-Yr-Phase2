import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true){
		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = sc.nextInt();
		int d = sc.nextInt();
		int e = sc.nextInt();
		int f = sc.nextInt();
		if((a|b|c|d|e|f)==0) break;
			int sum = 100;
		
			//-3*a : ゲーム開始用のコイン
			//15*a : 777そろって手に入るコイン
			//a*5*13 : ボーナスゲーム中のブドウで, 2枚入れて15枚返ってくるやつ
			sum += -3 * a + 15 * a + a * 5 * 13;
			f -= a + 5 * a;

			//-3*b : ゲーム開始用のコイン
			//15*b : BARBARBARそろって手に入るコイン
			//b*3*13 : ボーナスゲーム中のブドウで, 2枚入れて15枚返ってくるやつ
			sum += -3 * b + 15 * b + b * 3 * 13;
			f -= b + 3 * b;

			//-3*c : ゲーム開始用のコイン
			//7*c : ブドウがそろって手に入るコイン
			sum += -3 * c + 7 * c;
			f -= c;

			//-d : ゲーム開始に3枚入れて, チェリーの場合2枚返ってくる
			sum -= d;
			f -= d;

			//e*3 : ゲーム開始時のコイン
			sum -= e * 3;
			f -= e * 2;

			//3*f : 残りゲーム回数で3枚ずつ投入してそろわなかった
			sum -= 3 * f;
			
			System.out.println(sum);
		}
		}

}
