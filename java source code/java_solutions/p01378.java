import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//[[iwi]]
public class Main{

	class R{
		int l, r;
		List<R> adj;
		public R(int l, int r) {
			this.l = l;
			this.r = r;
			adj = new ArrayList<R>();
		}
		public void f(int depth){
			max = Math.max(max, depth);
			for(R a:adj){
				a.f(depth+1);
			}
		}
	}
	
	int max;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		char[] s = sc.next().toCharArray();
		int l, r;
		l = r = -1;
		int n = s.length;
		for(int i=0;i<n;i++){
			if(s[i]=='w'){
				l = i-1;
				r = i+2;
				break;
			}
		}
		List<R> list = new ArrayList<R>();
		for(int i=0;i<l;i++){
			char c1 = s[i];
			for(int j=r;j<n;j++){
				if(c1=='('&&s[j]==')'||
						c1==')'&&s[j]=='('||
						c1=='{'&&s[j]=='}'||
						c1=='}'&&s[j]=='{'||
						c1=='['&&s[j]==']'||
						c1==']'&&s[j]=='[')list.add(new R(i,j));
			}
		}
		for(R a:list){
			for(R b:list){
				if(a.l<b.l&&a.r>b.r)a.adj.add(b);
			}
		}
		max = 0;
		for(R a:list)a.f(1);
		System.out.println(2*max+3);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
