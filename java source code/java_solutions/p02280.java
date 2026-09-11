import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	class Node {
		public Node(int left, int right) {
			this.left = left;
			this.right = right;
		}

		int parent = -1;
		int left;
		int right;
		int sibling = -1;
		int depth = 0;
		int degree;
		int height;
		String type;

		public String toString() {
			return ": parent = " + parent + ", sibling = " + +sibling + ", degree = " + degree + ", depth = " + depth
					+ ", height = " + height + ", " + type;
		}
	}

	HashMap<Integer, Node> nodes = new HashMap<>();

	private void solv() {
		Scanner input = new Scanner(System.in);
		int n = input.nextInt();
		int[] keys = new int[n];

		for (int i = 0; i < n; i++) {
			int id = input.nextInt();
			int left = input.nextInt();
			int right = input.nextInt();
			Node node = new Node(left, right);
			nodes.put(id, node);
			keys[i] = id;
		}

		// height
		for (int target : keys) {
			Node node = nodes.get(target);
			node.height = calcHeight(target);
		}

		// degree
		for (int target : keys) {
			Node node = nodes.get(target);
			
			if (node.left == -1 && node.right == -1) {
				node.degree = -0;
			} else if(node.left == -1 || node.right == -1){
				node.degree = 1;
			}else{
				node.degree = 2;
			}
		}

		// parent
		for (int target : keys) {
			Node node = nodes.get(target);
			for (int i : keys) {
				if (nodes.get(i).left == target || nodes.get(i).right == target) {
					node.parent = i;
					break;
				}
			}
		}

		// depth
		for (int target : keys) {
			int depth = 0;
			Node node = nodes.get(target);
			while (node.parent != -1) {
				node = nodes.get(node.parent);
				depth++;
			}
			nodes.get(target).depth = depth;
		}

		// sibling
		for (int target : keys) {
			Node node = nodes.get(target);
			int left = node.left;
			int right = node.right;
			if (left != -1 && right != -1) {
				nodes.get(left).sibling = right;
				nodes.get(right).sibling = left;
			}
		}
		
		// type
		for (int target : keys) {
			Node node = nodes.get(target);
			if (node.parent == -1) {
				node.type = "root";
			} else if (node.degree == 0) {
				node.type = "leaf";
			} else {
				node.type = "internal node";
			}
		}

		Arrays.sort(keys);
		for (int i : keys) {
			System.out.println("node " + i + nodes.get(i));
		}

	}

	public int calcHeight(int key) {
		int h1 = 0;
		int h2 = 0;
		if (nodes.get(key).right != -1) {
			h1 = calcHeight(nodes.get(key).right) + 1;
		}
		if (nodes.get(key).left != -1) {
			h2 = calcHeight(nodes.get(key).left) + 1;
		}
		return Math.max(h1, h2);
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.solv();
	}

	private void print(Object[] xs) {
		StringBuilder sb = new StringBuilder();
		for (Object x : xs)
			sb.append(x.toString()).append(' ');
		System.out.println(sb.toString().trim());
	}

	private void print(int[] xs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < xs.length; i++) {
			sb.append(xs[i]).append(' ');
		}
		System.out.println(sb.toString().trim());
	}

	private static class Scanner {
		BufferedReader r;
		StreamTokenizer st;

		public Scanner(InputStream in) {
			r = new BufferedReader(new InputStreamReader(in));
			st = new StreamTokenizer(r);
		}

		public String next() {
			nextToken();
			return st.sval;
		}

		public Integer nextInt() {
			nextToken();
			return (int) st.nval;
		}

		private int nextToken() {
			int token = st.TT_EOF;
			try {
				token = st.nextToken();
			} catch (IOException e) {
			}
			return token;
		}
	}

}
