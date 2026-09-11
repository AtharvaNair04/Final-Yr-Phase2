import java.util.*;
import java.io.*;
import java.math.*;
public class Main {
	static int max;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while(true) {
			max = sc.nextInt();
			if(max == 0) break;
			int n = sc.nextInt();
			int[] d = new int[n+2];
			for(int i = 0; i < n; i++) {
				d[i+1] = sc.nextInt();
			}
			Node[] list = new Node[n+2];
			for(int i = 0; i < n+2; i++) {
				list[i] = new Node(i);
			}
			dfs(0,list,d);
			boolean[] ok = new boolean[n+2];
			dfs(n+1,list,ok);
			boolean ans = dfs2(0,list,ok,new boolean[n+2]);
			System.out.println((ans)?"OK":"NG");
		}
		
	}
	static class Node {
		ArrayList<Node> to = new ArrayList<Node>();
		ArrayList<Node> dst = new ArrayList<Node>();
		int id;
		Node(int a) {
			id = a;
		}
	}
	static boolean dfs2(int a,Node[] b, boolean[] c, boolean[] d) {
		if(!c[a]) return false;
		if(d[a]) return true;
		d[a] = true;
		boolean ret = true;
		for(int i = 0; i < b[a].to.size(); i++) {
			ret &= dfs2(b[a].to.get(i).id,b,c,d);
		}
		return ret;	
	}
	
	
	
	static void dfs(int a, Node[] b, boolean[] c) {
		if(c[a]) return;
		c[a] = true;
		for(int i = 0; i < b[a].dst.size(); i++) {
			dfs(b[a].dst.get(i).id,b,c);
		}	
	}
	
	static void dfs(int a, Node[] b, int[] c) {
		for(int i = 1; i <= max; i++) {
			int to = a + i;
			if(to >= b.length) to = b.length-1;
			to += c[to];
			if(to >= b.length) to = b.length-1;
			if(to < 0) to = 0;
			if(b[a].to.contains(b[to])) continue;
			b[a].to.add(b[to]);
			if(b[to].dst.contains(b[a])) continue;
			b[to].dst.add(b[a]);
			
			dfs(to,b,c);
		}
	}
	
}
