import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		int n = s.nextInt();
		
		int p[][] = new int[n*(n-1)/2][4];
		int x[] = new int[n];
		int y[] = new int[n];
		
		for(int i=0; i < n*(n-1)/2; i++){
			for(int k=0; k < 4; k++){
				p[i][k] = s.nextInt();
			}
		}
		
		for(int i=0; i < n*(n-1)/2; i++){
			if(p[i][2]  > p[i][3])		x[p[i][0] - 1] += 3;
			else if(p[i][2]  == p[i][3]){
				x[p[i][0] - 1] += 1;
				x[p[i][1] - 1] += 1;
			}
			else if(p[i][2] < p[i][3])	x[p[i][1] - 1] += 3;
		}
		
		for(int i=0; i<n; i++){
			y[i] = 1;
			for(int k=0; k<n; k++){
				if(x[i] < x[k])		y[i]++;
			}
		}
		
		for(int i=0; i < n; i++){
			System.out.println(y[i]);
		}
		
	}

}
