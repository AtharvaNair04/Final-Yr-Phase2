import java.util.ArrayList;
import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main{
	public static char[] s;
	public static int[] table;
	public static int n;
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		while(true){
			s = in.next().toCharArray();
			if(s[0] == '#') break;
			int alps = 0;
			for(int i=0; i<s.length; i++){
				if(s[i] < 'a' || 'z' < s[i]) continue;
				alps |= 1<<s[i]-'a';
			}
			n = Integer.bitCount(alps);
			table = new int[n];
			int idx = 0;
			for(int i=0; i<15; i++){
				if((alps&1<<i)==0) continue;
				table[idx++] = i;
			}
			System.out.println(dfs(0, 0) ? "YES" : "NO");
		}
	}
	
	public static boolean dfs(int idx, int state){
		if(idx == n){
			if(!check(state))
				return false;
			return true;
		}
		if(!dfs(idx+1, state|1<<table[idx]) || !dfs(idx+1, state))
			return false;
		return true;
	}
	public static int idx;
	public static boolean check(int state){
		idx = 0;
		st = state;
		boolean val = formula();
		idx++;
		return val==formula();
	}
	public static int st;
	public static boolean[][] arr = {
		{true, true},
		{false,true}
	};
	public static boolean formula(){
		if(s[idx] == '-'){
			idx++;
			return !formula();
		}else if(s[idx] == '('){
			idx++;
			boolean val = formula();
			if(s[idx] == '*'){
				idx++;
				val = val&formula();
			}else if(s[idx] == '+'){
				idx++;
				val = val|formula();
			}else{
				idx+=2;
				val = arr[val?1:0][formula()?1:0];
			}
			idx++;
			return val;
		}else if(s[idx] == 'T'){
			idx++;
			return true;
		}
		else if(s[idx] == 'F'){
			idx++;
			return false;
		}
		return (st&1<<s[idx++]-'a')>0;
	}
	
}
