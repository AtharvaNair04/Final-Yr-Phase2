import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int F = sc.nextInt();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		ArrayList<String> list = new ArrayList<String>();
		String[][] str = new String[N][];
		for(int i = 0; i < N; i++) {
			int M = sc.nextInt();
			str[i] = new String[M];
			for(int j = 0; j < M; j++) {
				str[i][j] = sc.next();
				if(map.containsKey(str[i][j])) continue;
				else {
					map.put(str[i][j], list.size());
					list.add(str[i][j]);
				}
				
			}
		}
		int[][] num = new int[list.size()][list.size()];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < str[i].length; j++) {
				for(int k = j+1; k < str[i].length; k++) {
					if(j == k) continue;
					num[map.get(str[i][j])][map.get(str[i][k])]++;
					num[map.get(str[i][k])][map.get(str[i][j])]++;
				}
			}
		}
		ArrayList<Data> l = new ArrayList<Data>();
		for(int i = 0; i < list.size(); i++) {
			for(int j = i+1; j < list.size(); j++) {
				if(num[i][j] >= F) {
					l.add(new Data(list.get(i),list.get(j)));
				}
			}
		}
		Collections.sort(l);
		System.out.println(l.size());
		for(int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i).a + " " + l.get(i).b);
		}
	}
	static class Data implements Comparable<Data> {
		String a;
		String b;
		Data(String a, String b) {
			String[] kk = {a,b};
			Arrays.sort(kk);
			this.a = kk[0];
			this.b = kk[1];
		}
		@Override
		public int compareTo(Data o) {
			if(o.a.equals(this.a)) return -o.b.compareTo(this.b);
			return -o.a.compareTo(this.a);
		}
	}
}
