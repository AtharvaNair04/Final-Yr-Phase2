import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner sc = new Scanner(System.in);
		String S = sc.next();

		char[] ch = S.toCharArray();

		int a=0;
		int b=0;
		int c=0;
		for (char cs : ch){
			if(cs=='a') a++;
			if(cs=='b') b++;
			if(cs=='c') c++;
		}



		if(a==b && b==c){
			System.out.println("YES");
			System.exit(0);
		}

		if(a==b+1 && a==c+1){
			System.out.println("YES");
			System.exit(0);
		}

		if(b==c+1 && b==a+1){
			System.out.println("YES");
			System.exit(0);
		}
		if(c==a+1 && c==b+1){
			System.out.println("YES");
			System.exit(0);
		}

		if(a==b-1 && a==c-1){
			System.out.println("YES");
			System.exit(0);
		}

		if(b==c-1 && b==a-1){
			System.out.println("YES");
			System.exit(0);
		}
		if(c==a-1 && c==b-1){
			System.out.println("YES");
			System.exit(0);
		}



		System.out.println("NO");
	}

}
