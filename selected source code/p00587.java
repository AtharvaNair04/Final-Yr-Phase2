import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String[] s = sc.nextLine().split(" ");
			BinaryTree t1 = BinaryTree.valueOf(s[1]);
			BinaryTree t2 = BinaryTree.valueOf(s[2]);
			if (s[0].equals("i")) {
				System.out.println(t1.intersection(t2));
			}else{
				System.out.println(t1.union(t2));
			}
		}
	}

	static class BinaryTree {
		BinaryTree left,right;
		private int strlen = 0;
		public BinaryTree(BinaryTree left,BinaryTree right,int strlen) {
			this.left = left;
			this.right = right;
			this.strlen = strlen;
		}

		public static BinaryTree valueOf(String s) {
			return valueOf(s.toCharArray(), 0);
		}
		public static BinaryTree valueOf(char[] s,int i) {
			int left = i;
			if (s[i] != '(') {
				return null;
			}
			i++;
			BinaryTree t1 = valueOf(s, i);
			if (t1 != null) {
				i += t1.strlen;
			}
			i++;
			BinaryTree t2 = valueOf(s, i);
			if (t2 != null) {
				i += t2.strlen;
			}
			i++;
			return new BinaryTree(t1, t2, i-left);
		}

		BinaryTree intersection(BinaryTree t) {
			BinaryTree l = null, r = null;
			if (this.left != null && t.left != null) {
				l = this.left.intersection(t.left);
			}
			if (this.right != null && t.right != null) {
				r = this.right.intersection(t.right);
			}
			return new BinaryTree(l, r, -1);
		}

		BinaryTree union(BinaryTree t) {
			BinaryTree l = null, r = null;
			if (this.left != null && t.left != null) {
				l = this.left.union(t.left);
			}else if (this.left != null) {
				l = this.left;
			}else{
				l = t.left;
			}
			if (this.right != null && t.right != null) {
				r = this.right.union(t.right);
			}else if (this.right != null) {
				r = this.right;
			}else{
				r = t.right;
			}
			return new BinaryTree(l, r, -1);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append('(');
			if (left != null) {
				sb.append(left);
			}
			sb.append(',');
			if (right != null) {
				sb.append(right);
			}
			sb.append(')');
			return sb.toString();
		}
	}

}
