import java.util.Arrays;
import java.util.Scanner;
 
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		////////////////////////////////////
		
		int n = sc.nextInt();
		int k = sc.nextInt();
		int[] alphabet = new int[26];
		int[] memory = new int[k];
		String s;
		char c;
		int m;
		int x;
		int y;
		int cnt = 0;
		boolean flag = true;
		
		for (int i=0; i<n; i++) {
			s = sc.next();
			c = s.charAt(0);
			m = (int) (c - 'A');
			alphabet[m]++;
		}
		
		
		while (flag) {
			Arrays.sort(alphabet);
			x=0;
			y=0;
			flag = false;
			for (int i=25; i>=0; i--) {
				if (alphabet[i]>0) {
					memory[y++]=i;
					x++;
					if (x==k) {
						flag = true;
						cnt++;
						for (int mem: memory) {
							alphabet[mem]--;
						}
						break;
					}
				}
			}
		}
		
		System.out.println(cnt);
		
		////////////////////////////////////
		sc.close();
	}
}
