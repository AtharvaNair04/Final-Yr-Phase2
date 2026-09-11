import static java.lang.System.*;
import java.util.*;

class UnionFind {
    int[] par;
    int[] num;

    UnionFind(int n) { 
    	par = new int[n];
    	num = new int[n];
        for(int i = 0; i < n; i++) {
        	par[i] = i;
        	num[i] = 1;
        }
    }

    int root(int x) {
        if (par[x] == x) return x;
        return par[x] = root(par[x]);
    }

    void unite(int x, int y) {
        int rx = root(x); 
        int ry = root(y);
        if (rx == ry) return; 

        par[rx] = ry;
        num[ry] += num[rx];
    }

    boolean same(int x, int y) {
        int rx = root(x);
        int ry = root(y);
        return rx == ry;
    }

    int getNum(int n) {
    	return num[root(n)];
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(in);

        int n = sc.nextInt();
		int m = sc.nextInt();
		
		int[] count = new int[n];
		ArrayList<ArrayList<Integer>> sides = new ArrayList<ArrayList<Integer>>();
		for(int i=0; i<n; i++) sides.add(new ArrayList<Integer>());
		for(int i=0; i<m; i++) {
			int a = sc.nextInt()-1;
			int b = sc.nextInt()-1;
			
			count[a]++;
			count[b]++;
			sides.get(a).add(b);
			sides.get(b).add(a);		
		}
		
		boolean even = true;
		boolean six = false;
		int branch = 0;
		for(int i=0; i<n; i++) {
			even &= ((count[i]%2)==0);
			six |= count[i] >= 6;
			branch += count[i]-2;
		}
		if(branch >= 6) six = true;

		if(even && !six && branch == 4) {
			UnionFind uf = new UnionFind(n);
			for(int i=0; i<n; i++) {
				if(sides.get(i).size() == 2 && sides.get(sides.get(i).get(0)).size() == 2) 
					uf.unite(i, sides.get(i).get(0));
				if(sides.get(i).size() == 2 && sides.get(sides.get(i).get(1)).size() == 2) 
					uf.unite(i, sides.get(i).get(1));
			}
			
			ArrayList<Integer> mid = new ArrayList<Integer>();
			for(int i=0; i<n; i++) {
				if(sides.get(i).size() == 4) {
					if(uf.same(sides.get(i).get(0), sides.get(i).get(1))) {
						mid.add(sides.get(i).get(2));
						mid.add(sides.get(i).get(3));
					}
					else if(uf.same(sides.get(i).get(0), sides.get(i).get(2))) {
						mid.add(sides.get(i).get(1));
						mid.add(sides.get(i).get(3));
					}						
					else if(uf.same(sides.get(i).get(0), sides.get(i).get(3))) {
						mid.add(sides.get(i).get(1));
						mid.add(sides.get(i).get(2));
					}						
					else if(uf.same(sides.get(i).get(1), sides.get(i).get(2))) {
						mid.add(sides.get(i).get(0));
						mid.add(sides.get(i).get(3));
					}						
					else if(uf.same(sides.get(i).get(1), sides.get(i).get(3))) {
						mid.add(sides.get(i).get(0));
						mid.add(sides.get(i).get(2));
					}
					else if(uf.same(sides.get(i).get(2), sides.get(i).get(3))) {
						mid.add(sides.get(i).get(0));
						mid.add(sides.get(i).get(1));
					}						
				}	
			}
			if(mid.size() == 4) {
				if(uf.same(mid.get(0), mid.get(2)) && uf.same(mid.get(1), mid.get(3)) || 
			       uf.same(mid.get(0), mid.get(3)) && uf.same(mid.get(1), mid.get(2))) 
					six = true;
			}
		}
		
		out.println(even && six ? "Yes": "No");
    }
}
