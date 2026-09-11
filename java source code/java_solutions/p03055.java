import java.util.*;

public class Main{
	static final int	dx[]={0,-1,0,1};
	static final int	dy[]={-1,0,1,0};

	public static void main(String[] $){
		Scanner s=new Scanner(System.in);
		int n=s.nextInt();
		ArrayList<ArrayList<Integer>>g=new ArrayList<>();
		for(int i=0;i<n;++i)
			g.add(new ArrayList<>(1));
		for(int i=1;i<n;++i) {
			int a=s.nextInt()-1;
			int b=s.nextInt()-1;
			g.get(a).add(b);
			g.get(b).add(a);
		}
		boolean f[]=new boolean[n];

		int root=0;
		{
			f[0]=true;

			ArrayDeque<Integer>q=new ArrayDeque<>();
			q.add(0);
			f[0]=true;
			while(!q.isEmpty()) {
				int i=q.poll();
				for(int j:g.get(i)) {
					if(!f[j]) {
						q.add(j);
						f[j]=true;
					}
				}
				if(q.isEmpty())
					root=i;
			}
			Arrays.fill(f,false);
		}

		int r2 = dfs(g,f,root,1);
		System.err.println(r2);;
		System.out.println(r2%3==2?"Second":"First");
	}

	private static int dfs(ArrayList<ArrayList<Integer>> g,boolean[] f,int i,int d){
		f[i]=true;
		int b=0;
		for(int j:g.get(i)) {
			if(!f[j]) {
				b = Math.max(b,dfs(g,f,j,d+1));
			}
		}
		return b==0?d:b;
	}
}
