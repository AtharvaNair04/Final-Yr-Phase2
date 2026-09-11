import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		//秒単位の時間Sをh:m:sの形に直す
		Scanner sc=new Scanner(System.in);
		int S=sc.nextInt();
		int h=S/3600;//秒を時間に
		int h_amari=S%3600;//余りを出す
		int m=h_amari/60;//余りの秒を分に
		int s=h_amari%60;//余りを秒に
		System.out.println(h+":"+m+":"+s);
	}

}
