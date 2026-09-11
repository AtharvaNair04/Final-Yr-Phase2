import java.util.Scanner;

//Spiral Footrace
public class Main{

	class P{
		int x, y;
		public P(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	
	void run(){
		double EPS = 1e-6;
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n==0)break;
			P[] p = new P[n];
			for(int i=0;i<n;i++)p[i]=new P(sc.nextInt(), sc.nextInt());
			boolean[] u = new boolean[n];
			P t = new P(0, 0);
			P h = new P(0, 1);
			P pre = new P(0, 0);
			double res = 0;
			for(int i=0;i<n;i++){
				int id = -1;
				double min = 360;
				double d = 1<<29;
				for(int j=0;j<n;j++){
					if(u[j])continue;
					double dist = Math.hypot(p[j].x-pre.x, p[j].y-pre.y);
					double x1 = p[j].x-pre.x;
					double y1 = p[j].y-pre.y;
					double x2 = h.x-t.x;
					double y2 = h.y-t.y;
//					double thita = Math.acos((x1*x2+y1*y2)/(Math.hypot(x1, y1)*Math.hypot(x2, y2)));
					double thita = Math.atan2(x1*y2-x2*y1, x1*x2+y1*y2);
//					System.out.println("ID:" + j + " THIA:" + thita);
					if(thita+EPS<min){
						id = j;
						min = thita;
						d = dist;
					}
					else if(Math.abs(thita-min)<EPS&&dist<d){
						id = j;
						d = dist;
					}
				}
//				System.out.println("P: (" +p[id].x+","+p[id].y+")" + " D:" + d);
				u[id] = true;
				res += d;
				pre = p[id];
				if(i==0){
					h = p[id];
				}
				else{
					t = h;
					h = p[id];
				}
			}
			System.out.printf("%.1f\n", res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
