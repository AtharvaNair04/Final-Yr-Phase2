import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
 
 
public class Main{
	
	public static final int SIZE = 4;
	public static final int LEN  = 8;
	
	public static boolean is_ok(int x, int y){
		if(x < 0 || y < 0 || x >= SIZE || y >= SIZE){
			return false;
		}else{
			return true;
		}
	}
	
	public static class Trie {
		int score;
		Trie[] next;
		int id;
		
		private Trie(){
			this.score = -1;
			next = new Trie[26];
		}
		
		public void register_word(int deep, int id, char[] word_ch, int score){
			if(word_ch.length == deep){
				this.score = score;
				this.id = id;
				return;
			}else{
				final int index = word_ch[deep] - 'A';
				
				if(next[index] == null){
					next[index] = new Trie();
				}
				next[index].register_word(deep + 1, id, word_ch, score);
			}
		}
		
		public static void register(Trie root, int id, String word, int score){
			root.register_word(0, id, word.toCharArray(), score);
		}
		
		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			
			if(score >= 0){
				sb.append(score + " ");
			}
			
			for(int index = 0; index < 26; index++){
				if(next[index] == null){
					continue;
				}
					
				sb.append("[ " + (char)(index + 'A') + " : " + next[index].toString() + " ], ");
			}
			
			
			return sb.toString();
		}
		
		public boolean hasNext(char c){
			return next[(int)(c - 'A')] != null;
		}
		
		public boolean isWord(){
			return score >= 0;
		}
		
		public Trie getNext(char c){
			return next[(int)(c - 'A')];
		}
	}
	
	public static void dfs(final int x, final int y, int bit, char[][] inputs, Trie dict, int[] counts){
		//System.out.println(bit);
		//System.out.println(dict);
		if(dict.isWord()){
			counts[dict.id]++;
			//benefit[bit] = Math.max(benefit[bit], dict.score);
		}
		
		//System.out.println(y + " "  + x);
		
		for(int dy = -1; dy <= 1; dy++){
			for(int dx = -1; dx <= 1; dx++){
				if(dx == 0 && dy == 0){
					continue;
				}
				
				final int nx = x + dx;
				final int ny = y + dy;
				
				final int next_addr = ny * SIZE + nx;
				if(!is_ok(nx, ny)){
					continue;
				}else if((bit & (1 << next_addr)) != 0){
					continue;
				}else if(!dict.hasNext(inputs[ny][nx])){
					continue;
				}
				
				dfs(nx, ny, bit | (1 << next_addr), inputs, dict.getNext(inputs[ny][nx]), counts);
			}
		}
	}
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	final int N = sc.nextInt();
    	
    	Trie dict = new Trie();
    	
    	int[] times  = new int[N];
    	int[] scores = new int[N];
    	int[] counts = new int[N];
    	
    	for(int i = 0; i < N; i++){
    		final String word = sc.next();
    		final int score = sc.nextInt();
    		
    		times[i] = word.length();
    		scores[i] = score;
    		Trie.register(dict, i, word, score);
    		
    		//System.out.println(word);
    		//System.out.println(dict);
    	}
    	//System.out.println(dict);
    	
    	char[][] inputs = new char[SIZE][SIZE];
    	for(int i = 0; i < SIZE; i++){
    		inputs[i] = sc.next().toCharArray();
    		//System.out.println(i);
    	}
    	
    	final int T = sc.nextInt();
    	
    	/*
    	final int MAX_BIT = 1 << (SIZE * SIZE);
    	int[] benefits = new int[MAX_BIT];
    	Arrays.fill(benefits, -1);
    	*/
    	//System.out.println("T");
    	
    	for(int y = 0; y < SIZE; y++){
    		for(int x = 0; x < SIZE; x++){
    			if(!dict.hasNext(inputs[y][x])){
    				continue;
    			}
    			
    			//System.out.println(x + " : " + y);
    			final int addr = y * SIZE + x;
    			dfs(x, y, 1 << addr, inputs, dict.getNext(inputs[y][x]), counts);
    			//System.out.println(x + " : " + y);
    		}
    	}
    	//System.out.println("B");
    	
    	//System.err.println(times + " " + scores);
    	
    	//System.out.println(scores);
    	
    	int[] DP = new int[T + 1];
    	Arrays.fill(DP, -1);
    	DP[0] = 0;
    	
    	int max = 0;
    	for(int index = 0; index < counts.length; index++){
    		if(counts[index] == 0){
    			continue;
    		}
    		//System.out.println(counts[index]);
    		
    		for(int exp = 1; counts[index] > 0; exp *= 2){
    			final int mul = Math.min(exp, counts[index]);
    			
    			for(int time = T; time >= 0; time--){
    				if(DP[time] < 0){
    					continue;
    				}
    			
    				final int next_score = DP[time] + (scores[index] * mul);
    				final int next_time  = time + (times[index] * mul);
    			
    				if(next_time > T){
    					continue;
    				}
    			
    				if(DP[next_time] < next_score){
    					DP[next_time] = next_score;
    					max = Math.max(max, DP[next_time]);
    				}
    			}
    			
    			counts[index] -= mul;
    		}
    	}
    	
    	System.out.println(max);
    	
    	//System.out.println(Arrays.toString(DP));
    	
    }
     
}
