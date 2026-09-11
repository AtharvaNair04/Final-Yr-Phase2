import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {
	private static FastScanner sc = new FastScanner();

	public static void main(String[] args) {
		int N = sc.nextInt();
		
		Rectangle[] rect = new Rectangle[N];

		for(int i=0; i<N; i++) {
			rect[i] = new Rectangle(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
		}
		
		ArrayList<Integer> axis_x = new ArrayList<>();
		Rectangle[] sort1 = Arrays.copyOf(rect, N);
		Arrays.sort(sort1, (a, b) -> a.x1 - b.x1);
		Rectangle[] sort2 = Arrays.copyOf(rect, N);
		Arrays.sort(sort2, (a, b) -> a.x2 - b.x2);
		int idx1 = 0;
		int idx2 = 0;
		while(idx2 < N) {
			if(idx1 == N || sort2[idx2].x2 < sort1[idx1].x1) {
				axis_x.add(sort2[idx2].x2);
				idx2++;
			} else {
				axis_x.add(sort1[idx1].x1);
				idx1++;
			}
		}
		
		ArrayList<Integer> axis_y = new ArrayList<>();
		Arrays.sort(sort1, (a, b) -> a.y1 - b.y1);
		Arrays.sort(sort2, (a, b) -> a.y2 - b.y2);
		idx1 = 0;
		idx2 = 0;
		while(idx2 < N) {
			if(idx1 == N || sort2[idx2].y2 < sort1[idx1].y1) {
				axis_y.add(sort2[idx2].y2);
				idx2++;
			} else {
				axis_y.add(sort1[idx1].y1);
				idx1++;
			}
		}
		
		boolean[][] grid = new boolean[axis_x.size()][axis_y.size()];
		for(int i=0; i<N; i++) {
			int axisidxx1 = 0;
			int axisidxy1 = 0;
			int axisidxx2 = 0;
			int axisidxy2 = 0;
			for(int j=0; j<axis_x.size(); j++) {
				if(axis_x.get(j) == rect[i].x1) {
					axisidxx1 = j;
				}
				if(axis_x.get(j) == rect[i].x2) {
					axisidxx2 = j;
					break;
				}
			}
			for(int j=0; j<axis_y.size(); j++) {
				if(axis_y.get(j) == rect[i].y1) {
					axisidxy1 = j;
				}
				if(axis_y.get(j) == rect[i].y2) {
					axisidxy2 = j;
					break;
				}
			}
			
			for(int j=axisidxx1; j<axisidxx2; j++) {
				for(int k=axisidxy1; k<axisidxy2; k++) {
					grid[j][k] = true;
				}
			}
		}
		
		long sum = 0;
		for(int i=0; i<axis_x.size(); i++) {
			for(int j=0; j<axis_y.size(); j++) {
				if(grid[i][j]) {
					sum += ((long)axis_x.get(i+1) - axis_x.get(i)) * ((long)axis_y.get(j+1) - axis_y.get(j));
				}
			}
		}
		
		System.out.println(sum);
		
	}
	
	static class Rectangle {
		int x1;
		int y1;
		int x2;
		int y2;
		public Rectangle(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}		
	}

	static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1024];
        private int ptr = 0;
        private int buflen = 0;
        private boolean hasNextByte() {
            if (ptr < buflen) {
                return true;
            }else{
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
        private int readByte() { if (hasNextByte()) return buffer[ptr++]; else return -1;}
        private static boolean isPrintableChar(int c) { return 33 <= c && c <= 126;}
        private void skipUnprintable() { while(hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;}
        public boolean hasNext() { skipUnprintable(); return hasNextByte();}
        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            StringBuilder sb = new StringBuilder();
            int b = readByte();
            while(isPrintableChar(b)) {
                sb.appendCodePoint(b);
                b = readByte();
            }
            return sb.toString();
        }
        public long nextLong() {
            return Long.parseLong(next());
        }
        public int nextInt(){
            return Integer.parseInt(next());
        }
    }
}
