import java.util.*;
public class Main {

	public static boolean isOK(int i, int j) {
		char[] str_i = String.valueOf(i).toCharArray();
		char[] str_j = String.valueOf(j).toCharArray();
		
		return str_i[0] == str_j[str_j.length - 1] && str_i[str_i.length -1] == str_j[0];
	}
	
	public static Integer hash(int n) {
		char[] str = String.valueOf(n).toCharArray();
		if(str[0] == '0' || str[str.length -1] == '0') {
			return -1;
		}
		String a = String.valueOf(new char[] {str[0], str[str.length -1]});
		
		
		return Integer.parseInt(a);
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		Map<Integer, List<Integer>> map = new HashMap<>();
		
		
		for(int i=1; i<=9;i++) {
			for(int j=1; j<=9;j++) {
				map.put(i*10 + j, new ArrayList<Integer>());	
			}
		}
		
		
		for(int i=1; i<=n;i++) {
			int h = hash(i);
			if(h <= 0) {
				continue;
			}
			
			List<Integer> l = map.get(h);
			l.add(i);
			map.put(h, l);
		}
		


		int ans = 0;

		for(int i=1; i<=9; i++) {
			for(int j=1; j<=9; j++) {
				int a = map.get(i*10 + j).size();
				int b = map.get(j*10 + i).size();
				ans += a * b;
				
			}
		}
		
		System.out.println(ans);
		
	}
	


}
