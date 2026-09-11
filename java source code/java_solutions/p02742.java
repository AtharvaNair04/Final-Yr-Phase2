import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);

		long h = stdIn.nextLong();
		long w = stdIn.nextLong();
		
		long cnt = 0;
		if(h==1 || w == 1) {
			System.out.println(1);
			return;
		}
		
		if(h % 2 == 0 && w % 2 == 0) { //h偶数、w偶数
			for(int i = 0; i < h; i++) {
				cnt += w/2;
			}
		}else if(h % 2 == 0 && w % 2 != 0) { //h偶数、w奇数
			for(int i = 0; i < h ; i++) {
				if(i % 2 == 0) {
					cnt += (w+1)/2;
				}else {
					cnt += w/2;
				}
			}
		}else if( h % 2 != 0 && w % 2 == 0) { // h奇数,w偶数
			for(int i = 0; i < h; i++) {
				cnt += w/2;
			}
		}else {                                //h奇数,w奇数
			for(int i = 0; i < h ; i++) {
				if(i % 2 == 0) {
					cnt += (w+1)/2;
				}else {
					cnt += w/2;
				}
			}
		}

		System.out.println(cnt);

	}

}
