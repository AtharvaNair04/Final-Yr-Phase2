import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		for(int m,n,x,a[][];(n=s.nextInt())>0;){
			m=s.nextInt();
			x=s.nextInt();
			a=new int[n][m];
			Deque<int[]>q=new ArrayDeque<int[]>();
			for(int i=0;i<x;++i){
				int u=s.nextInt()-1,v=s.nextInt()-1;
				a[u][v]=1;
				q.add(new int[]{u,v,1});
			}
			int[][]dn={{-1,-1,-1,0,0,1},{-1,0,0,1,1,1}};
			int[][]dm={{-1,0,1,-1,1,0},{0,-1,1,-1,0,1}};
			
			while(!q.isEmpty()){
				int[]t=q.poll();
				for(int i=0;i<6;++i){
					int u=t[0]+dn[t[1]%2][i],v=t[1]+dm[t[1]%2][i];
					if(0<=u&&u<n&&0<=v&&v<m&&a[u][v]<1){
						q.add(new int[]{u,v,a[u][v]=t[2]+1});
					}
				}
			}
			int r=0;
			for(x=s.nextInt();x-->0;){
				int u=s.nextInt()-1,v=s.nextInt()-1;
				if(a[u][v]>1){
					int[][] tmp = new int[n][];
					for(int i=0;i<n;++i)
						tmp[i]=Arrays.copyOf(a[i],m);
					tmp[u][v]=1;
					q.add(new int[]{u,v,1});
					int c=1;
					while(!q.isEmpty()){
						int[]t=q.poll();
						for(int i=0;i<6;++i){
							int u1=t[0]+dn[t[1]%2][i],v1=t[1]+dm[t[1]%2][i];
							if(0<=u1&&u1<n&&0<=v1&&v1<m&&tmp[u1][v1]>t[2]+1){
								++c;
								q.add(new int[]{u1,v1,tmp[u1][v1]=t[2]+1});
							}
						}
					}
					r=r>c?r:c;
				}
			}
			System.out.println(r);
		}
	}
}
