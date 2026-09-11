import java.util.*;

public class Main {
	boolean[][] matrix;
	int n, m;
	
	void print(){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(j!=0) System.out.print(" ");
				if(matrix[i][j]==true) System.out.print(1);
				else System.out.print(0);
			}
			System.out.println();
		}
	}
	
	void Rotate(int r, int c, int size, int angle){
		boolean[][] temp = new boolean[size][size];
		for(int k=0;k<angle/90;k++){
			for(int i=0;i<size;i++){
				for(int j=0;j<size;j++){
					temp[i][j] = matrix[r+i][c+j];
				}
			}
			for(int i=0;i<size;i++){
				for(int j=0;j<size;j++){
					matrix[r+i][c+j] = temp[size-1-j][i];
				}
			}
		}
	}
	
	void Island(int r, int c){
		boolean f = matrix[r][c];
		int py = r;
		int px = c;
		int sy, sx;
		int[] a = new int[]{0,0,1,-1};
		int[] b = new int[]{1,-1,0,0};
		ArrayDeque<Integer> y = new ArrayDeque<Integer>();
		ArrayDeque<Integer> x = new ArrayDeque<Integer>();
		y.add(r);
		x.add(c);
		matrix[py][px] ^= true;
		while(y.size()!=0){
			py = y.poll();
			px = x.poll();
			for(int i=0;i<4;i++){
				sy = py+a[i];
				sx = px+b[i];
				if(0<=sy && sy<=n-1 && 0<=sx && sx<=n-1){
					if(matrix[sy][sx]==f){
						y.offer(sy);
						x.offer(sx);
						matrix[sy][sx] ^= true;
					}
				}
			}
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();
		matrix = new boolean[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				int p = sc.nextInt();
				if(p==1) matrix[i][j] = true;
			}
		}
		for(int i=0;i<m;i++){
			int o = sc.nextInt();
			int r = sc.nextInt()-1;
			if(o==0){
				int c = sc.nextInt()-1;
				int size = sc.nextInt();
				int angle = sc.nextInt();
				if(angle==90 || angle==180 || angle==270) Rotate(r, c, size, angle);
			}else if(o==1){
				int c = sc.nextInt()-1;
				int size = sc.nextInt();
				for(int j=0;j<size;j++){
					for(int k=0;k<size;k++) matrix[r+j][c+k] ^= true;
				}
			}else if(o==2){
				boolean f = matrix[r][0];
				for(int j=0;j<n-1;j++) matrix[r][j] = matrix[r][j+1];
				matrix[r][n-1] = f;
			}else if(o==3){
				boolean f = matrix[r][n-1];
				for(int j=n-1;j>0;j--) matrix[r][j] = matrix[r][j-1];
				matrix[r][0] = f;
			}else if(o==4){
				int c = sc.nextInt()-1;
				Island(r, c);
			}
		}
		print();
	}
	
	public static void main(String[] args) {
		new Main().run();
	}

}
