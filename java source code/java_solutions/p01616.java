import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	HashMap<String, Integer> map;
	int m;
	V[] vs;
	
	void run() {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(); m = sc.nextInt();
		map = new HashMap<String, Integer>();
		id = 0;
		vs = new V[2*m+10];
		for(int i=0;i<2*m+10;i++) vs[i] = new V();
		for(int i=0;i<n;i++) {
			s = sc.next(); p = 0;
			E e = exp();
//			debug(e.v1, e.v2, e.op);
			if( e.op == 1) {
				vs[id2num(-e.v1)].l.add(id2num(e.v2));
				vs[id2num(e.v2)].rl.add(id2num(-e.v1));
				vs[id2num(-e.v2)].l.add(id2num(e.v1));
				vs[id2num(e.v1)].rl.add(id2num(-e.v2));
			} else {
				vs[id2num(-e.v1)].l.add(id2num(e.v1));
				vs[id2num(e.v1)].rl.add(id2num(-e.v1));
				vs[id2num(-e.v2)].l.add(id2num(e.v2));
				vs[id2num(e.v2)].rl.add(id2num(-e.v2));
			}
		}
//		for(V v: vs) debug(v.l, v.rl);
//		debug(map);
		scc();
//		debug(comp);
		boolean ans = true;
		for(int i=1;i<=m;i++) ans &= comp[i*2-1] != comp[i*2];
		System.out.println((ans? "accept": "reject"));
	}
	LinkedList<Integer> list;
	boolean visited[];
	int[] comp;
	int scc() {
		list = new LinkedList<Integer>();
		visited = new boolean[2*m+10];
		comp = new int[2*m+10];
		for(int i=1;i<2*m;i++) if(!visited[i]){
			dfs(i);
		}
//		debug(list);
		fill(visited, false);
		int k = 0;
		for(int p: list) if(!visited[p]){
			rdfs(p, ++k);
		}
		return k;
	}
	
	void dfs(int p) {
		visited[p] = true;
		for(int i: vs[p].l) if(!visited[i])dfs(i);
		list.addFirst(p);
	}
	
	void rdfs(int p, int k) {
		visited[p] = true;
		comp[p] = k;
		for(int i: vs[p].rl) if(!visited[i])rdfs(i, k);
	}
	
	class V {
		ArrayList<Integer> l;
		ArrayList<Integer> rl;
		V() {
			l = new ArrayList<Integer>();
			rl = new ArrayList<Integer>();
		}
	}
	
	int id2num(int id) {
		if(id<=0) return -id*2;
		return id*2-1;
	}
	
	String s; int p;
	char next() {
		return s.charAt(p++);
	}
	
	E exp() {
		E e1 = term();
//		debug(e1.v1, e1.v2, e1.op);
		char c = next(); int op = -1;
		if( c == ';' || c == ')') return e1;
		if( c == '|' ) op = 1;
		if( c == '&' ) op = 0;
		E e2 = term();
		return new E(e1.v1, e2.v1, op);
	}
	
	E term() {
		char c = next();
		if( c == '~' ) {
			E e = term();
//			debug("", e.v1, e.v2, e.op);
			if( e.op == 2 ) {
				return new E(-e.v1, -e.v2, e.op);
			}
			return new E(-e.v1, -e.v2, 1-e.op);
		}
		if( c == '(' ) {
			E e = exp();
//			debug("", e.v1, e.v2, e.op, p);
			return e;
		}
		return new E(val(), 0, 2);
	}
	
	int id;
	int val() {
		p--;
		String name = "";
		for(char c=next();(c>='a' && c<='z') || (c>='A' && c<='Z');c=next() )
			name += c;
		p--;
		if( map.containsKey(name) ) return map.get(name);
		map.put(name, ++id);
		return id;
	}
	
	//op and: 0, or: 1, non: 2
	class E {
		int v1, v2, op;
		E(int v1, int v2, int op ) {
			this.v1 = v1;
			this.v2 = v2;
			this.op = op;
		}
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
