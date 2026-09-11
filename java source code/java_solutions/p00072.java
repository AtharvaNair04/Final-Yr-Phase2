import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static class Edge implements Comparable<Edge>{
		int v1, v2;
		int weight;

		Edge (int v1, int v2, int weight) {
			this.v1 = v1;
			this.v2 = v2;
			this.weight = weight / 100 - 1;
		}

		public int compareTo(Edge o) {
			return this.weight - o.weight;
		}
	}

	static class Vertex {
		Vertex parent;

		Vertex () {
			this.parent = this;
		}
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		Vertex vertex[];
		Edge edge[];
		int sumWeight;

		n = sc.nextInt();
		while (n != 0) {
			m = sc.nextInt();
			vertex = new Vertex[n];
			edge = new Edge[m];
			sumWeight = 0;
			
			for (int i = 0; i < n; i++) {
				vertex[i] = new Vertex();
			}

			for (int i = 0; i < m; i++) {
				String s[] = sc.next().split(",");
				edge[i] = new Edge(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
			}
			Arrays.sort(edge);

			for (int i = 0; i < m; i++) {
				if (find(vertex[edge[i].v1]) != (find(vertex[edge[i].v2]))) {
					sumWeight += edge[i].weight;
					union(vertex[edge[i].v1], vertex[edge[i].v2]);
				}
			}
			System.out.println(sumWeight);
			n = sc.nextInt();
		}
	}

	public static Vertex find(Vertex vertex) {
		if (vertex.parent == vertex) {
			return vertex;
		} else {
			return find(vertex.parent);
		}
	}
	
	public static void union (Vertex v1, Vertex v2) {
		Vertex vertex1 = find(v1);
		Vertex vertex2 = find(v2);
		vertex1.parent = vertex2;
	}
}
