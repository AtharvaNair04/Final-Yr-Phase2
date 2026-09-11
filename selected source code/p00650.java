import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//The House of Huge Family
public class Main{

	class E{
		int s, t, c, id;
		public E(int s, int t, int c, int id) {
			this.s = s;
			this.t = t;
			this.c = c;
			this.id = id;
		}
	}
	
	int n, m;
	List<E>[] adj;
	
	boolean isCon(int f1, int f2){
		boolean[] u = new boolean[n];
		List<Integer> l = new ArrayList<Integer>();
		l.add(0); u[0] = true;
		int N = 0;
		while(!l.isEmpty()){
			List<Integer> next = new ArrayList<Integer>();
			for(int i:l){
				N++;
				for(E e:adj[i])if(!u[e.t]&&e.id!=f1&&e.id!=f2){
					u[e.t] = true; next.add(e.t);
				}
			}
			l = next;
		}
		return N==n;
	}
	
	@SuppressWarnings("unchecked")
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			n = sc.nextInt(); m = sc.nextInt();
			if((n|m)==0)break;
			adj = new List[n];
			for(int i=0;i<n;i++)adj[i]=new ArrayList<E>();
			int N = 0, M = 0;
			List<Integer> cost = new ArrayList<Integer>();
			while(m--!=0){
				int s = sc.nextInt(), t = sc.nextInt(), c = sc.nextInt();
				if(c<=0)M+=c;
				else{
					cost.add(c); adj[s].add(new E(s, t, c, N)); adj[t].add(new E(t, s, c, N)); N++;
				}
			}
			int min = Integer.MAX_VALUE;
			if(!isCon(-1, -1))min = 0;
			for(int i=0;i<N;i++)if(!isCon(i, -1))min = Math.min(min, cost.get(i));
			for(int i=0;i<N;i++)for(int j=i+1;j<N;j++)min = Math.min(min, cost.get(i)+cost.get(j));
			System.out.println(M+min);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
