import java.util.Scanner;
public class Main{
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		int i;
		while(true){
			int n = sc.nextInt();
			if(n==0){
				break;
			}
			int score[] = new int[n];
			for(i=0;i<score.length;i++){
				score[i]=sc.nextInt();
			}
			double ave=0,v=0,x=0;
			int sum=0;
			for(i=0;i<score.length;i++){
				sum = sum + score[i];
			}
			ave = (double)sum/n;
			for(i=0;i<score.length;i++){
				x=x+(score[i]-ave)*(score[i]-ave);
			}
			v=x/n;
			System.out.println(Math.sqrt(v));
		}
	}
}
