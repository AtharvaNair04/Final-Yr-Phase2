import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main{

	public static void main(String[] args){
		SC sc=new SC(System.in);
		int n = sc.nextInt();
		Tree[] tree = new Tree[n];
		for(int i = 0; i < n; i++){
			tree[i] = new Tree(i);
		}
		for(int i = 0; i < n; i++){
			String[] tmpArray = sc.nextLine().split(" ");
			int inputID = Integer.parseInt(tmpArray[0]);
			int childNum = Integer.parseInt(tmpArray[1]);
			for(int j = 0; j < childNum ; j++){
				int tmpIndex = Integer.parseInt(tmpArray[j + 2]);
				tree[inputID].addChild(tree[tmpIndex]);
				tree[tmpIndex].parent = inputID;
			}
		}
		for(int i = 0; i < n ;i++){
			if(tree[i].parent == -1){
				tree[i].initDepth(0);
				break;
			}
		}
		for(int i = 0; i < n; i++){
			System.out.print("node " + tree[i].id + ": parent = " + tree[i].parent + ", depth = "
					+ tree[i].depth + ", " + tree[i].type() + ", ");
			tree[i].printChildren();
			System.out.println();
		}
	}
}

class SC {
	private BufferedReader reader = null;
	private StringTokenizer tokenizer = null;
	public SC(InputStream in) {
		reader = new BufferedReader(new InputStreamReader(in));
	}
	public String next() {
		if (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		return tokenizer.nextToken();
	}
	public int nextInt() {
		return Integer.parseInt(next());
	}
	public long nextLong() {
		return Long.parseLong(next());
	}
	public double nextDouble() {
		return Double.parseDouble(next());
	}
	public String nextLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}


class Tree {
	int parent = -1;
	int id = -1;
	ArrayList<Tree> children = new ArrayList<Tree>();
	int depth = 0;
	public Tree (int id){
		this.id = id;
	}
	public void addChild(Tree n){
		children.add(n);
	}
	public void printChildren(){
		System.out.print("[");
		for(int i = 0; i < children.size(); i++){
			System.out.print(children.get(i).id);
			if(i != children.size() - 1){
				System.out.print(", ");
			}
		}
		System.out.print("]");
	}
	public String type(){
		if(parent == -1){
			return "root";
		}
		else if(children.size() == 0){
			return "leaf";
		}
		else{
			return "internal node";
		}
	}
	public void initDepth(int depth){
		this.depth = depth;
		for(int i = 0; i < children.size() ; i++){
			children.get(i).initDepth(depth + 1);
		}
	}
}
