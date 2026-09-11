import java.util.Scanner;
import java.util.Arrays;
public class Main{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
			int n = scan.nextInt();
			int[] num = new int[n];
			for(int i = 0;i < n;i++){
				num[i] = scan.nextInt();
			}
			Arrays.sort(num);
			int count = 0;
			System.out.println(1);
			for(int i = 2;i <= num[0];i++){
				count = 0;
				for(int j = 0;j < n;j++){
					if(num[j]%i != 0){
						break;
					}
					count++;
					if(count == n){
						System.out.println(i);
					}
				}
			}
		}
	}
}
