import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.math.*;
 
public class Main {
	public static void main(String[] args)throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		String[] str = br.readLine().split(" ");
		int n = Integer.parseInt(str[0]);
		int a = Integer.parseInt(str[1]);
		int b = Integer.parseInt(str[2]);
		String s = br.readLine();
		int cnt = 0;
		int foreign = 0;
		for(int i = 0; i < n; i++){
			char c = s.charAt(i);
			if(c=='c'){
				sb.append("No").append("\n");
				continue;
			}
			if(c=='a'){
				if(a+b>cnt){
					cnt++;
					sb.append("Yes").append("\n");
					continue;
				}else{
					sb.append("No").append("\n");
					continue;
				}
			}
			if(c=='b'){
				foreign++;
				if(a+b>cnt && foreign<=b){
					cnt++;
					sb.append("Yes").append("\n");
					continue;
				}else{
					sb.append("No").append("\n");
					continue;
				}
			}
		}
		System.out.print(sb);
	}
}
