import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		//キーボード入力
		int n=Integer.parseInt(br.readLine());

		//配列宣言
		int[] ary=new int[n];

		int count=0;

		StringTokenizer st=new StringTokenizer(br.readLine());
		while(n>count){
			int a=Integer.parseInt(st.nextToken());
			ary[count]=a;
			count++;
		}
		while(count>0){
			count--;
			if(count==0){
				System.out.println(ary[count]);
			}else{
				System.out.print(ary[count]);
			}
			if(count>0){
				System.out.print(" ");
			}
		}
	}

}
