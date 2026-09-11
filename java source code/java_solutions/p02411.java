import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int s=0;
		ArrayList<String> list=new ArrayList<String>();
		while(s!=-3) {
			//値の入力
			StringTokenizer st=new StringTokenizer(br.readLine());
			int m=Integer.parseInt(st.nextToken());
			int f=Integer.parseInt(st.nextToken());
			int r=Integer.parseInt(st.nextToken());
			s=m+f+r;
			int mf=m+f;

			if(s==-3) {
				break;
			}

			//式の評価
			String eva="";
			if(m==-1 || f==-1) {
				eva="F";
			}else if(mf>=80) {
				eva="A";
			}else if(65<=mf && mf<80) {
				eva="B";
			}else if(50<=mf && mf<65 || r>=50) {
				eva="C";
			}else if(30<=mf && mf<50) {
				eva="D";
			}else {
				eva="F";
			}

			//配列への追加
			list.add(eva);
		}
		//評価の出力
		for(String lists:list) {
			System.out.println(lists);
		}
	}

}
