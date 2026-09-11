import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		boolean[] h = new boolean[1000001];
		h[1] = true;
		int[] s = new int[1000001];
		s[1] = 1;
		
		for(int i = 2; i < 1000001; i++){
			if(i % 2 == 0 && h[i / 2])h[i] = true;
			if(i % 3 == 0 && h[i / 3])h[i] = true;
			if(i % 5 == 0 && h[i / 5])h[i] = true;
			s[i] = s[i-1] + (h[i]?1:0);
		}

		while(true){
			int m = scan.nextInt();
			if(m == 0)break;
			int n = scan.nextInt();
			System.out.println(s[n] - s[m-1]);
		}
	}
}
