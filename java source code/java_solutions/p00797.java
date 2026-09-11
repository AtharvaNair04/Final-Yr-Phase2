import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//Family Tree
public class Main{

	class V{
		int id;
		String name;
		V parent;
		List<V> adj;
		public V(int id, String name) {
			this.id = id;
			this.name = name;
			parent = null;
			adj = new ArrayList<V>();
		}
		void add(int depth, String name, int id){
			if(depth==0){
				V t = new V(id, name);
				t.parent = this;
				adj.add(t);
				v[id] = t;
			}
			else{
				adj.get(adj.size()-1).add(depth-1, name, id);
			}
		}
		boolean isChild(String s){
			for(V t:adj)if(t.name.equals(s))return true;
			return false;
		}
		boolean isParent(String s){
			if(parent==null)return false;
			return parent.name.equals(s);
		}
		boolean isSibling(String s){
			if(parent==null)return false;
			for(V t:parent.adj)if(t.name.equals(s))return true;
			return false;
		}
		boolean isDesc(String s){
			for(V t:adj){
				if(t.name.equals(s))return true;
				if(t.isDesc(s))return true;
			}
			return false;
		}
		boolean isAnc(String s){
			if(parent==null)return false;
			if(parent.name.equals(s))return true;
			return parent.isAnc(s);
		}
	}
	
	V[] v;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if((n|m)==0)break;
			v = new V[n+1];
			v[0] = new V(0, "");
			sc.nextLine();
			Map<String, Integer> ref = new HashMap<String, Integer>();
			for(int i=1;i<=n;i++){
				String s = sc.nextLine();
				int depth = 0;
				while(s.charAt(depth)==' ')depth++;
				String name = s.substring(depth, s.length());
				v[0].add(depth, name, i);
				ref.put(name, i);
			}
			while(m--!=0){
				String[] s = sc.nextLine().split(" ");
				String a = s[0];
				String cmd = s[3];
				String b = s[5].substring(0, s[5].length()-1);
				boolean f;
				if("child".equals(cmd))f = v[ref.get(b)].isChild(a);
				else if("parent".equals(cmd))f = v[ref.get(b)].isParent(a);
				else if("sibling".equals(cmd))f = v[ref.get(b)].isSibling(a);
				else if("descendant".equals(cmd))f = v[ref.get(b)].isDesc(a);
				else f = v[ref.get(b)].isAnc(a);
				System.out.println(f?"True":"False");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
