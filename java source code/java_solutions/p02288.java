import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.StringTokenizer;

public class Main {
	static class MaximumHeaps {
		public void mh (InputReader ir) {
			
			int h = ir.nextInt();
			Node[] node = new Node[h + 1];
			
			for (int i = 1; i <= h; i++) {
				node[i] = new Node ();
				node[i].key = ir.nextInt();
			}
			
			Heaps (node, h);
			BuildMaxHeap (node, h);
			
			StringBuilder sb = new StringBuilder ();
			
			for (int i = 1; i <= h; i++) sb.append(" ").append(node[i].key);
			
			System.out.println(sb);
			
			
		}
		
		static void Heaps (Node[] node, int n) {
			
			for (int i = 1; i <= n; i++) {
				if (i / 2 > 0) node[i].parent = node[i / 2].key;
				if ( i * 2  <= n) node[i].left = node[ i * 2].key;
				if ( i * 2 +1 <= n) node[i].right = node[ i * 2 + 1].key;
			}
			
		}
		
		
		static void MaxHeapify(Node[] node, int i, int n) {
			
			int l = i * 2;
			int r = i * 2 + 1;      
			int largest = i;
		        
			if (l <= n && node[l].key > node[i].key) largest = l;
			else largest = i;
			
			if (r <= n && node[r].key > node[largest].key) largest = r;    
		    
			if (largest != i) {
				int tmp = node[i].key;
				node[i].key = node[largest].key;
				node[largest].key = tmp;
				MaxHeapify(node, largest, n);
			}		
		}
		    
		
		static void BuildMaxHeap(Node[] node, int n) {
			for(int i = n / 2; i >= 1; i--) {
				MaxHeapify(node, i, n);	
			}    
		}
		
		
	}
	
	static class Node {
		Integer key;
		Integer parent;
		Integer left;
		Integer right;
		Node () {
			key = null;
			parent = null;
			left = null;
			right = null;
		}
	}
	
	static class InputReader {
		BufferedReader br;
		StringTokenizer st;
		
		public String nextString() {
			
			 while (!st.hasMoreTokens()) {
	                try {
	                    st = new StringTokenizer(br.readLine(), " ");
	                } catch (IOException e) {
	                    throw new InputMismatchException();
	                }
	            }
			return st.nextToken();
		}
		
		public int nextInt() {
			return Integer.parseInt(nextString());
		}
					
		public InputReader (InputStream inputStream) {
			br = new BufferedReader (new InputStreamReader (inputStream));
			st = new StringTokenizer ("");
		}
		
	}
	
	public static void main (String[] args) {
		InputStream inputStream = System.in;
		InputReader in = new InputReader (inputStream);
		MaximumHeaps obj = new MaximumHeaps ();
		obj.mh (in);
	}
	
	
}
