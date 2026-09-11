import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		int x=Integer.parseInt(br.readLine());
		int i=0;

		while(x!=0) {
			i++;
			System.out.println("Case "+i+": "+x);
			x=Integer.parseInt(br.readLine());
		}
	}
}
