import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//Colored Octahedra
public class Main{

	int id, res;
	Map<String, Integer> ref;
	int[] c;
	void reg(String s){
		if(ref.containsKey(s))c[ref.get(s)]++;
		else{
			c[id]++;
			ref.put(s, id++);
		}
	}
	
	int[][] d = {
			{0, 1, 2, 3, 4, 5, 6, 7},
			{4, 5, 1, 0, 7, 6, 2, 3},
			{4, 0, 3, 7, 5, 1, 2, 6},
			{1, 5, 6, 2, 0, 4, 7, 3},
			{5, 4, 7, 6, 1, 0, 3, 2},
			{3, 2, 6, 7, 0, 1, 5, 4}
	};
	
	char[] s;
	Set<String> u;
	void dfs(int k){
		if(k==8){
			String t = new String(s);
			if(u.contains(t))return;
			res++;
			for(int i=0;i<6;i++){
				String a = "", b = "";
				for(int j=0;j<4;j++)a+=t.charAt(d[i][j]);
				for(int j=4;j<8;j++)b+=t.charAt(d[i][j]);
				for(int j=0;j<4;j++){
					u.add(a+b);
					a = a.substring(1)+a.charAt(0); b = b.substring(1)+b.charAt(0);
				}
			}
			return;
		}
		for(int i=0;i<id;i++){
			if(c[i]==0)continue;
			s[k] = (char)(i+'0');
			c[i]--;
			dfs(k+1);
			c[i]++;
		}
	}
	
	
	void run(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			id = res = 0;
			ref = new HashMap<String, Integer>();
			c = new int[8];
			for(int i=0;i<8;i++)reg(sc.next());
			u = new HashSet<String>();
			s = new char[8];
			dfs(0);
			System.out.println(res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
