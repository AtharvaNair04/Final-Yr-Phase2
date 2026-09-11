import java.util.Scanner;

class Main {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int n, y;
		int[] h;
		
		while(true){
			n = scan.nextInt();
			if(n == 0)break;
			
			h = new int[7];
			for(int i = 0; i < n; i++){
				y = scan.nextInt();
				
				if(y < 10)h[0]++;
				else if(y < 20)h[1]++;
				else if(y < 30)h[2]++;
				else if(y < 40)h[3]++;
				else if(y < 50)h[4]++;
				else if(y < 60)h[5]++;
				else h[6]++;
			}
			for(int i = 0; i < 7; i++){
				System.out.println(h[i]);
			}
		}
	}
}
