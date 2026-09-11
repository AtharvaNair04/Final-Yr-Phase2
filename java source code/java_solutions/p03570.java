import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class Main {
	
	static class Scanner{
		BufferedReader br=null;
		StringTokenizer tk=null;
		public Scanner(){
			br=new BufferedReader(new InputStreamReader(System.in));
		}
		public String next() throws IOException{
			while(tk==null || !tk.hasMoreTokens()) {
				String cad = br.readLine();
				if (cad == null)
					return null;
				tk=new StringTokenizer(cad);
			}
			return tk.nextToken();
		}
		public int nextInt() throws NumberFormatException, IOException{
			return Integer.valueOf(next());
		}
		public long nextLong() throws NumberFormatException, IOException{
			return Long.valueOf(next());
		}
		public double nextDouble() throws NumberFormatException, IOException{
			return Double.valueOf(next());
		}
	}
	
	
	static class State {
		int mask;
		int tam;
		public State(int[] count) {
			mask = 0;
			tam = count.length;
			for(int i = 0; i < tam; i++)
				if (count[i] % 2 == 1)
					mask = mask | (1<<i);
		}
		public State(int m, int t) {
			mask = m;
			tam = t;
		}
		LinkedList<Integer> generateNextStates() {
			LinkedList<Integer> ret = new LinkedList<Integer>();
			ret.add(mask);
			for(int i = 0; i < tam; i++) {
				int nmask = mask ^ (1<<i);
				ret.add(nmask);
			}
			return ret;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + mask;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (mask != other.mask)
				return false;
			return true;
		}
	}
	
	static char[] s;
	
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner();
		s = sc.next().toCharArray();
		int[] ans = new int[s.length];
		int[] count = new int[27];
		Arrays.fill(count, 0);
		HashMap<State, Integer> map = new HashMap<State, Integer>();
		State cstate = new State(count);
		map.put(cstate, 0);
		int idx = 0;
		for(char c: s) {
			count[c - 'a']++;
			State cs = new State(count);
			LinkedList<Integer> ns = cs.generateNextStates();
			int current_value = Integer.MAX_VALUE;
			for(int m: ns) {
				State tmp = new State(m, count.length);
				if (map.containsKey(tmp))
					current_value = Math.min(current_value, map.get(tmp) + 1);
			}
			ans[idx] = current_value;
			if (!map.containsKey(cs) || map.get(cs) > current_value)
				map.put(cs, current_value);
			idx++;
		}
		System.out.println(ans[s.length - 1]);
	}

}
