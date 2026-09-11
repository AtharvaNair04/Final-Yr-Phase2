import java.util.*;

public class Main {
	static int judge(String name, String org){
		for(int i=1; i<org.length(); i++){
			for(int k=0; k<i; k++){
				StringBuilder ename = new StringBuilder();
				int interval = k;
				while(interval < org.length()){
					ename.append(org.charAt(interval));
					interval += i;
				}
				if(ename.indexOf(name) >= 0)	return 1;
				
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		String name = s.next();
		int cnt =0;
		
		for(int i=0; i<n; i++){
			cnt += judge(name, s.next());
		}
		System.out.println(cnt);
	}
}
