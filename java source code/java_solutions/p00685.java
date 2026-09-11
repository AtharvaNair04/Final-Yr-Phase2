import java.util.*;
public class Main {	
	Scanner in = new Scanner(System.in);
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){		
		new AOJ1103();
	}
	
	class AOJ1103{
		Relative relative[];
		public AOJ1103() {
			while(true){
				relative = new Relative[4];
				boolean sw=false;
				for(int i=0;i<4;i++){
					int a = in.nextInt();
					if(a>=4){
						sw=true;break;
					}
					int b = in.nextInt();
					relative[i]=new Relative(a,b);
				}
				if(sw)break;
				int[][] map = new int[4][4];for(int i=0;i<4;i++)Arrays.fill(map[i],-1);
				System.out.println(bfs(map,0));
			}
		}
		void print(int[][] map){
			for(int i=0;i<4;i++){
				for(int s=0;s<4;s++)System.out.print(map[i][s]+" ");
				System.out.println();
			}
		}
		
		int bfs(int[][] map,int num){
			
//			System.out.println(num);
//			print(map);
//			System.out.println();
			
			int result = 0;
			if(num>=7){
				boolean sw = true;
				for(int i=0;i<4;i++)for(int s=0;s<4;s++)if(map[s][i]==-1)sw=false;
				if(sw)return 1;
			}else if(num>=7)return 0;
			int x=0,y=0;
			for(y=0;y<4;y++){//一番目に置くカードの位置
				boolean sw=true;
				for(x=0;x<4;x++)if(map[y][x]==-1){
					sw=false;
					break;
				}
				if(!sw)break;
			}
			
			for(int i=0;i<4;i++){//二枚目に置くカードの位置
				int nx = x+relative[i].x;
				int ny = y+relative[i].y;//もう片方の数字
				if(nx<0||nx>=4||ny<0||ny>=4)continue;
				if(map[ny][nx]!=-1)continue;
				map[ny][nx]=map[y][x]=num;
				result+=bfs(map,num+1);
//				if(bfs(map, num+1)==1){
//					System.out.println(num);
//					print(map);
//					System.out.println();
//				}
				map[ny][nx]=map[y][x]=-1;
			}
			return result;
		}
		
		class Relative{
			int x,y;
			public Relative(int x,int y) {
				this.x = x;
				this.y = y;
			}
		}
	}
	
}
