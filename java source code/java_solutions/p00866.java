import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//Stopped Watches
public class Main{

	class R implements Comparable<R>{
		int h, m, s;
		public R(int h, int m, int s) {
			this.h = h;
			this.m = m;
			this.s = s;
		}
		public int compareTo(R o) {
			return h!=o.h?h-o.h:m!=o.m?m-o.m:s-o.s;
		}
		public int val(){
			return 3600*h+60*m+s;
		}
		@Override
		public boolean equals(Object obj) {
			R r = (R)obj;
			return h==r.h&&m==r.m&&s==r.s;
		}
		@Override
		public String toString() {
			return String.format("%02d:%02d:%02d", h, m, s);
		}
	}

	@SuppressWarnings("unchecked")
	void run(){
		Scanner sc = new Scanner(System.in);
		int[][] p = {{0,1,2},{0,2,1},{1,0,2},{1,2,0},{2,0,1},{2,1,0}};
		while(true){
			int n = sc.nextInt();
			if(n==0)break;
			int[][] a = new int[n][3];
			for(int i=0;i<n;i++)for(int j=0;j<3;j++)a[i][j]=sc.nextInt();
			List<R>[] l = new ArrayList[n];
			for(int i=0;i<n;i++){
				l[i] = new ArrayList<R>();
				for(int d=0;d<60;d++){
					for(int k=0;k<6;k++){
						int h = (a[i][p[k][0]]+d)%60;
						int m = (a[i][p[k][1]]+d)%60;
						int s = (a[i][p[k][2]]+d)%60;
						if(h%5!=m/12)continue;
						R r = new R(h/5, m, s);
						if(!l[i].contains(r))l[i].add(r);
					}
				}
				Collections.sort(l[i]);
			}
			int min = 3600*12;
			R s, e;
			s = null;
			e = null;
			for(int i=0;i<n;i++){
				for(R r:l[i]){
					int m = 0;
					R end = null;
					for(int j=0;j<n;j++){
						if(i==j)continue;
						boolean exist = false;
						for(R t:l[j]){
							if(t.compareTo(r)>=0){
								exist = true;
								int v = t.val()-r.val();
								if(m<=v){
									m = v;
									end = t;
								}
								break;
							}
						}
						if(!exist){
							end = null;
							break;
						}
					}
					if(end==null)continue;
					if(m<min){
						min = m;
						s = r;
						e = end;
					}
					else if(m==min&&r.compareTo(s)<0){
						s = r;
						e = end;
					}
				}
			}
			System.out.println(s+" "+e);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
