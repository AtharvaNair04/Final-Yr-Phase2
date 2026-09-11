import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{
	static Queue<Pair> queue = new LinkedList<Pair>();
	
	static class Pair {
		int nextNumber;
		int cost;
		
		Pair(int nextNumber, int cost) {
			this.nextNumber = nextNumber;
			this.cost = cost;
		}
	}
	
	static class Router {
		int number;
		int degree;
		Router next[];
		int cost;
		boolean mark;
		
		Router(int number) {
			this.number = number;
		}
	}
	
	public static void main(String srgs[]) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		Router rt[];
		
		n = sc.nextInt();
		rt = new Router[n + 1];
		for (int i = 0; i < n + 1; i++) {
			rt[i] = new Router(i);
		}
		for (int i = 0; i < n; i++) {
			int number = sc.nextInt();
			int degree = sc.nextInt();
			rt[number].next = new Router[degree];
			rt[number].degree = degree;
			for (int j = 0; j < degree; j++) {
				int nextIndex = sc.nextInt();
				rt[number].next[j] = rt[nextIndex];
			}
		}
		
		m = sc.nextInt();
		for (int i = 0; i < m; i++) {
			int start = sc.nextInt();
			int goal = sc.nextInt();
			int ttl = sc.nextInt();
			for (int j = 1; j <= n; j++) {
				rt[j].mark = false;
				rt[j].cost = Integer.MAX_VALUE;
			}
			queue.clear();
			bestWay(rt, start, goal, 1);
			if (rt[goal].cost <= ttl) {
				System.out.println(rt[goal].cost);
			} else {
				System.out.println("NA");
			}
		}
	}
	
	public static boolean bestWay(Router rt[], int start, int goal, int cost) {
		if (rt[start].mark) {
			return false;
		}
		rt[start].mark = true;
		rt[start].cost = cost;
		if (rt[goal].mark) {
			return true;
		}
		
		for (int i = 0; i < rt[start].degree; i++) {
			Pair p = new Pair(rt[start].next[i].number, cost + 1);
			queue.add(p);
		}
		
		while (!queue.isEmpty()) {
			Pair p = queue.poll();
			if (bestWay(rt, p.nextNumber, goal, p.cost)) {
				return true;
			}
		}
		return false;
	}
}
