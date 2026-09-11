import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static class UnionFind{
		final int n;
		int par[];
		
		public UnionFind(int n){
			this.n = n;
			par = new int[this.n];
			for(int i = 0; i < this.n; i++){
				par[i] = i;
			}
		}
		
		public int find(int x){
			if(par[x] == x){
				return x;
			}
			
			return par[x] = find(par[x]);
		}
		
		public void unite(int x, int y){
			if(same(x, y)){
				return;
			}
			
			par[find(y)] = find(x);
		}
		
		public boolean same(int x, int y){
			return find(x) == find(y);
		}
	}
	
	public static int translate(int x, int y, int W, int H){
		return y * W + x;
	}
	
	public static void print(UnionFind uf, int W, int H){
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				System.out.printf("%4d ", uf.find(translate(j, i, W, H)));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static boolean is_ok(int x, int y, int W, int H){
		if(x < 0 || x >= W || y < 0 || y >= H){
			return false;
		}else{
			return true;
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		final int W = sc.nextInt();
		final int H = sc.nextInt();
		
		boolean[][] is_wall = new boolean[H][W];
		int goal_x = -1, goal_y = -1;
		
		for(int i = 0; i < H; i++){
			char[] input = sc.next().toCharArray();
			
			for(int j = 0; j < W; j++){
				if(input[j] == '#'){
					is_wall[i][j] = true;
				}
				
				if(input[j] == 't'){
					goal_x = j;
					goal_y = i;
				}
			}
		}
		final int size = H * W;
		UnionFind uf = new UnionFind(size);
		
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				if(is_wall[i][j]){
					continue;
				}
				
				if(is_ok(j, i + 1, W, H) && !is_wall[i + 1][j]){
					uf.unite(translate(j, i, W, H), translate(j, i + 1, W, H));
				}
				
				if(is_ok(j + 1, i, W, H) && !is_wall[i][j + 1]){
					uf.unite(translate(j, i, W, H), translate(j + 1, i, W, H));
				}
				
				if(is_ok(j, i - 1, W, H) && !is_wall[i - 1][j]){
					uf.unite(translate(j, i, W, H), translate(j, i - 1, W, H));
				}
				
				if(is_ok(j - 1, i, W, H) && !is_wall[i][j - 1]){
					uf.unite(translate(j, i, W, H), translate(j - 1, i, W, H));
				}
			}
		}
		
		final int N = sc.nextInt();
		boolean ok = uf.same(translate(0, 0, W, H), translate(goal_x, goal_y, W, H));
		int time = 0;
		
		for(int i = 0; i < N; i++){
			final int x = sc.nextInt();
			final int y = sc.nextInt();
			
			//print(uf, W, H);
			
			if(ok){
				continue;
			}
			
			time++;
			if(!is_wall[y][x]){
				continue;
			}
			
			is_wall[y][x] = false;
			
			if(is_ok(x - 1, y, W, H) && !is_wall[y][x - 1]){
				uf.unite(translate(x, y, W, H), translate(x - 1, y, W, H));
			}
			
			if(is_ok(x + 1, y, W, H) && !is_wall[y][x + 1]){
				uf.unite(translate(x, y, W, H), translate(x + 1, y, W, H));
			}
			
			if(is_ok(x, y - 1, W, H) && !is_wall[y - 1][x]){
				uf.unite(translate(x, y, W, H), translate(x, y - 1, W, H));
			}
			
			if(is_ok(x, y + 1, W, H) && !is_wall[y + 1][x]){
				uf.unite(translate(x, y, W, H), translate(x, y + 1, W, H));
			}
			
			ok |= uf.same(translate(0, 0, W, H), translate(goal_x, goal_y, W, H));
		}
		
		System.out.println(ok ? time : -1);
	}

}
