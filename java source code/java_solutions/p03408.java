import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);

		int n = stdIn.nextInt();

		Map<String,Integer> map1 = new HashMap<>();
		Map<String,Integer> map2 = new HashMap<>();





		for(int i = 0; i < n; i++) {
			String s = stdIn.next();

			if(map1.containsKey(s)) {
				map1.put(s,map1.get(s)+1);
			}else {
				map1.put(s,1);
			}
		}

		int m = stdIn.nextInt();
		for(int i = 0; i < m; i++) {
			String t = stdIn.next();

			if(map2.containsKey(t)) {
				map2.put(t,map2.get(t)+1);
			}else {
				map2.put(t,1);
			}
		}

		int ans = 0;

		for(String mp1: map1.keySet()) {
			boolean flag = false;
			int cnt = 0;
			for(String mp2: map2.keySet()) {

				if(mp1.equals(mp2)) {
					flag = true;
					cnt = map1.get(mp1) - map2.get(mp2);
				}


			}

			if(!flag) {
				ans = Math.max(map1.get(mp1), ans);
			}else {
				ans = Math.max(ans, cnt);
			}


		}

		System.out.println(ans);

	}

}
