import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main{
	public static void main(String args[]) {

		BufferedReader bfr=new BufferedReader(new InputStreamReader(System.in));
		String str="";
		int[] syugo;
		StringTokenizer stk;

		int mae_y_s,mae_y_e=0;
		syugo=new int[1001];

		for(int i=0; i<1001; i++) {
			syugo[i]=0;
		}

		try {
			str=bfr.readLine();
			if(str=="") {
				System.exit(0);
			}
			stk=new StringTokenizer(str," ");
			int yoyaku_s,yoyaku_e=0;
			yoyaku_s=Integer.parseInt(stk.nextToken());
			yoyaku_e=Integer.parseInt(stk.nextToken());

			str=bfr.readLine();
			int yoyaku=Integer.parseInt(str);

			for(int i=0; i<yoyaku; i++) {
				str=bfr.readLine();
				if(str=="") {
					System.exit(0);
				}
				stk=new StringTokenizer(str," ");
				mae_y_s=Integer.parseInt(stk.nextToken());
				mae_y_e=Integer.parseInt(stk.nextToken());

				for(int j=mae_y_s; j<=mae_y_e-1; j++) {
					syugo[j]=1;
				}
			}

			for(int a=yoyaku_s; a<=yoyaku_e-1; a++) {
				if(syugo[a]==0) {
					// nanimo sinai
				}
				else {
					System.out.println("1");
					System.exit(0);
				}
			}
			System.out.println("0");

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
