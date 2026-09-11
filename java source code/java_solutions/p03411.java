import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main{

	static Scanner s=new Scanner(System.in);
	public static int maximumBipartiteMatching(int L,ArrayList<HashSet<Integer>> adj){
		boolean[] visited=new boolean[adj.size()];
		int[] match=new int[adj.size()];
		Arrays.fill(match,-1);

		int count=0;
		for(int curr=0;curr<L;curr++){
			Arrays.fill(visited,false);

			if(augment(adj,curr,match,visited)){
				count++;
			}
		}

		return count;
	}

	private static boolean augment(ArrayList<HashSet<Integer>> adj,int curr,int[] match,boolean[] visited){
		if(curr<0){
			return true;
		}

		for(final int next:adj.get(curr)){
			if(visited[next]){
				continue;
			}

			visited[next]=true;
			if(augment(adj,match[next],match,visited)){
				match[curr]=next;
				match[next]=curr;
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args){

		int n=s.nextInt();
		final int size=n*2;
		ArrayList<HashSet<Integer>> adj=new ArrayList<>();
		for(int i=0;i<size;i++){
			adj.add(new HashSet<Integer>());
		}

		Point[]red=new Point[n];
		for(int i=0;i<n;++i)
			red[i]=new Point(s.nextInt(),s.nextInt());

		for(int i=0;i<n;i++){
			int x=s.nextInt(),y=s.nextInt();
			for(int j=0;j<n;++j) {
				if(red[j].x<x&&red[j].y<y) {
					adj.get(j).add(n+i);
				}
			}
		}
		System.out.println(maximumBipartiteMatching(n,adj));
	}
}
