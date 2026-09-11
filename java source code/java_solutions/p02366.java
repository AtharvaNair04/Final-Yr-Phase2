import java.util.Scanner;
import java.util.Stack;
import java.util.TreeSet;

public class Main 
{
	static class Vertex
	{
		TreeSet<Integer> adjlist = new TreeSet<Integer>();
		
		int dim()
		{
			return adjlist.size();
		}
		
		public Vertex()
		{
			
		}
		
		public Vertex(int adjacentTo)
		{
			adjlist.add(adjacentTo);
		}
	}
	
	static Scanner sc = new Scanner(System.in);
	static int vn, en;
	static Vertex[] v;
	static int[] low;
	static int[] disc;
	static int[] parent;
	static boolean[] visited;
	static TreeSet<Integer> ap;
	
	static void DFS()
	{
		int time = 0;
		int pos = 0;
		Stack<Integer> queue = new Stack<Integer>();
		
		queue.push(0);
		
		int children = 0;
		int rootchildren = 0;
		
		while(!queue.isEmpty())
		{
			pos = queue.peek();
			
			if(!visited[pos])
			{
	//			System.out.println("Visit " + pos + " " + queue);
				if(pos == 0)time = 0;
				else
				{
					time = disc[parent[pos]] + 1;
				}
				Vertex v0 = v[pos];
				boolean alreadyvisited = visited[pos];
				visited[pos] = true;
				disc[pos] = time;
				low[pos] = time;
				children = 0;
				
				for(int adj : v0.adjlist)
				{
					if(parent[pos] != adj)
					{
						if(!visited[adj])
						{
							children++;
//							if(pos == 0)rootchildren++;
							
							parent[adj] = pos;
							queue.push(adj);
						}
						else
						{
							low[pos] = Math.min(low[pos], disc[adj]);
	//						System.out.println("Re " + pos + " " + adj);
						}
					}				
				}

			}
			else
			{
				pos = queue.pop();
				
				int par = parent[pos];
				if(par > 0)
				{
					low[par] = Math.min(low[par], low[pos]);
					if(par != 0 && disc[par] <= low[pos])
					{
		//				System.out.println(pos);
						ap.add(par);
					}
				}
				if(par == 0)rootchildren++;
			}

			
		}
		if(pos == 0 && rootchildren > 1)
		{
			ap.add(0);
		}

	}

	public static void main(String[] args) 
	{
		vn = sc.nextInt();
		en = sc.nextInt();
		v = new Vertex[vn];
		disc = new int[vn];
		low = new int[vn];
		parent = new int[vn];
		visited = new boolean[vn];
		ap = new TreeSet<Integer>();

		for(int i = 0; i < vn; i++)
		{
			v[i] = new Vertex();
			parent[i] = -1;
			low[i] = 999999999;
		}
		
		for(int i = 0; i < en; i++)
		{
//			System.out.println("Reading edge " + (i+1));
			int v1 = sc.nextInt();
			int v2 = sc.nextInt();
			
			v[v1].adjlist.add(v2);
			v[v2].adjlist.add(v1);
		}
		
		DFS();
//		System.out.println(ap);
//		System.out.println("Disc "+ Arrays.toString(disc));
//		System.out.println("Low "+ Arrays.toString(low));
		StringBuilder sb = new StringBuilder("");
		int i = 0;
		for(int a : ap)
		{
			i++;
			sb.append(a);
			if(i < ap.size())
			{
				sb.append("\n");
			}
		}
		if(ap.size()>0)System.out.println(sb);
	}

}
