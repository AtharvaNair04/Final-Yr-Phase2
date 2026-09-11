import java.util.*;
public class Main {
	//0155 start
	//0211 cording end
	//0221 sample matched
	int INF = 1 << 24;
	
	class State implements Comparable<State> {
		int now, cost, fix;

		public State(int now, int cost, int fix) {
			this.now = now;
			this.cost = cost;
			this.fix = fix;
		}

		@Override
		public int compareTo(State o) {
			if(this.fix < o.fix)return -1;
			if(this.fix > o.fix) return 1;
			if(this.cost < o.cost) return -1;
			if(this.cost > o.cost ) return 1;
			return 0;
		}

		@Override
		public String toString() {
			return "State [now=" + now + ", cost=" + cost + ", fix=" + fix
					+ "]";
		}
		
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			int m = sc.nextInt();
			int c = sc.nextInt();
			if((n|m|c) == 0 ) break;
			int [][] pass = new int[n][n];
			for(int i = 0 ; i < n;i++){
				Arrays.fill(pass[i], INF);
				pass[i][i] = 0;
			}
			for(int i = 0 ; i < m; i++){
				int from = sc.nextInt() - 1;
				int to = sc.nextInt() - 1;
				int cost = sc.nextInt();
				pass[from][to] = cost;
				//pass[to][from] = cost;
			}
			
			PriorityQueue<State> open = new PriorityQueue<Main.State>();
			open.add(new State(0, 0, 0));
			int [][] close = new int[n][m + 2];
			for(int i = 1 ; i < n; i++){
				Arrays.fill(close[i], INF);
			}
			
			int ans = INF;
			while(! open.isEmpty()){
				State now = open.poll();
				if(now.now == n-1){
					//System.out.println(now);
					ans = now.fix;
					break;
				}
				for(int i = 0 ; i < n; i++){
					if(pass[now.now][i] == INF) continue;
					int nextfix = now.fix + 1;
					if(close[i][nextfix] > now.cost){
						open.add(new State(i, now.cost, nextfix));
						close[i][nextfix] = now.cost;
					}
					
					nextfix = now.fix;
					int nextcost = now.cost + pass[now.now][i];
					if(nextcost > c) continue;
					if(close[i][nextfix] > nextcost){
						open.add( new State(i, nextcost, nextfix));
						close[i][nextfix] = nextcost;
					}
				}
			}
			System.out.println(ans);
		}
	}
	
	
	public static void main(String [] args){
		new Main().doit();
	}
}
