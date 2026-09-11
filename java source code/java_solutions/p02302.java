import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Main {
	private static FastScanner sc = new FastScanner();
	static final double ALLOWABLE_ERROR = 0.0000000001;

	public static void main(String[] args) {
		int n = sc.nextInt();
		Point[] p = new Point[n];
		for(int i=0; i<n; i++) {
			p[i] = new Point(sc.nextDouble(), sc.nextDouble());
		}
		
		int q = sc.nextInt();
		StringBuilder ans = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#######0.0000000000");
		for(int i=0; i<q; i++) {
			double p1x = sc.nextDouble();
			double p1y = sc.nextDouble();
			double p2x = sc.nextDouble();
			double p2y = sc.nextDouble();
			
			int l_idx = -1;
			int r_idx = -1;
			boolean mode = true;
			for(int j=0; j<n; j++) {
				if(mode) {
					if((p2x - p1x)*(p[j].y - p1y)-(p[j].x - p1x)*(p2y - p1y) + ALLOWABLE_ERROR < 0) {
						r_idx = j;
						mode = false;
					}
				} else {
					if((p2x - p1x)*(p[j].y - p1y)-(p[j].x - p1x)*(p2y - p1y) >= ALLOWABLE_ERROR) {
						l_idx = j;
						mode = true;
					}
				}
			}
			
//			System.out.println("i=" + i);
//			System.out.println(l_idx + " " + r_idx);
			
			ArrayList<Point> pa = new ArrayList<>();
			if(l_idx == -1 && r_idx == 0) {
				ans.append(0);
				ans.append("\n");
				continue;
			} else if(l_idx == -1 && r_idx == -1) {
				l_idx = 0;
				r_idx = 0;
				for(int j=0; j<n; j++) {
					pa.add(p[j]);
				}
			} else if(l_idx == -1) {
				l_idx = 0;
				for(int j=l_idx; j%n != r_idx; j++) {
					pa.add(p[j%n]);
				}
				Point p1 = new Point(p1x, p1y);
				Point p2 = new Point(p2x, p2y);

				pa.add(0, crossPoint(p1, p2, p[(l_idx-1+n)%n], p[l_idx]));
				pa.add(crossPoint(p1, p2, p[(r_idx-1+n)%n], p[r_idx]));
			} else {
				for(int j=l_idx; j%n != r_idx; j++) {
					pa.add(p[j%n]);
				}
				Point p1 = new Point(p1x, p1y);
				Point p2 = new Point(p2x, p2y);

				pa.add(0, crossPoint(p1, p2, p[(l_idx-1+n)%n], p[l_idx]));
				pa.add(crossPoint(p1, p2, p[(r_idx-1+n)%n], p[r_idx]));
			}
			
			
			
			
			
			double sum_area = 0;
			for(int j=0; j<pa.size(); j++) {
				Point origin = new Point(0, 0);
				if(checkCounterClockwise(pa.get(j), pa.get((j+1)%pa.size()), origin)) {
					sum_area += area(pa.get(j), pa.get((j+1)%pa.size()), origin);
				} else {
					sum_area -= area(pa.get(j), pa.get((j+1)%pa.size()), origin);
				}
			}
			
			if(Double.isNaN(sum_area)) {
				ans.append(0);
				ans.append("\n");
				continue;
			}
			
			ans.append(df.format(sum_area));
			ans.append("\n");
		}
		
		System.out.print(ans);
	}
	
	static Point crossPoint(Point p0, Point p1, Point p2, Point p3) {
		double a1 = (p1.y - p0.y) / (p1.x - p0.x);
		double a2 = (p3.y - p2.y) / (p3.x - p2.x);
		double b1 = p0.y - a1 * p0.x;
		double b2 = p2.y - a2 * p2.x;
		
		double x;
		double y;
		if(p1.x == p0.x) {
			x = p0.x;
			y = a2 * x + b2;
		} else if(p3.x == p2.x) {
			x = p2.x;
			y = a1 * x + b1;
		} else {
			x = (b1 - b2)/(a2 - a1);
			y = a1 * x + b1;
		}
		
		return new Point(x, y);
	}
	
	static boolean checkCounterClockwise(Point p1, Point p2, Point p3) {
		double a = (p2.y - p1.y) / (p2.x - p1.x);
		double b = p2.y - a * p2.x;
		if(p1.x == p2.x) {
			if((p1.y < p2.y && p3.x < p1.x) || (p1.y > p2.y && p3.x > p1.x)) {
				return true;
			} else {
				return false;
			}	
			
		} else {
			if((p1.x < p2.x && p3.y - (a * p3.x + b) > 0) || (p1.x > p2.x && p3.y - (a * p3.x + b) < 0)) {
				return true;
			} else {
				return false;
			}				
		}
	}
	
	static double area(Point p1, Point p2, Point p3) {
		double a = distance(p1, p2);
		double b = distance(p2, p3);
		double c = distance(p3, p1);
		double s = (a + b + c) / 2;
		if(s < Math.max(a, Math.max(b, c)) + ALLOWABLE_ERROR && s + ALLOWABLE_ERROR > Math.max(a, Math.max(b, c))) {
			return 0;
		}
		return Math.sqrt(s * (s - a) * (s - b) * (s - c));
	}
	
	static double distance(Point p1, Point p2) {
		return Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y));
	}

	static class Point {
		double x;
		double y;
		double ang;
		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static class FastScanner {
        private final InputStream in = System.in;
        private final byte[] buffer = new byte[1024];
        private int ptr = 0;
        private int buflen = 0;
        private boolean hasNextByte() {
            if(ptr < buflen) {
                return true;
            } else {
                ptr = 0;
                try {
                    buflen = in.read(buffer);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                if(buflen <= 0) {
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
        public double nextDouble(){
            return Double.parseDouble(next());
        }
    }
}
