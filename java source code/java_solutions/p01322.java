import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		while(true){
			int n = stdIn.nextInt();
			if(n==0){
				return;
			}
			int m = stdIn.nextInt();
			char[][] t = new char[n][8];
			int[] p = new int[n];		// price
			for(int i=0;i<n;i++){
				t[i] = stdIn.next().toCharArray();
				p[i] = stdIn.nextInt();
			}
			
			int pay = 0;
			for(int i=0;i<m;i++){			// paper
				char[] h = new char[8];
				h = stdIn.next().toCharArray();
				int idx = 8;
				int tmp = 0;
				for(int j=0;j<n;j++){		// target
					for(int k=7;k>=-1;k--){		// digit
						if(k==-1||t[j][k]=='*'){
							if(k<idx){
								idx = k;
								tmp = p[j];
							}
							break;
						}
						else if(h[k]!=t[j][k]){
							break;
						}
						else if(h[k]==t[j][k]){
							continue;
						}
					}
				}
				pay += tmp;
			}
			System.out.println(pay);
		}
	}
}
