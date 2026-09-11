import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
	public static void main(String[] args) throws NumberFormatException,
	IOException {
		ContestScanner in = new ContestScanner();
		while(true){
			int m = in.nextInt();
			int t = in.nextInt();
			int p = in.nextInt();
			int r = in.nextInt();
			if(m==0) break;
			int[][] wtime = new int[t][p];
			Team[] team = new Team[t];
			for(int i=0; i<t; i++){
				team[i] = new Team(i+1);
			}
			for(int i=0; i<r; i++){
				int mi = in.nextInt();
				int ti = in.nextInt()-1;
				int pi = in.nextInt()-1;
				int ji = in.nextInt();
				if(ji!=0){
					// incorrect
					wtime[ti][pi] += 20;
					continue;
				}
				// correct
				team[ti].score++;
				team[ti].time += mi + wtime[ti][pi];
			}
			Arrays.sort(team);
			StringBuilder sb = new StringBuilder();
			sb.append(team[0].id);
			for(int i=1; i<t; i++){
				if(team[i].comp(team[i-1]) == 0) sb.append("=");
				else sb.append(",");
				sb.append(team[i].id);
			}
			System.out.println(sb.toString());
		}
	}
}

class Team implements Comparable<Team>{
	int time;
	int id;
	int score;
	public Team(int id) {
		this.id = id;
	}
	
	public int comp(Team o) {
		if(score != o.score) return o.score - score;
		return time - o.time;
	}
	
	@Override
	public int compareTo(Team o) {
		if(score != o.score) return o.score - score;
		else if(time != o.time) return time - o.time;
		return o.id - id;
	}
}

class Node{
	int id;
	int sink;
	ArrayList<Node> edge = new ArrayList<Node>();
	public Node(int id) {
		this.id = id;
	}
	public void createEdge(Node node) {
		edge.add(node);
	}
}

class MyComp implements Comparator<int[]> {
	final int idx;
	public MyComp(int idx){
		this.idx = idx;
	}
	public int compare(int[] a, int[] b) {
		return a[idx] - b[idx];
	}
}

class Reverse implements Comparator<Integer> {
	public int compare(Integer arg0, Integer arg1) {
		return arg1 - arg0;
	}
}


class ContestWriter {
	private PrintWriter out;

	public ContestWriter(String filename) throws IOException {
		out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
	}

	public ContestWriter() throws IOException {
		out = new PrintWriter(System.out);
	}

	public void println(String str) {
		out.println(str);
	}
	
	public void println(Object obj) {
		out.println(obj);
	}

	public void print(String str) {
		out.print(str);
	}
	
	public void print(Object obj) {
		out.print(obj);
	}

	public void close() {
		out.close();
	}
}

class ContestScanner {
	private BufferedReader reader;
	private String[] line;
	private int idx;

	public ContestScanner() throws FileNotFoundException {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public ContestScanner(String filename) throws FileNotFoundException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				filename)));
	}

	public String nextToken() throws IOException {
		if (line == null || line.length <= idx) {
			line = reader.readLine().trim().split(" ");
			idx = 0;
		}
		return line[idx++];
	}
	
	public String readLine() throws IOException{
		return reader.readLine();
	}

	public long nextLong() throws IOException, NumberFormatException {
		return Long.parseLong(nextToken());
	}

	public int nextInt() throws NumberFormatException, IOException {
		return (int) nextLong();
	}

	public double nextDouble() throws NumberFormatException, IOException {
		return Double.parseDouble(nextToken());
	}
}
