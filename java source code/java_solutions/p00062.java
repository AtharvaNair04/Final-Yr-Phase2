import java.util.Scanner;

class Main {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		int[] a = new int[10];
		String str;;
		
		while(scan.hasNext()){
			str = scan.next();
			char[] ch = str.toCharArray();

			for(int i = 0; i < 10; i++){
				a[i] = Integer.parseInt("" + ch[i]);

			}
			
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9 - i; j++){
					a[j] = a[j] + a[j + 1];
					if(a[j] >= 10)a[j] -= 10;
				}
			}
			System.out.println(a[0]);
		}
	}
}
