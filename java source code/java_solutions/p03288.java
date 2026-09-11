import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		// 입력
		int r = Integer.parseInt(br.readLine());

		if(r < 1200){
			bw.write("ABC");
		}else if(r < 2800){
			bw.write("ARC");		
		}else{
			bw.write("AGC");
		}
		
		bw.flush();
		bw.close();
		br.close();
	}
}
