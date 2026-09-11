//Minimum Spanning Tree
//Kruskal's algorithm

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

class Edge implements Comparable<Edge> {
	public int v1,v2,w;
	Edge(int v1,int v2,int w){
		this.v1=v1; this.v2=v2; this.w=w;
	}
	public int compareTo(Edge e){
		return this.w - e.w;
	}
}

class Main {

	private static final int                         EMPTY = -1;
	private static       ArrayList<Edge>             E     = new ArrayList<Edge>();
	private static       ArrayList<HashSet<Integer>> V     = new ArrayList<HashSet<Integer>>();

	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		int t;
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				t = sc.nextInt();
				if(t!=EMPTY && j>=i+1){
					E.add(new Edge(i,j,t));
				}
			}
		}
		Collections.sort(E);
		HashSet<Integer> set,t1,t2;
		int v1=EMPTY, v2=EMPTY,sum_w=0;
		out:while(!E.isEmpty()){
			Edge e = E.remove(0);
			t1 = null; t2 = null;
			for(int i=0;i<V.size();i++){
				set = V.get(i);
				if        (set.contains(e.v1)&&set.contains(e.v2)){
					continue out;
				} else if (set.contains(e.v1)){
					t1 = set; v2 = e.v2;
				} else if (set.contains(e.v2)){
					t2 = set; v1 = e.v1;
				}
				if(t1!=null&&t2!=null){break;}
			}
			sum_w += e.w;
			if     (t1!=null&&t2!=null){t1.addAll(t2); V.remove(t2);}
			else if(t1!=null)          {t1.add(v2)   ;}
			else if(t2!=null)          {t2.add(v1)   ;}
			else                       {
				HashSet<Integer> s = new HashSet<Integer>();
				s.add(e.v1); s.add(e.v2); V.add(s);
			}
		}
		System.out.println(sum_w);
	}
}
