import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		char[] s = scan.next().toCharArray();
		char[] a = scan.next().toCharArray();
		
		for(int i = 0; i < s.length - a.length + 1; i++){
			for(int j = 0; j < a.length; j++){
				if(a[j] == s[i + j]){
					if(j == a.length - 1){
						System.out.println(i);
					}
				}else break;
			}
		}
		
	}
}
