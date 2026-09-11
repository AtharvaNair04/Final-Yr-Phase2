import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		int W = sc.nextInt();//南北方向にまっすぐに伸びる 道路
		int H = sc.nextInt();//東西方向にまっすぐに伸びる 道路
		int N = sc.nextInt();//観光スポットの数
		int i;
		int j = 0;
		int x[] = new int[N];
		int y[] = new int[N];
		int c = 0;
		
		for(i = 0; i < N; i++){
			x[i] = sc.nextInt();
			y[i] = sc.nextInt();
		}
		
		
		
		while(true){//斜め移動上
			if(x[N-2] == x[N-1] && y[N-2] == y[N-1])break;
			if(x[j] < x[j+1] && y[j] < y[j+1]){
				x[j]++;
				y[j]++;
				c++;
			}
			
			else if(x[j] > x[j+1] && y[j] > y[j+1]){//斜め移動下
				x[j]--;
				y[j]--;
				c++;
			}
				
			else if(x[j] < x[j+1] && y[j] == y[j+1]){//目的地よりも現在地が左側でy軸は等しい
				x[j]++;
				c++;
			}
				
			else if(x[j] == x[j+1] && y[j] < y[j+1]){//目的地よりも現在地が下側でx軸は等しい
				y[j]++;
				c++;
			}
				
			else if(x[j] > x[j+1] && y[j] == y[j+1]){//目的地よりも現在地が右側でy軸は等しい
				x[j]--;
				c++;
			}
				
			else if(x[j] == x[j+1] && y[j] > y[j+1]){//目的地よりも現在地が上側でx軸は等しい
				y[j]--;
				c++;
			}
				
			else if(x[j] > x[j+1] && y[j] < y[j+1]){
				x[j]--;
				y[j]++;
				c+=2;
			}
				
			else if(x[j] < x[j+1] && y[j] > y[j+1]){
				x[j]++;
				y[j]--;
				c+=2;
			}
				
			if(x[j] == x[j+1] && y[j] == y[j+1]){
				j++;
			}
			

		}

		System.out.println(c);
		
		
		
	}

}
