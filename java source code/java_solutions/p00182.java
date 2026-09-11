import java.util.*;
import java.util.Map.Entry;
import java.math.*;
import java.awt.geom.*;
import java.io.*;
      
      
public class Main {	
	static boolean[] use;
	static boolean[] used;
	static boolean[] notEmpty;
	static int[]     beaker;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			int n = sc.nextInt();
			if(n == 0) break;
			beaker = new int[n];
			for(int i = 0; i < n; i++) {
				beaker[i] = sc.nextInt();
			}
			Arrays.sort(beaker);
			int max = beaker[n-1];
			use = new boolean[n];
			used = new boolean[n];
			notEmpty = new boolean[n];
			use[0] = true;
			
			boolean ans = dfs(1,max - beaker[0]);
			System.out.println(ans?"YES":"NO");
		}
	}
	static boolean dfs(int index, int water) {
		if(water == 0) {
			used = new boolean[beaker.length];
			notEmpty = new boolean[beaker.length];
			for(int i = 0; i < use.length; i++) {
				if(use[i]) {
					notEmpty[i] = true;
				}
			}
			return greedy();
		}
		if(water < 0) return false;
		if(index >= beaker.length) return false;
		use[index] = true;
		if(dfs(index+1,water - beaker[index])) return true;
		use[index] = false;
		return dfs(index+1,water);
	}
	
	static boolean greedy() {
		for(int i = 0; i < used.length; i++) {
			if(!used[i] && !notEmpty[i]) {
				if(!c(i-1,beaker[i])) return false;
				notEmpty[i] = true;
			}
		}
		return true;
	}
	
	static boolean c(int index, int water) {
		if(water == 0) return true;
		if(water < 0) return false;
		for(int i = index; i >= 0; i--) {
			if(!used[i] && notEmpty[i]) {
				used[i] = true;
				notEmpty[i] = false;
				if(c(index-1,water - beaker[i])) return true;
				used[i] = false;
				notEmpty[i] = true;
			}
		}
		return false;
	}
	
	
}
