import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st1 = new StringTokenizer(br.readLine());
		int x=Integer.parseInt(st1.nextToken());
		int y=Integer.parseInt(st1.nextToken());

		int z=0;

		if(x>y){
			z=x;
			x=y;
			y=z;
		}

		while(x!=0 || y!=0){
			System.out.println(x+" "+y);

			StringTokenizer st2 = new StringTokenizer(br.readLine());
			x=Integer.parseInt(st2.nextToken());
			y=Integer.parseInt(st2.nextToken());

			if(x>y){
				z=x;
				x=y;
				y=z;
			}
		}
	}

}
