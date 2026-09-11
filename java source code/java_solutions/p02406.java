import java.util.Scanner;
public class Main{
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int x,i=1;
		while(n >= i){
			x = i;
			if(x%3==0){
				System.out.printf(" %d",i);
			}
			else if(x%10==3){
				System.out.printf(" %d",i);
			}
			else{
				while(x != 0){
					x = x/10;
					if(x%10==3){
					System.out.printf(" %d",i);
					break;
					}
				}
			}
			i++;
		}
		System.out.printf("\n");
	}
}
