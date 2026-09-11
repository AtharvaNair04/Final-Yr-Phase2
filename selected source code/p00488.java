import java.util.*;
public class Main {
	public static void main(String[] args) {
		int pasta[] = new int[3];
		int juice[] = new int[2];
		Scanner sc = new Scanner(System.in);
		for(int i = 0; i < 3; i++){
			pasta[i] = sc.nextInt();
		}
		for(int i = 0; i < 2; i++){
			juice[i] = sc.nextInt();
		}
		int minp =3000;
		for(int i = 0; i < 3; i++){
			if(minp > pasta[i]){
				minp = pasta[i];
			}
		}
		int minj = 3000;
		for(int i = 0; i < 2; i++){
			if(minj > juice[i]){
				minj = juice[i];
			}
		}
		int sum = minp + minj;
		sum = sum - 50;
		System.out.println(sum);
	}

}
