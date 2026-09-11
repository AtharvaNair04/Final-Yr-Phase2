import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader tec = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		for (String line; (line = tec.readLine()) != null;) {
			int n=Integer.parseInt(line.trim());
			int[]arr=new int[n];
			HashMap<Integer, Boolean>bool=new HashMap<>();
			for (int i = 0; i < n; i++){ 
				arr[i]=Integer.parseInt(tec.readLine().trim());
				bool.put(arr[i], (i&1)==1);
			}
			Arrays.sort(arr);
			int cont=0;			
			for (int i = 0; i < arr.length; i++) {
				if((i&1)==0==bool.get(arr[i])){
					cont++;
				}
			}
			out.println(cont>>1);
		}
		out.close();
	}
}
