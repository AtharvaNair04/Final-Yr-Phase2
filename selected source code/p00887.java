import java.util.Scanner;

public class Main {
	
	static int[][] s;
	static int m, n, d, S;
	
	static boolean gauss(int[][] A) {
		for(int i=0; i<S; i++) {
			int pivot=i;
			for(int j=i; j<S; j++) {
				if(A[j][i]!=0) {
					pivot=j;
					break;
				}
			}
			
			for(int j=0; j<S+1; j++) {
				int a=A[i][j];
				A[i][j]=A[pivot][j];
				A[pivot][j]=a;
			}
			
			if(Math.abs(A[i][i])<0.000000001) {
				continue;
			}
			
			for(int j=0; j<S; j++) {
				if(i!=j && A[j][i]==1) {
					for(int k=0; k<S+1; k++) {
						A[j][k]+=A[i][k];
						A[j][k]%=2;
					}
				}
			}
		}
		for(int i=0; i<S; i++) {
			if(A[i][i]==0 && A[i][S]>0) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			while(sc.hasNext()) {
				m=sc.nextInt();//width
				n=sc.nextInt();//hight
				d=sc.nextInt();
				if(m==0) break;
				s=new int[n][m];
				
				
				for(int i=0; i<n; i++) {
					for(int j=0; j<m; j++) {
						s[i][j]=sc.nextInt();
					}
				}
				
				S=n*m;
				int[][] field=new int[S][S+1];
				//行列式を作る
				for(int i=0; i<n; i++) {
					for(int j=0; j<m; j++) {
						field[i*m+j][i*m+j]=1;
						if(s[i][j]==1) {
							field[i*m+j][S]=1;
						}
						for(int y=0; y<n; y++) {
							for(int x=0; x<m; x++) {
								if(Math.abs(j-x)+Math.abs(i-y)==d ) {
									field[y*m+x][i*m+j]=1;
								}
							}
						}
					}
				}
				System.out.println(gauss(field)? 1:0);
				
				
				
			}
		}
	}
}
