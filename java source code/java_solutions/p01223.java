import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int t, n, b;
		int up, down;
		int [] block;
		
		t = scan.nextInt();
		
		for(int i = 0; i < t; i++){
			n = scan.nextInt();
			
			up = 0;
			down = 0;
			block = new int[n];
			for(int j = 0;j < n; j++){
				b = scan.nextInt();
				block[j] = b;
			}
			
			for(int j = 0; j < n - 1; j++){
				if(block[j] < block[j + 1]){
					if(up < block[j + 1] - block[j])up = block[j + 1] - block[j];
				}else if(block[j] > block[j + 1]){
					if(block[j] - block[j + 1] > down)down = block[j] - block[j + 1];
				}
			}
			
			System.out.println(up + " " + down);
		}
	}
}
