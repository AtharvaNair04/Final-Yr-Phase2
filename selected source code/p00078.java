import java.util.Scanner;
public class Main {
	public static void main(String[] args){
		Scanner sc=new Scanner(System.in);
		while(true){
			int n=sc.nextInt();
			if(n==0){
				break;
			}
			int[][] map=new int[n][n];
			boolean[][] flag=new boolean[n][n];
			int x=n/2;
			int y=n/2+1;
			map[y][x]=1;
			flag[y][x]=true;
			for(int i=2;i<=n*n;i++){
				x++;
				y++;
				if(x==n&&y==n){
					x--;
					for(int j=0;j<n;j++){
						if(!flag[j][x]){
							y=j;
							break;
						}
					}
				}else if(x==-1&&y==n){
					x++;
					for(int j=0;j<n;j++){
						if(!flag[j][x]){
							y=j;
							break;
						}
					}
				}else if(x==n){
					x=0;
				}else if(y==n){
					y=0;
				}else if(x==-1){
					x=n-1;
				}
				if(flag[y][x]){
					x-=2;
					i--;
				}else{
					map[y][x]=i;
					flag[y][x]=true;
				}
			}
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					System.out.printf("%4d",map[i][j]);
				}
				System.out.println();
			}
		}
	}
}
