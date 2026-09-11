import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Main{
	Scanner sc=new Scanner(System.in);

	int INF=1<<28;
	double EPS=1e-12;

	int w, d, h;
	int n, m=3;
	int[][][][] a;

	void run(){
		for(;;){
			w=sc.nextInt();
			d=sc.nextInt();
			h=sc.nextInt();
			n=sc.nextInt();
			if((w|d|h|n)==0){
				break;
			}
			int sum=0;
			a=new int[n][m][m][m];
			for(int t=0; t<n; t++){
				int x=sc.nextInt(), y=sc.nextInt(), z=sc.nextInt();
				for(int k=0; k<z; k++){
					for(int j=0; j<y; j++){
						String s=sc.next();
						for(int i=0; i<x; i++){
							a[t][k][j][i]=s.charAt(i)=='*'?1:0;
							sum+=a[t][k][j][i];
						}
					}
				}
			}
			solve();
		}
	}

	long start=0;

	void solve(){
		// a[t]を回転させたのを作りたい
		HashSet<String> set=new HashSet<String>();
		ArrayList<ArrayList<Integer>> lists=new ArrayList<ArrayList<Integer>>();
		int cal=0;
		long start, end;
		start=System.nanoTime();
		for(int t=0; t<n; t++){
			set.clear();
			for(int rotX=0; rotX<4; rotX++){
				rotX(a[t]);
				for(int rotY=0; rotY<4; rotY++){
					rotY(a[t]);
					for(int rotZ=0; rotZ<4; rotZ++){
						rotZ(a[t]);
						int minX=m, maxX=-1, minY=m, maxY=-1, minZ=m, maxZ=-1;
						for(int x=0; x<m; x++){
							for(int y=0; y<m; y++){
								for(int z=0; z<m; z++){
									if(a[t][z][y][x]==1){
										minX=min(minX, x);
										maxX=max(maxX, x);
										minY=min(minY, y);
										maxY=max(maxY, y);
										minZ=min(minZ, z);
										maxZ=max(maxZ, z);
									}
								}
							}
						}
						for(int dx=-w; dx<=w; dx++){
							for(int dy=-d; dy<=d; dy++){
								for(int dz=-h; dz<=h; dz++){
									if(minX+dx>=0&&maxX+dx<w&&minY+dy>=0
											&&maxY+dy<d&&minZ+dz>=0&&maxZ+dz<h){}else{
										continue;
									}
									cal++;
									StringBuilder sb=new StringBuilder();
									ArrayList<Integer> is=new ArrayList<Integer>();
									for(int x=0; x<m; x++){
										for(int y=0; y<m; y++){
											for(int z=0; z<m; z++){
												if(a[t][z][y][x]==1){
													cal++;
													int p=(z+dz)*d*w+(y+dy)*w
															+(x+dx);
													is.add(p);
													sb.append(p).append(',');
												}
											}
										}
									}
									is.add(w*d*h+t);
									// sb.append(w*d*h+t).append(',');
									String hash=sb.toString();
									if(!set.contains(hash)){
										set.add(hash);
										// debug(is);
										lists.add(is);
									}
								}
							}
						}
					}
				}
			}
		}
		end=System.nanoTime();
//		debug(lists.size(), cal, set.size(), (end-start)/1e9);
		// size <= 1300
		// 全く同じ場合は消す
		ArrayList<ArrayList<Integer>> next=new ArrayList<ArrayList<Integer>>();
		HashSet<Integer> exist=new HashSet<Integer>();
		for(ArrayList<Integer> list : lists){
			int bit=0;
			for(int i : list){
				if(i<m*d*h){
					bit|=1<<i;
				}
			}

		}
		if(lists.size()>1300){
			// new int[]{}[0]=0;
		}
		start=System.nanoTime();
		this.start=System.currentTimeMillis();
		found=false;
		// println(solve(lists, w*d*h+n)?"Yes":"No");
		visited=new HashSet<Long>();
		solve(lists, w*d*h+n);
		println(found?"Yes":"No");
		end=System.nanoTime();
		// debug((end-start)/1e9);
		// debug(count);
	}

	void rotX(int[][][] a){
		for(int k=0; k<m; k++){
			for(int j=0; j<m/2; j++){
				for(int i=0; i<m/2+m%2; i++){
					int t=a[j][i][k];
					a[j][i][k]=a[m-1-i][j][k];
					a[m-1-i][j][k]=a[m-1-j][m-1-i][k];
					a[m-1-j][m-1-i][k]=a[i][m-1-j][k];
					a[i][m-1-j][k]=t;
				}
			}
		}
	}

	void rotY(int[][][] a){
		for(int k=0; k<m; k++){
			for(int j=0; j<m/2; j++){
				for(int i=0; i<m/2+m%2; i++){
					int t=a[j][k][i];
					a[j][k][i]=a[m-1-i][k][j];
					a[m-1-i][k][j]=a[m-1-j][k][m-1-i];
					a[m-1-j][k][m-1-i]=a[i][k][m-1-j];
					a[i][k][m-1-j]=t;
				}
			}
		}
	}

	void rotZ(int[][][] a){
		for(int k=0; k<m; k++){
			for(int j=0; j<m/2; j++){
				for(int i=0; i<m/2+m%2; i++){
					int t=a[k][j][i];
					a[k][j][i]=a[k][m-1-i][j];
					a[k][m-1-i][j]=a[k][m-1-j][m-1-i];
					a[k][m-1-j][m-1-i]=a[k][i][m-1-j];
					a[k][i][m-1-j]=t;
				}
			}
		}
	}

	V head;
	V[] cols, rows;
	int[] count, set;

	HashSet<Long> visited;

	boolean solve(ArrayList<ArrayList<Integer>> lists, int m){
		int n=lists.size();
		head=new V(-1, -1);
		cols=new V[m];
		rows=new V[n];
		for(int i=0; i<m; i++){
			cols[i]=new V(i, -1);
			cols[i].connectX(head);
		}
		for(int i=0; i<n; i++){
			rows[i]=new V(-1, i);
			rows[i].connectY(head);
		}
		count=new int[m];
		// int[] _=new int[m];
		// for(int i=0; i<m; i++){
		// _[i]=i;
		// }
		// Random rand=new Random(0);
		// for(int i=m-1; i>=1; i--){
		// int j=rand.nextInt(i+1);
		// int t=_[i];
		// _[i]=_[j];
		// _[j]=t;
		// }

		for(int j=0; j<n; j++){
			for(int i : lists.get(j)){
				V v=new V(i, j);
				v.connectX(rows[j]);
				v.connectY(cols[i]);
				count[i]++;
			}
		}
		// debug(count);
		set=new int[n];
		fill(set, -1);
		return rec(0);
	}

	boolean found;

	boolean rec(int depth){
		if(head==head.right)
			return found=true;

		long state=(1L<<62)-1;
//		debug("state", state, Long.toBinaryString(state));
		for(V v=head.right; v!=head; v=v.right){
			state&=~(1L<<v.x);
//			debug("x", v.x);
		}
//		debug("state2", state, Long.toBinaryString(state));
		if(visited.contains(state)){
//			debug(count);
//			debug(Long.toBinaryString(state));
//			debug(head.x, head.right.x);
			return false;
		}

		// debug(count);
		V col=head.right;
		for(V v=head.right; v!=head; v=v.right){
			if(count[v.x]==0)
				return false;
			if(count[v.x]<count[col.x]){
				col=v;
				if(count[v.x]<=1)
					break;
			}
		}
		if(System.currentTimeMillis()-start>1.37e3){
			// return true;
		}
		for(V v=col.down; v!=col; v=v.down){
			for(V u=rows[v.y].right; u!=rows[v.y]; u=u.right)
				cols[u.x].cover();
			set[depth]=v.y;
			if(rec(depth+1)){
				for(V u=rows[v.y].left; u!=rows[v.y]; u=u.left)
					cols[u.x].uncover();
				return true;
			}
			set[depth]=-1;
			for(V u=rows[v.y].left; u!=rows[v.y]; u=u.left)
				cols[u.x].uncover();
		}
		visited.add(state);
		return false;
	}

	class V{
		int x, y;
		V up, down, left, right;

		V(int x, int y){
			this.x=x;
			this.y=y;
			up=down=left=right=this;
		}

		void cover(){
			left.right=right;
			right.left=left;
			for(V v=down; v!=this; v=v.down){
				for(V u=v.right; u!=v; u=u.right){
					u.up.down=u.down;
					u.down.up=u.up;
					if(u.x>=0)
						count[u.x]--;
				}
			}
		}

		void uncover(){
			for(V v=down; v!=this; v=v.down){
				for(V u=v.right; u!=v; u=u.right){
					u.up.down=u.down.up=u;
					if(u.x>=0)
						count[u.x]++;
				}
			}
			left.right=right.left=this;
		}

		void connectX(V v){
			left=v.left;
			right=v;
			left.right=right.left=this;
		}

		void connectY(V v){
			up=v.up;
			down=v;
			up.down=down.up=this;
		}
	}

	void debug(Object... os){
		System.err.println(Arrays.deepToString(os));
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
