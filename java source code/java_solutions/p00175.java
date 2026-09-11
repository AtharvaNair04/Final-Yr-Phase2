import java.util.Arrays;
import java.util.Scanner;
public class Main {
	public static void main(String[] args){
		Scanner scan =new Scanner(System.in);
		while(true){
		int n=scan.nextInt();
		if(n==-1)break;
		int []a=new int[10];
		int len=0;
		while(n!=0){
			a[len]=n%4;
			n=n/4;
			len++;
		}
		if(len==0)System.out.println('0');
		else{
		for(int i=len-1;i>=0;i--){
			System.out.print(a[i]);
			}
		System.out.println();
		}
		}
	}
}
