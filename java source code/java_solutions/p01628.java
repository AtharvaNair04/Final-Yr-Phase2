import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
  
  
public class Main{
	
	public static int calc(final int N, final int M, LinkedList<Integer> bar_list, LinkedList<Integer> baz_list){
        int[] high = new int[N];
		
        int max = 0;
        Iterator<Integer> bar_iter = bar_list.iterator();
        Iterator<Integer> baz_iter = baz_list.iterator();
        
        while(true){
        	if(!bar_iter.hasNext() || !baz_iter.hasNext()){
        		break;
        	}
        	
            final int bar = bar_iter.next();
            final int baz = baz_iter.next();
             
            final int hi = Math.max(high[bar], high[baz]);
             
            high[bar] = high[baz] = hi + 1;
            max = Math.max(max, hi + 1);
        }
        
        return max;
	}
	
	public static int[] play(final int N, final int M, LinkedList<Integer> bar_list, LinkedList<Integer> baz_list){
		int[] in = new int[N];
		for(int i = 0; i < N; i++){
			in[i] = i;
		}
		
		Iterator<Integer> bar_iter = bar_list.iterator();
		Iterator<Integer> baz_iter = baz_list.iterator();
	    
		while(true){
			if(!bar_iter.hasNext() || !baz_iter.hasNext()){
        		break;
        	}
        	
            final int bar = bar_iter.next();
            final int baz = baz_iter.next();
            
            int tmp = in[bar];
            in[bar] = in[baz];
            in[baz] = tmp;
        }
		
		return in;
	}
	
	public static boolean check(final int N, int[] result, int[] answer){
		for(int i = 0; i < N; i++){
			if(result[i] != answer[i]){
				return false;
			}
		}
		
		return true;
	}
	
	public static int dfs(final int deep, final int N, final int M, int[] answer, int[][] swap, boolean[] visited, LinkedList<Integer> bar_list, LinkedList<Integer> baz_list){
		if(deep >= M){
			int[] result = play(N, M, bar_list, baz_list);
			if(!check(N, result, answer)){
				return M;
			}
			
			int min = calc(N, M, bar_list, baz_list);
			//System.out.println(Arrays.toString(result) + " : " + min);
			
			return min;
		}else{
			int min = M;
			
			for(int i = 0; i < M; i++){
				if(visited[i]){
					continue;
				}
				
				visited[i] = true;
				bar_list.addLast(swap[i][0]);
				baz_list.addLast(swap[i][1]);
				
				min = Math.min(dfs(deep + 1, N, M, answer, swap, visited, bar_list, baz_list), min);
				
				baz_list.removeLast();
				bar_list.removeLast();
				visited[i] = false;
			}
			
			//System.out.println(min + " " + deep);
			return min;
		}
	}
	
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
         
        final int N = sc.nextInt();
        final int M = sc.nextInt();
        
        boolean[] visited = new boolean[M];
        int[][] swap = new int[M][2];
        
        LinkedList<Integer> bar_list = new LinkedList<Integer>();
        LinkedList<Integer> baz_list = new LinkedList<Integer>();
        
        for(int i = 0; i < M; i++){
        	final int bar = sc.nextInt() - 1;
        	final int baz = bar + 1;
        	
        	swap[i][0] = bar;
        	swap[i][1] = baz;
        	
        	bar_list.addLast(bar);
        	baz_list.addLast(baz);
        }
        
        int[] answer = play(N, M, bar_list, baz_list);
        
       // System.out.println(Arrays.toString(answer));
        
        bar_list.clear();
        baz_list.clear();
        
        System.out.println(dfs(0, N, M, answer, swap, visited, bar_list, baz_list));
        
    }
      
}
