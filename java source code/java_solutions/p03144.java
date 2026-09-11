import java.util.*;
import java.io.*;
public class Main {
	public static class Jewels implements Comparable<Jewels> {
		int c;
		long v;
		Jewels(){}
		Jewels(long v,int c) {
			this.c=c;
			this.v=v;
		}
		@Override
		public int compareTo(Jewels o) {
			if (o.v>v) return 1;
			if (o.v<v) return -1;
			return o.c-c;
		}
	}
	public static void main(String[] args) throws IOException {
		StreamTokenizer in=new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
		PrintWriter out=new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n=(int)in.nval;
		in.nextToken();
		int k=(int)in.nval;
		ArrayList<Integer>[] arr=new ArrayList[k+1];
		for (int i=1;i<=k;++i) arr[i]=new ArrayList<>();
		for (int i=0;i<n;++i) {
			in.nextToken();
			int c=(int)in.nval;
			in.nextToken();
			int v=(int)in.nval;
			arr[c].add(v);
		}
		ArrayList<Jewels> J=new ArrayList<>();
		PriorityQueue<Jewels> X=new PriorityQueue<>();
		PriorityQueue<Jewels> Y=new PriorityQueue<>();
		PriorityQueue<Jewels> Z=new PriorityQueue<>();
		for (int i=1;i<=k;++i) {
			arr[i].sort(new Comparator<Integer>() {
				public int compare(Integer a,Integer b) {
					return b-a;
				}
			});
			J.add(new Jewels(arr[i].get(0)+arr[i].get(1),i));
			J.add(new Jewels(arr[i].get(0)+arr[i].get(1),i));
			for (int j=2;j<arr[i].size();++j) J.add(new Jewels(arr[i].get(j)*2,i));
			Y.add(new Jewels(arr[i].get(0)+arr[i].get(1),i));
			if (arr[i].size()>=3) Z.add(new Jewels(0L+arr[i].get(0)+arr[i].get(1)+arr[i].get(2),i));
		}
		J.sort(null);
		long min_value=Long.MAX_VALUE;
		long min_pair=Long.MAX_VALUE;
		long sum=0;
		int[] C=new int[k+1];
		boolean[] col=new boolean[k+1];
		for (int i=0;i<n;++i) {
			long value=J.get(i).v;
			int color=J.get(i).c;
			long ans=-2;
			if (C[color]==0) {
				while (!Y.isEmpty()&&col[Y.peek().c]) Y.poll();
				while (!Z.isEmpty()&&col[Z.peek().c]) Z.poll();
				if (!X.isEmpty()) ans=Math.max(ans,sum+X.peek().v);
				if (!Y.isEmpty()) ans=Math.max(ans,sum-min_value+Y.peek().v*2);
				if (!Z.isEmpty()) ans=Math.max(ans,sum-min_pair+Z.peek().v*2);
				col[color]=true;
			} else {
				ans=sum+value;
				if (C[color]==1) {
					for (int j=2;j<arr[color].size();++j)
						X.add(new Jewels(arr[color].get(j)*2,color));
					min_pair=value*2;
				} else {
					X.poll();
					min_value=value;
				}
			}
			++C[color];
			sum+=value;
			out.printf("%d\n",ans/2);
		}
		out.flush();
	}
}
