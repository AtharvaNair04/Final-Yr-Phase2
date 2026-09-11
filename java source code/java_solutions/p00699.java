import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Main{

	Scanner sc=new Scanner(System.in);

	int T;
	final int n=5;
	int[][] a;

	void run(){
		T=sc.nextInt();
		for(int k=0; k<T; k++){
			a=new int[n][n];
			for(int j=0; j<n; j++){
				for(int i=0; i<n; i++){
					a[j][i]=sc.nextInt();
				}
			}
			solve();
			println(yes+"");
		}
	}

	class P{
		int x, y;

		P(int x, int y){
			this.x=x;
			this.y=y;
		}

		P add(P p){
			return new P(x+p.x, y+p.y);
		}

		public String toString(){
			return "("+x+","+y+")";
		}
	}

	boolean yes;

	void solve(){
		P[] ps=new P[6];
		yes=true;
		for(int j=0; j<n; j++){
			for(int i=0; i<n; i++){
				if(a[j][i]!=0){
					if(ps[a[j][i]-1]!=null){
						yes=false;
						return;
					}else{
						ps[a[j][i]-1]=new P(i, j);
					}
				}
			}
		}
		for(int i=0; i<6; i++){
			if(ps[i]==null){
				yes=false;
				return;
			}
		}
		// 1~6ツつェ6ツづ?
		// ツ連ツ個仰青ォ
		int[][] w=new int[6][6];
		for(int j=0; j<6; j++){
			for(int i=0; i<j; i++){
				if(abs(ps[i].x-ps[j].x)+abs(ps[i].y-ps[j].y)==1){
					w[i][j]=w[j][i]=0;
				}else{
					w[i][j]=w[j][i]=1;
				}
			}
			w[j][j]=0;
		}
		for(int k=0; k<6; k++){
			for(int i=0; i<6; i++){
				for(int j=0; j<6; j++){
					w[i][j]=min(w[i][j], w[i][k]+w[k][j]);
				}
			}
		}
		boolean connected=true;
		for(int j=0; j<6; j++){
			for(int i=0; i<6; i++){
				connected&=w[i][j]==0;
			}
		}
		// debug("connected", connected);
		if(!connected){
			yes=false;
			return;
		}
		// ツ連ツ個仰つセツつィ

		@SuppressWarnings("unchecked")
		ArrayList<P>[] lists=new ArrayList[7];

		for(int i=0; i<7; i++){
			lists[i]=new ArrayList<P>();
		}

		lists[0].add(new P(1, 0));
		lists[0].add(new P(2, 0));

		for(int j=1; j<7; j++){
			int len=(j+1)/2;
			lists[j].add(new P(1, 0));
			for(int i=1; i<=len; i++){
				if(j%2==0){
					lists[j].add(new P(1, i));
				}else{
					lists[j].add(new P(1, -i));
				}
			}
			if(j%2==0){
				lists[j].add(new P(2, len));
			}else{
				lists[j].add(new P(2, -len));
			}
			// debug(lists[j]);
		}

		for(int i=0; i<6; i++){
			P p=ps[i];
			ArrayList<P> os=new ArrayList<P>();
			for(ArrayList<P> list : lists){
				// debug("list", list);
				for(int rot=0; rot<4; rot++){
					// debug("rot", rot);
					// listツづ罫otツづ可づつつ「ツづ?
					P last=null;
					boolean ok=true;
					for(int k=0; k<list.size(); k++){
						P q=ps[i].add(rot(list.get(k), rot));
						// debug("k", k, q);
						if(q.x>=0&&q.x<n&&q.y>=0&&q.y<n&&a[q.y][q.x]!=0){
							if(k==list.size()-1){
								last=q;
							}
						}else{
							ok=false;
							break;
						}
					}
					if(ok){
						os.add(last);
					}
				}
			}
			// debug(i, p, os);
			if(os.size()==1){
				P q=os.get(0);
				if(a[p.y][p.x]+a[q.y][q.x]==7){}else{
					yes=false;
					return;
				}
			}else{
				yes=false;
				return;
			}
		}
	}

	P rot(P p, int rot){
		if(rot==0){
			return new P(p.x, p.y);
		}else if(rot==1){
			return new P(-p.y, p.x);
		}else if(rot==2){
			return new P(-p.x, -p.y);
		}else{
			return new P(p.y, -p.x);
		}
	}

	void debug(Object... os){
		System.err.println(deepToString(os));
	}

	void print(String s){
		System.out.print(s);
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}

}
