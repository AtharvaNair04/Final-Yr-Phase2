import java.util.*;

public class Main{
	int INF = 1 << 24;
	int n;
	int [][] g;
	ArrayList<Node> nlist;
	
	class Node implements Comparable<Node>{
		int [] dis;
		ArrayList<Integer> edge;
		public Node() {
			this.dis = new int[n];
			Arrays.fill(this.dis, -1);
			this.edge = new ArrayList<Integer>();
		}
		@Override
		public String toString() {
			return "Node [dis=" + Arrays.toString(dis) + ", edge=" + edge + "]";
		}
		@Override
		public int compareTo(Node o) {
			return this.edge.size() - o.edge.size();
		}
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n = sc.nextInt();
			if(n == 0) break;
			g = new int[n][n];
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					g[i][j] = sc.nextInt();
				}
			}
			nlist = new ArrayList<Node>();
			
			for(int i = 0; i < g[0][1]-1; i++){
				nlist.add(new Node());
				nlist.get(i).edge.add(i-1);
				if(i != 0){
					nlist.get(i-1).edge.add(i);
				}
			}
			nlist.get(nlist.size()-1).edge.add(-2);
			calcdis(0, 1, 0, -1);
			calcdis(g[0][1] - 2, 1, 1, -1);
			
			for(int i = 2; i < n; i++){
				for(int j = 0; j < nlist.size(); j++){
					int x = g[0][i] - nlist.get(j).dis[0];
					if(x <= 0)continue;
					boolean flg = true;
					for(int k = 1; k < i;k++){
						int nowx = g[k][i] - nlist.get(j).dis[k];
						if(nowx != x){
							flg =false;
							break;
						}
					}
					if(flg){
						for(int k = 0; k < x-1; k++){
							nlist.add(new Node());
							int ind = nlist.size()-1;
							if(k == 0){
								nlist.get(ind).edge.add(j);
								nlist.get(j).edge.add(ind);
							}
							else{
								nlist.get(ind).edge.add(ind-1);
								nlist.get(ind-1).edge.add(ind);
							}
							for(int l = 0; l < i; l++){
								nlist.get(ind).dis[l] = nlist.get(j).dis[l] + k + 1;
							}
						}
						if(x == 1){
							nlist.get(j).edge.add(-i-1);
							calcdis(j, 1, i, -1);
						}
						else{
							nlist.get(nlist.size()-1).edge.add(-i-1);
							calcdis(nlist.size()-1, 1, i, -1);
						}
						break;
					}
				}
			}
			
			//print
			Collections.sort(nlist);
			StringBuilder sb = new StringBuilder();
			for(Node node: nlist){
				sb.append(" " + node.edge.size());
			}
			System.out.println(sb.substring(1));
		}
		
	}
	private void calcdis(int nowpos, int dis, int ind,int prev) {
		if(nowpos < 0) return;
		nlist.get(nowpos).dis[ind] = dis;
		
		for(int i = 0; i < nlist.get(nowpos).edge.size(); i++){
			int nextnode = nlist.get(nowpos).edge.get(i);
			if(nextnode == prev) continue;
			calcdis(nextnode, dis+1, ind, nowpos);
		}
	}
	private void debug(Object... o) { System.out.println("debug = " + Arrays.deepToString(o)); }

	public static void main(String[] args) {
		new Main().doit();
	}
}
