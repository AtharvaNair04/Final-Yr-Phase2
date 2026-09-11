import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int c = sc.nextInt();
		long sum=0;
		long sum1=0;
		for(int i=1;i<=c;i++) {
			sum+=i;
			if(i%3==0||i%5==0) {
				sum1+=i;
			}
		}
		System.out.println(sum-sum1);


	}

}
