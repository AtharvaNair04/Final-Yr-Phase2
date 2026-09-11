import java.util.Scanner;

public class Main {
	Scanner sc = new Scanner(System.in);
	int s,g,a;

	public void kei(){
		for(a=0;a<10;a++){
			s = sc.nextInt();
			g += s;
		}
	}

	public void hyou(){
		System.out.println(g);
	}

	public static void main(String[] args) {
		Main a = new Main();
		a.kei();
		a.hyou();
	}
}
