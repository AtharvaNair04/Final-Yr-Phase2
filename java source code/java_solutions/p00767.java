import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws IOException {

		ArrayList<String> datas = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		ArrayList<String> result = new ArrayList<String>();

		while (true) {
			String s1 = new String(in.readLine());
			int count = 0;

			if (s1.equals("0 0"))
				break;

			String[] data = s1.split(" ");

			int d1 = Integer.parseInt(data[0]);
			int d2 = Integer.parseInt(data[1]);

			int r1 = 0;
			int r2 = 0;

			int target = d1 * d1 + d2 * d2;
			int min = 150 * 150 * 2;
			
			for (int i = 1; i <= 150; i++) {
				for (int j = i+1; j <= 150; j++) {
					int res = i * i + j * j;
					
					if(res == target && i<= d1) break;
					
					if (target <= res && (d1 != i || d2 != j)) {
						
						if (res < min) {
							min = res;
							r1 = i;
							r2 = j;
						}else{
							break;
						}
					}
				}
			}
			
			result.add(String.valueOf(r1)+" "+String.valueOf(r2));
		}

		for (String s : result) {
			System.out.println(s);
		}
	}
}
