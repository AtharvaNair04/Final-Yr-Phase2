import java.util.Scanner;

public class Main{

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		char[] S = sc.next().toCharArray();
		int a = -1;
		int b = -1;
		for(int i = 0; i<S.length;i++){
			if(S[i] == 'C'){
				a = i;
			}
			if(S[i] == 'F'){
				b = i;
			}
		}
		if(a<b && a != -1 && b != -1){
			System.out.println("Yes");
		}else if(a==-1 || b==-1 || b<a){
			System.out.println("No");
		}
	}
}
