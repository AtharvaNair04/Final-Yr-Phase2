import java.util.Scanner;
public class Main{
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		int i;
		int a;
		int b;
		int c;
		int d;
		while(true){
			i = 0;
			int n = sc.nextInt();
			int x = sc.nextInt();
			if(n == 0 && x == 0){
				break;
			}
			for(a=1;a<n+1;a++){
				for(b=1;b<n+1;b++){
					for(c=1;c<n+1;c++){
						if(x == a+b+c){
							i++;
							if(a == b||b == c||c == a){
								i--;
							}
							else if(a > b||b > c){
								i--;
							}
						}
					}
				}
			}
			System.out.printf("%d\n",i);
		}
	}
}
