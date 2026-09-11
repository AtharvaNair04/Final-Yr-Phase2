import java.util.*;

public class Main {
	Scanner in = new Scanner(System.in);
	public static void main(String[] args) {
		new Main();
	}
	public Main() {
		new AOJ0119();
	}

	class AOJ0119{
		public AOJ0119() {
			int m = in.nextInt();
			int n = in.nextInt();
			int[] cnt = new int[m];
			boolean edge[][] = new boolean[m][m];
			for(int i=0;i<n;i++){
				int x = in.nextInt(),y = in.nextInt();
				edge[y-1][x-1] = true;
				cnt[y-1]++;
			}
			ArrayList<Integer> list = new ArrayList<Integer>();
			for(int k=0;k<m;k++){
				for(int i=0;i<m;i++)if(cnt[i]==0){
					list.add(i+1);
					cnt[i] = Integer.MAX_VALUE;
					for(int s=0;s<m;s++)if(edge[s][i])cnt[s]--;
				}
			}
			for(int i=0;i<list.size();i++)System.out.println(list.get(i));
		}
	}
}
