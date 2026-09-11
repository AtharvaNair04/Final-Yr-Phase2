import java.util.*;

public class Main{
	int n;
	HashMap<P, Integer> map;
	HashMap<P, P>pair;
	int [] vx = {0,1,0,-1};
	int [] vy = {1,0,-1,0};
	
	class P {
		int x,y;

		public P(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			P other = (P) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		private Main getOuterType() {
			return Main.this;
		}

		@Override
		public String toString() {
			return "P [x=" + x + ", y=" + y + "]";
		}
		
		
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n = sc.nextInt();
			if(n == 0) break;
			map = new HashMap<P, Integer>();
			pair = new HashMap<Main.P, Main.P>();
			for(int i = 0; i < n; i++){
				int x = sc.nextInt();
				int y = sc.nextInt();
				int dir = sc.next().charAt(0) == 'x' ? 0: 1;
				P p = new P(x, y);
				map.put(p, 0);
				if(dir == 0){
					P pp = new P(x + 1, y);
					map.put(pp, 0);
					pair.put(p, pp);
					pair.put(pp, p);
				}
				else {
					P pp =  new P(x, y + 1);
					map.put(pp, 0);
					pair.put(p, pp);
					pair.put(pp, p);
				}
				
			}
			boolean ans = true;
			for(P nowp: pair.keySet()){
				if(map.get(nowp) == 0){
					boolean res = bfs(nowp);
					if(! res){
						ans = false;
						break;
					}
				}
			}
			System.out.println(ans ? "Yes" : "No");
		}
	}

	private boolean bfs(P argp) {
		LinkedList<P> open = new LinkedList<Main.P>();
		open.add(argp);
		map.put(argp, 1);
		while(! open.isEmpty()){
			P nowp = open.poll();
			int nowvalue = map.get(nowp);
			for(int i = 0; i < 4; i++){
				int xx = nowp.x + vx[i];
				int yy = nowp.y + vy[i];
				P pp = new P(xx, yy);
				if(map.containsKey(pp)){
					int value = map.get(pp);
					if(value == 0){
						if(pair.get(nowp).equals(pp)){
							map.put(pp, nowvalue == 1 ? 2 :1);
							open.add(pp);
						}
						else{
							map.put(pp, nowvalue);
							open.add(pp);
						}
					}
					else{
						if(pair.get(nowp).equals(pp)){
							if(value == nowvalue) return false;
						}
						else{
							if(value != nowvalue) return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void setvalue(P[] p) {
		for(int i = 0; i < 4; i++){
			int xx = p[0].x + vx[i];
			int yy = p[0].y + vy[i];
			P pp = new P(xx, yy);
			if(pp.equals(p[1])) continue;
			if(map.containsKey(pp)){
				int value = map.get(pp);
				if(value != 0){
					map.put(p[0], value);
					map.put(p[1], value == 1 ? 2 : 1);
					return;
				}
			}
		}
		
		for(int i = 0; i < 4; i++){
			int xx = p[1].x + vx[i];
			int yy = p[1].y + vy[i];
			P pp = new P(xx, yy);
			if(pp.equals(p[0])) continue;
			if(map.containsKey(pp)){
				int value = map.get(pp);
				if(value != 0){
					map.put(p[1], value);
					map.put(p[0], value == 1 ? 2 : 1);
					return;
				}
			}
		}
		map.put(p[0], 1);
		map.put(p[1], 2);
	}

	private boolean check(P[] p) {
		for(int j = 0; j < 2; j++){
			int nowvalue = map.get(p[j]);
			for(int i = 0; i < 4; i++){
				int xx = p[j].x + vx[i];
				int yy = p[j].y + vy[i];
				P pp = new P(xx, yy);
				if(pp.equals(p[j^1])) continue;
				if(map.containsKey(pp)){
					int ppvalue = map.get(pp);
					if(ppvalue == 0){
						map.put(pp, nowvalue);
					}
					else if(ppvalue != nowvalue){
						return false;
					}
				}
			}
		}
		
		return true;
	}

	private void debug(Object... o) {
		System.out.println("debug = " + Arrays.deepToString(o));
	}

	public static void main(String[] args) {
		new Main().doit();
	}
}
