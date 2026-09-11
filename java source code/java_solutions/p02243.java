//Algorithms and Data Structures I - Graph II - Single Source Shortest Path II
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class MyList extends ArrayList<Node> {
	public Node getNode(int u) {
		Node ret = null;
		for(Node n:this){
			if(u==n.u){
				ret = n;
			}
		}
		return ret;
	}
}

class Node implements Comparable<Node> {
	public  int     u,
		            k,
			        d = Integer.MAX_VALUE;
	public  int[][] adj;
	boolean S;

	Node(int u, int k) {
		this.u = u;
		this.k = k;
		adj    = new int[2][k];
	}

	public int compareTo(Node n) {
		return this.d - n.d;
	}
}

class Main {

	private static ArrayList<Node>       Q   = new ArrayList<Node>();
	private static HashMap<Integer,Node> map = new HashMap<Integer,Node>();

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Node source = null,
			 nd;
		for(int i=0;i<n;i++){
			nd = new Node(sc.nextInt(), sc.nextInt());
			for (int j = 0; j < nd.k; j++) {
				nd.adj[0][j] = sc.nextInt();
				nd.adj[1][j] = sc.nextInt();
			}
			if (nd.u == 0) { nd.d   = 0 ;
			                 source = nd;}
			else           {Q.add(nd)   ;}
			map.put(nd.u, nd);
		}
		relax(source);

		while(!Q.isEmpty()){
			Collections.sort(Q);
			relax(Q.remove(0));
		}

		for(int i=0;i<n;i++){
			nd = map.get(i);
			System.out.println(nd.u+" "+nd.d);
		}
	}

	private static void relax(Node n){
		Node t;
		int  l;
		for(int i=0;i<n.k;i++){
			t = map.get(n.adj[0][i]);
			l = n.d+n.adj[1][i];
			if(!t.S && l<t.d){
				t.d=l;
			}
		}
		n.S = true;
	}
}
