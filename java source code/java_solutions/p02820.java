import java.util.Scanner;
public class Main{

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		int n = stdIn.nextInt(); //じゃんけんをする回数
		int k = stdIn.nextInt(); //k回目までのじゃんけんは好きな手を出せる
		int r = stdIn.nextInt(); //グーで勝った時の得点
		int s = stdIn.nextInt(); //パーで勝った時の得点
		int p = stdIn.nextInt(); //チョキで勝った時の得点

		String t = stdIn.next(); //筐体の出す手の文字列
		int ans = 0;
		String[] hand = new String[n];

		for(int i = 0; i < k; i++) {
			if(t.charAt(i)== 'r') {
				ans += p;
				hand[i] = "p";
			}else if(t.charAt(i)=='s') {
				ans += r;
				hand[i] = "r";
			}else {
				ans += s;
				hand[i] = "s";
			}

		}

		for(int i = k; i < n; i++) {

			if(t.charAt(i)== 'r') {
				if(!hand[i-k].equals("p")) {
					ans += p;
					hand[i] = "p";
				}else if(i < t.length()-k) {
					if(t.charAt(i+k) == 's') {
						hand[i] = "s";
					}else {
						hand[i] = "r";
					}
				}
			}
			if(t.charAt(i)== 's') {
				if(!hand[i-k].equals("r")) {
					ans += r;
					hand[i] = "r";
				}else if(i < t.length()-k){
					if(t.charAt(i+k)== 'p') {
						hand[i] = "p";
					}else {
						hand[i] ="s";
					}
				}
			}
			if(t.charAt(i)== 'p') {
				if(!hand[i-k].equals("s")) {
					ans += s;
					hand[i] = "s";
				}else if(i < t.length()-k){
					if(t.charAt(i+k)== 'r') {
						hand[i] = "r";
					}else {
						hand[i] = "p";
					}
				}
			}
		}



			System.out.println(ans);

	}
}
