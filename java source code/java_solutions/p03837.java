import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int INF = 10_000_000;
    private static Scanner scan = new Scanner(System.in);
    private static int[][] edge;
    private static boolean[][] used;
    private static int[][] data;
    private static int N;
    private static int M;

    public static void main(String[] args) {
	N = Integer.parseInt(scan.next());
	M = Integer.parseInt(scan.next());
	edge = new int[N][N];
	data = new int[N][N];
	used = new boolean[N][N];

	for (int i = 0; i < M; i++) {
	    int s = Integer.parseInt(scan.next()) - 1;
	    int d = Integer.parseInt(scan.next()) - 1;
	    int c = Integer.parseInt(scan.next());
	    edge[s][d] = edge[d][s] = c;
	}
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++) {
		data[i][j] = (i == j ? 0 : INF);
	    }
	}

	for (int node = 0; node < N; node++) {
	    dijkstra(node);
	}

	int count = 0;
	for (int i = 0; i < N; i++) {
	    for (int j = 0; j < N; j++) {
		if(used[i][j]) count++;
	    }
	}

	System.out.println(M - count/2);
	scan.close();
    }

    private static void dijkstra(int node) {
	boolean[] fix = new boolean[N];	
	int rest = 0;
	for (int i = 0; i < N; i++) {
	    if(data[node][i] < INF) fix[i] = true;
	    else rest++;
	}

	List<List<Integer>> pre = new ArrayList<List<Integer>>(N);
	for (int i = 0; i < N; i++) {
	    pre.add(new ArrayList<Integer>());
	}

	for (int step = 0; step < rest; step++) {
	    int min = INF;
	    int index = 0;
	    for (int i = 0; i < N; i++) {
		if(!fix[i]) {
		    for (int j = 0; j < N; j++) {
			if(fix[j] && edge[j][i] > 0) {
			    int tmp = data[node][j] + edge[j][i];
			    if(tmp < data[node][i]) {
				pre.get(i).clear();
				pre.get(i).add(j);
				data[node][i] = tmp;
			    } else if(tmp == data[node][i]) {
				pre.get(i).add(j);
			    }
			}
		    }
		    if(data[node][i] < min) {
			min = data[node][i];
			index = i;
		    }
		}
	    }
	    fix[index] = true;
	    for(Integer p: pre.get(index)) {
		used[p][index] = used[index][p] = true;
	    }
	    data[index][node] = data[node][index];
	}
    }
}
