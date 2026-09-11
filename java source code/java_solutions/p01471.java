import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
 
 
public class Main{
	
	public static class Pair implements Comparable<Pair>{
		int w, v;
		
		public Pair(int w, int v) {
			super();
			this.w = w;
			this.v = v;
		}

		@Override
		public int compareTo(Pair o) {
			return this.w * o.v - this.v * o.w;
		}
	}
	
    public static void main(String[] args){
    	final Scanner sc = new Scanner(System.in);
    	
    	final int N = sc.nextInt();
    	final int W = sc.nextInt();
    	
    	PriorityQueue<Pair> queue = new PriorityQueue<Main.Pair>(); 
    	
    	double cur_w = 0, cur_v = 0;
    	
    	for(int i = 0; i < N; i++){
    		int w = sc.nextInt();
    		int v = sc.nextInt();
    		
    		if(w >= 0 && v <= 0){
    			continue;
    		}else if(w <= 0 && v >= 0){
    			cur_w += w;
    			cur_v += v;
    			continue;
    		}else if(w <= 0 && v <= 0){
    			cur_w += w;
    			cur_v += v;
    			
    			w = -w;
    			v = -v;
    		}
    		
    		queue.add(new Pair(w, v));
    	}
    	
    	
    	while(!queue.isEmpty()){
    		Pair p = queue.poll();
    		
    		final double rest = W - cur_w;
    		final double x = Math.max(0, Math.min(1, rest / p.w));
    		
    		//System.out.println(cur_w + " "+ rest + " "+ p.v + " " + p.w + " " + x);
    		
    		cur_w += x * p.w;
    		cur_v += x * p.v;
    	}
    	
    	System.out.printf("%f\n", cur_v);
    }
     
}
