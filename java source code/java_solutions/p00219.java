import java.util.Scanner;

class Main {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int n;
		int ice;
		int[] a;
		
		while(true){
			n = scan.nextInt();
			if(n == 0)break;
			
			a = new int[10];
			for(int i = 0; i < n; i++){
				ice = scan.nextInt();
				a[ice]++;
			}
			for(int i = 0; i < 10; i++){
				if(a[i] > 0){
					for(int j = 0; j < a[i]; j++){
						System.out.print("*");
					}
				}else System.out.print("-");
				System.out.println();
			}
		}
	}
}
