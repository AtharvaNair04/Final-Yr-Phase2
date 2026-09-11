import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class Main{
	public static Node[] node;
	public static int[][] cost;
	public static int n;
	public static Scanner in = new Scanner(System.in);
	public static void main(String[] args){
		while(true){
			n = in.nextInt();
			if(n == 0) break;
			long sum = init();
			long res = remove();
			long longest = longest();
			System.out.println(res + longest*2 + (sum - res - longest)*3);
		}
	}
	
	public static long longest(){
		used = new BitSet(n);
		pathId = 0;
		path = -1;
		for(int i=0; i<n; i++){
			if(node[i] == null) continue;
			dfs(node[i], 0);
			path = 0;
			used.clear();
			if(pathId >= 0) dfs(node[pathId], 0);
			break;
		}
		return path;
	}
	
	public static long path;
	public static int pathId;
	public static BitSet used;
	public static void dfs(Node nd, long dist){
		used.set(nd.id);
		long res = dist;
		boolean update = false;
		for(Node v: nd.edge){
			if(used.get(v.id)) continue;
			update = true;
			used.set(v.id);
			dfs(v, dist + cost[nd.id][v.id]);
		}
		if(!update && path < res){
			path = res;
			pathId = nd.id;
		}
	}
	
	public static long init(){
		node = new Node[n];
		for(int i=0; i<n; i++){
			node[i] = new Node(i);
		}
		cost = new int[n][n];
		int[] map = new int[n];
		for(int i=1; i<n; i++){
			int p = in.nextInt()-1;
			node[p].createEdge(node[i]);
			node[i].createEdge(node[p]);
			map[i] = p;
		}
		long sum = 0;
		for(int i=1; i<n; i++){
			int d = in.nextInt();
			cost[i][map[i]] = d;
			cost[map[i]][i] = d;
			sum += d;
		}
		return sum;
	}
	
	public static long remove(){
		long res = 0;
		BitSet bs = new BitSet(n);
		for(int i=0; i<n; i++){
			if(node[i].edge.size() > 1) continue;
			bs.set(i);
		}
		for(int i=bs.nextSetBit(0); i!=-1; i=bs.nextSetBit(i+1)){
			Node next = node[i].edge.get(0);
			res += cost[i][next.id];
			next.edge.remove(node[i]);
			node[i].edge.remove(next);
			node[i] = null;
		}
		return res;
	}
}

class Node{
	int id;
	List<Node> edge = new ArrayList<Node>();
	public Node(int id){
		this.id = id;
	}
	public void createEdge(Node n){
		edge.add(n);
	}
}
