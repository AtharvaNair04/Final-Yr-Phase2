import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		//長方形の面積と周の長さ
		Scanner sc=new Scanner(System.in);
		int vertical=sc.nextInt();
		int cross=sc.nextInt();
		int menseki=vertical*cross;
		int gaisyu=(vertical+cross)*2;
		System.out.println(menseki+" "+gaisyu);
	}

}
