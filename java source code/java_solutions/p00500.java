import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int i,j,k,c = 0;
		int[][] a;
		a = new int[n][n];
		for(i=0; i<n; i++){
			a[0][i] = sc.nextInt();
			a[1][i] = sc.nextInt();
			a[2][i] = sc.nextInt();
			}
		for(i=0; i<3; i++){
			for(j=0; j<n; j++){
					for(k=j+1; k<n; k++){
					if(a[i][j] == a[i][k]){
						a[i][k] = 0;
						c = c + 1;
						}
					}
					if(c > 0){
						a[i][j] = 0;
					}
					c = 0;
				}
			}
		for(i=0; i<n; i++){
			System.out.println(a[0][i] + a[1][i] + a[2][i]);
		}
	}
}
