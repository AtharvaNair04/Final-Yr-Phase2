//import java.util.ArrayList;
import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
//		ArrayList<String> ansBox=new ArrayList<String>();
		String c="";
		Scanner sc=new Scanner(System.in);
		while(true) {//"-"が入力されるまで繰り返す
			//文字列入力
			String str=sc.next();
			int n=str.length();
			if(str.equals("-")) {
				break;
			}

			//シャッフル回数m入力
			int m=Integer.parseInt(sc.next());

			//シャッフルする番号入力//m回シャッフルする
			for(int i=0;i<m;i++) {
				int h=Integer.parseInt(sc.next());
				//strから文字列を切り分け
				String a=str.substring(0,h);
				String b=str.substring(h,n);
				str=b+a;
			}
			System.out.println(str);
		}
	}

}
