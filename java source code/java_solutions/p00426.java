import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static void print(int[][] states, int[] sizes){
		for(int i = 0; i < 3; i++){
			System.out.print(sizes[i] + " :");
			for(int j = 0; j < sizes[i]; j++){
				System.out.print(" " + states[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void translate(int from, int to, int[][] states, int[] sizes){
		//System.out.println("---------------------------------");
		//print(states, sizes);
		//System.out.println(from + " => " + to);
		
		sizes[from]--;
		states[to][sizes[to]] = states[from][sizes[from]];
		sizes[to]++;
		
	}
	
	public static boolean is_same(int[][] states, int[] sizes, int[][] p_states, int[] p_sizes){
		for(int i = 0; i < 3; i++){
			if(sizes[i] != p_sizes[i]){
				return false;
			}
			
			for(int j = 0; j < sizes[i]; j++){
				if(states[i][j] != p_states[i][j]){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static int ans = 0;
	public static boolean get_ans = false;
	
	public static int move(int depth, final int n, int from, int use, int to, int[][] states, int[] sizes, int[][] p_states, int[] p_sizes){
		int walk = 0;
		
		if(depth < n){
			walk += move(depth + 1, n, from, use, to, states, sizes, p_states, p_sizes);
		}
		
		//move
		//System.out.println(depth + "  : " + from + " => " + use);
		/*
		if(is_same(states, sizes, p_states, p_sizes)){
			ans = walk;
		}
		*/
		
		if(!get_ans && is_same(states, sizes, p_states, p_sizes)){
			get_ans = true;
		}
		
		translate(from, use, states, sizes);
		walk++;
		
		if(!get_ans){
			ans++;
		}
		
		/*
		if(is_same(states, sizes, p_states, p_sizes)){
			ans = walk;
		}
		*/
		
		if(depth < n){
			walk += move(depth + 1, n, to, use, from, states, sizes, p_states, p_sizes);
		}
		
		
		//move
		//System.out.println(depth + "  : " + use + " => " + to);
		/*
		if(is_same(states, sizes, p_states, p_sizes)){
			ans = walk;
		}
		*/
		
		if(!get_ans && is_same(states, sizes, p_states, p_sizes)){
			get_ans = true;
		}
		
		translate(use, to, states, sizes);
		walk++;
		
		if(!get_ans){
			ans++;
		}
		
		/*
		if(is_same(states, sizes, p_states, p_sizes)){
			ans = walk;
		}
		*/
		
		if(depth < n){
			walk += move(depth + 1, n, from, use, to, states, sizes, p_states, p_sizes);
		}
		
		/*
		if(is_same(states, sizes, p_states, p_sizes)){
			ans = walk;
		}
		*/
		
		return walk;
	}
	
	public static final int MAX = 16;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int[][] init_states = new int[3][MAX];
		int[]   init_sizes  = new int[3];
		int[][] p_states = new int[3][MAX];
		int[]   p_sizes  = new int[3];
		
		while(true){
			final int n = sc.nextInt();
			final int m = sc.nextInt();
			
			if(n == 0 && m == 0){
				break;
			}
			
			for(int i = 0; i < 3; i++){
				p_sizes[i] = sc.nextInt();
				
				for(int j = 0; j < p_sizes[i]; j++){
					p_states[i][j] = sc.nextInt();
				}
			}
			
			ans = 0;
			get_ans = false;
			init_sizes[0] = n; init_sizes[1] = 0; init_sizes[2] = 0;
			for(int i = 0; i < n; i++){
				init_states[0][i] = i + 1;
			}
			
			final int max = move(1, n, 0, 1, 2, init_states, init_sizes, p_states, p_sizes);
			
			int answer = Math.min(ans, max - ans);
			
			System.out.println(answer <= m ? answer : -1);
			
		}
	}
}
