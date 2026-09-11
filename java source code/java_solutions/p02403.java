import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st=new StringTokenizer(br.readLine());
		int H=Integer.parseInt(st.nextToken());
		int W=Integer.parseInt(st.nextToken());

		while(H!=0 || W!=0) {

			int i=1;
			int j=0;

			while(j<H) {
				while(i<W) {
					System.out.print("#");
					i++;
				}
				System.out.println("#");
				i=1;
				j++;
			}
			System.out.println("");
			StringTokenizer sa=new StringTokenizer(br.readLine());
			H=Integer.parseInt(sa.nextToken());
			W=Integer.parseInt(sa.nextToken());

		}
	}

}
