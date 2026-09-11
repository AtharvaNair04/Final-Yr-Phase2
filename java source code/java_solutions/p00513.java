import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
			int n = scan.nextInt();
			boolean fl = false;
			int count = 0;
			for(int i = 0;i < n;i++){
				int s = scan.nextInt();
				fl = false;
				for(int j = 1;j <= s/4;j++){
					if((s-j)%(2*j+1) == 0){
						fl = true;
						break;
					}
				}
				if(!fl){
					count++;
				}
			}
			System.out.println(count);
		}
	}
}
