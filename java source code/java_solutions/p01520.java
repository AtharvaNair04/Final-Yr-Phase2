import java.util.Scanner;
public class Main{
	public static void main(String[] args){
        new Main().run();
    }
	public void run(){
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
			int n = scan.nextInt();
			int t = scan.nextInt();
			int e = scan.nextInt();
			int count = 0;
			int[] x = new int[n];
			for(int i = 0;i < n;i++){
				x[i] = scan.nextInt();
			}
			for(int i = 0;i < n && count == 0;i++){
				for(int j = t-e;j <= t+e;j++){
					if(j%x[i] == 0){
						count = i+1;
						break;
					}
				}
			}
			System.out.println((count == 0)?-1:count);
		}
	}
}
