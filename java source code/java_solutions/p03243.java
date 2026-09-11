import java.util.*;

public class Main{
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		for(int k=1;k<=9;k++){
			if(k+10*k+100*k >= n){
				System.out.println(k+10*k+100*k);
				break;
			}
		}
	}
}
