import java.io.IOException;
import java.io.InputStream;
import java.util.*;
 
public class Main implements Runnable {
	
    public static void main(String[] args) {
    	new Thread(null, new Main(), "", 16 * 1024 * 1024).start();
    }
    
    public void run() {
        FastScanner sc = new FastScanner();
        
        int n = sc.nextInt();
 
        int[] from = new int[n-1];
        int[] to = new int[n-1];
        ArrayList<Integer> c = new ArrayList<>();
        int[] allo = new int[n];
        NodeList[] nlist = new NodeList[n];
        
        for(int i=0;i<n;i++){
        	nlist[i] = new NodeList();
        }
 
        for(int i=0;i<n-1;i++){
        	from[i] = sc.nextInt()-1;
        	to[i] = sc.nextInt()-1;
        	nlist[from[i]].add(to[i]);
        	nlist[to[i]].add(from[i]);
        }
        
        for(int i=0;i<n;i++){
        	c.add(sc.nextInt());
        }
        
        Collections.sort(c);
        
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for(int i=0;i<n;i++){
        	q.offerLast(c.get(i));
        }
        
        
        ArrayDeque<Integer> bfs = new ArrayDeque<Integer>();
        bfs.offerFirst(0);
        boolean[] isChecked = new boolean[n];
        isChecked[0] = true;
        
        while(!bfs.isEmpty()){
        	int now = bfs.pollFirst();
        	allo[now] = q.pollLast();
        	
        	for(int j:nlist[now]){
        		if(!isChecked[j]){
        			bfs.offerLast(j);
        			isChecked[j] = true;
        		}
        	}
        }
        
        
        int sum = 0;
        for(int i=0;i<n-1;i++){
            sum += Math.min(allo[from[i]], allo[to[i]]);
        }
        
        
        System.out.println(sum);
        
        for(int i=0;i<n;i++){
            System.out.println(allo[i]);
        }
    }
 
}
 
class Node{
	int idx;
 
	public Node(int idx){
		this.idx = idx;
	}

}

class NodeList extends ArrayList<Integer>{
	private static final long serialVersionUID = -4461479015945373120L;
}
 
 
class FastScanner {
	private final InputStream in = System.in;
	private final byte[] buffer = new byte[1024];
	private int ptr = 0;
	private int buflen = 0;
	private boolean hasNextByte() {
		if (ptr < buflen) {
			return true;
		} else {
			ptr = 0;
			try {
				buflen = in.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (buflen <= 0) {
				return false;
			}
		}
		return true;
	}
	private int readByte() {
		if (hasNextByte())
			return buffer[ptr++];
		else
			return -1;
	}
	private static boolean isPrintableChar(int c) {
		return 33 <= c && c <= 126;
	}
	public boolean hasNext() {
		while (hasNextByte() && !isPrintableChar(buffer[ptr]))
			ptr++;
		return hasNextByte();
	}
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = readByte();
		while (isPrintableChar(b)) {
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	public long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();
		long n = 0;
		boolean minus = false;
		int b = readByte();
		if (b == '-') {
			minus = true;
			b = readByte();
		}
		if (b < '0' || '9' < b) {
			throw new NumberFormatException();
		}
		while (true) {
			if ('0' <= b && b <= '9') {
				n *= 10;
				n += b - '0';
			} else if (b == -1 || !isPrintableChar(b)) {
				return minus ? -n : n;
			} else {
				throw new NumberFormatException();
			}
			b = readByte();
		}
	}
	public int nextInt() {
		long nl = nextLong();
		if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE)
			throw new NumberFormatException();
		return (int) nl;
	}
	public double nextDouble() {
		return Double.parseDouble(next());
	}
}
