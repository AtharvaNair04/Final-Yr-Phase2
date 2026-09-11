import java.util.*;

public class Main {
	int n, k, max;
	int[] c;
	StringBuffer a, b;
	
	
	void loop(int id, int br, int kk){
		if(kk==k+1) return;
		if(id==n){
			int ans = 0;
			int ten = 1;
			for(int i=0;i<n;i++){
				ans += c[i]*ten;
				ten *= 10;
			}
			max = Math.max(max, ans);
			return;
		}
		
		int p = a.charAt(id) - b.charAt(id) - br;
		if(p>=0){
			c[id] = p;
			loop(id+1,0,kk);
		}else{
			c[id] = p+10;
			loop(id+1,1,kk);
			loop(id+1,0,kk+1);
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		
		a = new StringBuffer(sc.next()).reverse();
		b = new StringBuffer(sc.next()).reverse();
		k = sc.nextInt();
		n = a.length();
		int dif = n-b.length();
		for(int i=0;i<dif;i++) b.append(0);
		max = 0;
		c = new int[n];
		loop(0,0,0);
		System.out.println(max);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}

}
