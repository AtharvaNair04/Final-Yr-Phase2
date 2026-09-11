import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String S = br.readLine();
		String T = br.readLine();
		int l = S.length();
		int cont = 0;
		for (int i = 0; i < l; i++) {
			char s = S.charAt(i);
			char t = T.charAt(i);
			if (s != t) {
				cont++;
			}
		}
		System.out.print(cont);
	}
}
