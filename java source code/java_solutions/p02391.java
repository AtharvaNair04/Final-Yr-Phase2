import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		//整数a,bを読み込んで、大小関係を示す
		Scanner sc=new Scanner(System.in);
		int a=sc.nextInt();
		int b=sc.nextInt();
		if(a>b){
			System.out.println("a > b");
		}else if(a<b){
			System.out.println("a < b");
		}else{
			System.out.println("a == b");
		}
	}

}
