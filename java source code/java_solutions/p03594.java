import java.util.*;
import java.io.PrintWriter;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		
		int H = sc.nextInt();
		int W = sc.nextInt();
		int d = sc.nextInt();
		char[] c = "RYGB".toCharArray();
		
		for(int i=0; i<H; i++) {
			for(int j=0; j<W; j++) {
				pw.print(d%2==0 ? c[( (i+j)/2/(d/2) + (i-j+W)/2/(d/2)*2)%4]: c[(i+j)%4]);
			}
			pw.println();
		}

		sc.close();
		pw.close();
	}
}
