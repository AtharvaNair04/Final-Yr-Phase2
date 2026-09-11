import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//Dr. Podboq or: How We Became Asymmetric
public class Main{

	double EPS = 1e-8;
	class R{
		String rep;
		Set<String> sub;
		int id, left, right, num, h;
		double rate;
		public R(int id) {
			this.id = id;
			rate = left = right = -1;
			rep = "";
			h = num = 1;
			sub = new HashSet<String>();
		}
		void f(boolean isLeft){
			if(left==-1)return;
			boolean leftStrong = comp(left, right)<=0;
			if(leftStrong && !isLeft || !leftStrong && isLeft){
				int t = left;
				left = right;
				right = t;
			}
			rs[left].f(true);
			rs[right].f(false);
		}
		int comp(int v1, int v2){
			if(Math.abs(rs[v1].rate-rs[v2].rate)>EPS){
				return rs[v1].rate < rs[v2].rate?-1:1;
			}
			if(rs[v1].left==-1 && rs[v2].left==-1)return 0;
			int l1 = rs[v1].left, r1 = rs[v1].right, l2 = rs[v2].left, r2 = rs[v2].right;
			if(comp(l1, r1)==1){
				int t = l1;
				l1 = r1; r1 = t;
			}
			if(comp(l2, r2)==1){
				int t = l2;
				l2 = r2; r2 = t;
			}
			int c = comp(l1, l2);
			if(c!=0){
				return c;
			}
			return comp(r1, r2);
		}
		void p(){
			if(left==-1)System.out.print("x");
			else{
				System.out.print("(");
				rs[left].p();
				System.out.print(" ");
				rs[right].p();
				System.out.print(")");
			}
		}
		void proc(){
			if(left==-1){
				rate = 0;
				h = 1;
				rep = "x";
				sub.add(rep);
				return;
			}
			rs[left].proc();
			rs[right].proc();
			sub.addAll(rs[left].sub); sub.addAll(rs[right].sub);
			int c = 0;
			for(String s:sub)if(rs[left].sub.contains(s) && rs[right].sub.contains(s))c++;
			rate = 1.*c/sub.size();
			num+=rs[left].num+rs[right].num;
			if(rs[left].h < rs[right].h || 
					rs[left].h==rs[right].h&&rs[left].num < rs[right].num ||
					rs[left].h==rs[right].h&&rs[left].num==rs[right].num&&rs[left].rep.compareTo(rs[right].rep)>0){
				int t = left;
				left = right;
				right = t;
			}
			h = Math.max(rs[left].h, rs[right].h)+1;
			rep = "("+rs[left].rep+" "+rs[right].rep+")";
			sub.add(rep);
		}
	}

	R[] rs;
	
	int idx, ID;
	char[] s;
	char get(){
		return s[idx++];
	}
	
	int tree(){
		int res = ID++;
		rs[res] = new R(res);
		char ch = get();
		if(ch=='x'){
			return res;
		}
		rs[res].left = tree();
		get();
		rs[res].right = tree();
		get();
		return res;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			s = sc.nextLine().toCharArray();
			if(s[0]=='0')break;
			idx = ID = 0;
			rs = new R[128];
			tree();
			rs[0].proc();
			rs[0].f(true);
			rs[0].p();
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
