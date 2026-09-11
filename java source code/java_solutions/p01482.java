import java.util.Scanner;

//Memory Leak
public class Main{

	final int NULL = -1, NOT = -2;
	
	class R{
		int mem;
		public R(int mem) {
			this.mem = mem;
		}
	}
	
	class Manager{
		boolean error;
		int maxSize, alloc;
		public Manager(int maxSize) {
			this.maxSize = maxSize;
			alloc = 0;
			error = false;
		}
		R malloc(int s){
			if(maxSize<s+alloc)return new R(NULL);
			alloc+=s;
			return new R(s);
		}
		R clone(R r){
			if(r.mem==NOT){
				error = true;
				return new R(NULL);
			}
			if(r.mem==NULL)return new R(NULL);
			if(maxSize<r.mem+alloc)return new R(NULL);
			alloc+=r.mem;
			return new R(r.mem);
		}
		void free(R r){
			if(r.mem==NOT){
				error = true; return;
			}
			if(r.mem==NULL)return;
			alloc-=r.mem;
		}
	}
	
	R[] r;
	Manager manager;
	char[] s;
	int idx;
	char get(){
		return s[idx++];
	}
	
	void line(){
		if(get()=='f'){
			get();
			R e = exp();
			if(0<=e.mem)for(int i=0;i<26;i++)if(r[i]==e)r[i]=new R(NOT);
			manager.free(e);
			get();
		}
		else{
			idx--;
			exp();
		}
	}
	
	R exp(){
		char ch = get();
		if(ch=='('){
			R r = exp();
			get();
			return r;
		}
		if(ch=='m'){
			get();
			int x = 0;
			char c = get();
			do{
				x = x*10 + c-'0';
				c = get();
			}while(Character.isDigit(c));
			return manager.malloc(x);
		}
		if(ch=='c'){
			get();
			R r = manager.clone(exp());
			get();
			return r;
		}
		char nx = get();
		if(nx=='U'){
			get(); get();
			return new R(NULL);
		}
		if(nx=='='){
			R e = exp();
			if(e.mem<0)return r[ch-'A'] = new R(e.mem);
			return r[ch-'A'] = e;
		}
		idx--;
		return r[ch-'A'];
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		r = new R[26];
		for(int i=0;i<26;i++)r[i]=new R(NOT);
		manager = new Manager(sc.nextInt());
		while(sc.hasNext()){
			idx = 0;
			s = (sc.next().replace("malloc", "m").replace("free", "f").replace("clone", "c")+"$").toCharArray();
			line();
		}
		if(manager.error)System.out.println("Error");
		else{
			int u = 0;
			for(int i=0;i<26;i++){
				boolean ok = true;
				for(int j=0;j<i;j++)if(r[i]==r[j])ok = false;
				if(ok&&0<=r[i].mem)u+=r[i].mem;
			}
			System.out.println(manager.alloc-u);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
