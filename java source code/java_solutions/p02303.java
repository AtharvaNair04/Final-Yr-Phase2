import java.util.*;
import java.text.DecimalFormat;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
 
class Main {
 
  static final double EPS = 1e-12;
        
        //  for two points x(x0,x1) and y(y0,y1), what is the squared distance? 
       public static Double dist(Double[] x, Double[] y) {     
         return (x[0]-y[0])*(x[0]-y[0]) + (x[1]-y[1])*(x[1]-y[1]);
       }
        
        // only need to return a value if closest pair is split between left and right halves
        public static Double[][][] splitpair(Double[][] px, Double[][] py, double d) {  
            double xBar = px[(px.length+1)/2][0]; // largest x-coordinate of the left half of px
            List<Double[]> Sy = new ArrayList<Double[]>(); // Sy will be the set of points 
            //(sorted by Y-coordinate) where the x-coord xi satisfies xBar-d <= xi <= xBar -d
            for (int i = 0; i < py.length; i++) {
                if (xBar - d - EPS <= py[i][0] && py[i][0]<= xBar + d + EPS) { //only consider a 'strip' 
                    // around xBar that is 2*d wide
                    Sy.add(py[i]); // if exists a pair with dist less than d
                    // it must be in this strip (obvious) 
                }
            }
            Double[][][] bestpair = new Double[2][2][2]; // the coords of the best pair of points so far
            bestpair = null;
            double bestD = d * d; // the squared distance of the best pair of points
            
            // now iterate through Sy for points that aren't more than 7 apart
            // if a pair exists that is closer than dist d, it must be within 7 of each other
            for (int i = 0; i < Sy.size() - 2; i++) {
                for (int j = 1; j < Math.min(8, Sy.size()-i-1); j++) { // the next 7 points
                    Double[] p = Sy.get(i); Double[] q = Sy.get(i+j);
                    
                    if (dist(p, q) < bestD) {
                        bestpair = convertToForm(p,q);
                        bestD = dist(p, q);
                    }
                }
            }
            return bestpair;
        }
   
        public static Double[][][] convertToForm(Double[] a, Double[] b) { 
            //convert 2 points into two pairs of pairs, first sorted by x, other sorted by y (so Px, Py style)
            Double[][] byX = {a, b};
            Double[][] byY = {a, b};
            // now sort by x coord
            if (byX[0][0] > byX[1][0] || 
                (byX[0][0] == byX[1][0] && byX[0][1] > byX[1][1])) {
              Double[] tmp = byX[0];
              byX[0] = byX[1];
              byX[1] = tmp;
            }
            
            // sort by y-coord
            if (byX[0][1] > byX[1][1] || 
                (byX[0][1] == byX[1][1] && byX[0][0] > byX[1][0])) {
              Double[] tmp = byX[0];
              byX[0] = byX[1];
              byX[1] = tmp;
            }
            
            Double[][][] res = {byX, byY};
            return res;
        }
 
        public static Double[][][] solve(Double[][][] P) { 
            Double[][] Px = P[0];
            Double[][] Py = P[1];
            // where Px and Py are the points sorted by x and y coordinate respectively
            // this function returns the 2 closest points
            if (Px.length == 2) {
                return P;
            } else if (Px.length == 3) {
                Double[] p1 = Px[0]; Double[] p2 = Px[1]; Double[] p3 = Px[2];
                Double d12 = dist(p1, p2); Double d13 = dist(p1, p3); Double d23 = dist(p2, p3);
                Double minD = Math.min(d23, Math.min(d12, d13));
                if (Math.abs(d12 - minD) < EPS) {
                    return convertToForm(p1, p2);
                } else if (Math.abs(d13 - minD) < EPS) {
                    return convertToForm(p1, p3);
                } else if (Math.abs(d23 - minD) < EPS) {
                    return convertToForm(p2, p3);
                } else {
                    System.out.println("HUNG!");
                    return convertToForm(p2, p3); // it fails here, this is just to return the right data struct
                }
            } else {
                // split the left and right halves of all points (by x coord)
                int L = Px.length;
                assert L > 1;
                Double[][] Qx = new Double[L/2][2];
                Double[][] Rx = new Double[L-L/2][2];
                for (int i = 0; i < L; i++) {
                    if (i < L/2) {
                        Qx[i] = Px[i];
                    } else {
                        Rx[i-L/2] = Px[i];
                    }
                }
                Double[][] Qy = new Double[L/2][2];
                Double[][] Ry = new Double[L-L/2][2];
                Double k = Qx[Qx.length-1][0]; 
                Double l = Qx[Qx.length-1][1];
                int cntQy = 0; int cntRy = 0; 
                for (int i=0; i < Py.length; i++) {
                    Double[] pt = Py[i];
                    if (pt[0] < k) {
                        Qy[cntQy++] = pt;
                    } else if (Math.abs(pt[0] - k) < EPS) {
                        if (pt[1] - l <= EPS) {
                            Qy[cntQy++] = pt;
                        } else {
                            Ry[cntRy++] = pt;
                        }
                    } else {
                        Ry[cntRy++] = pt;
                    }
                }
                
                // now use recursion
                Double[][][] Q = {Qx, Qy};
                Double[][][] R = {Rx, Ry};
                assert Q.length >= 2;
                Double[][][] c1 = solve(Q);
                Double[][][] c2 = solve(R);
                Double Dc1 = dist(c1[0][0], c1[0][1]); // returns SQUARED distance
                Double Dc2 = dist(c2[0][0], c2[0][1]); // returns SQUARED distance
                
                Double D = Math.min(Dc1, Dc2);
                Double[][][] bestpair = new Double[2][2][2];
                
                if (Math.abs(Dc1 - D) < EPS) {
                    bestpair = c1;
                } else if (Math.abs(Dc2 - D) < EPS) {
                    bestpair = c2;
                } else {
                    System.out.println("FUCK!");
                }
                        
                Double[][][] c3 = splitpair(Px, Py, Math.pow(D, 0.5));
                
                if (c3 == null) {
                    return bestpair;
                }
                return c3;
            }
        }
        
        public static void main(String[] args) {   
 
            InputStream inputStream = System.in;
            InputReader in = new InputReader(inputStream);
            
            // timer stuffs
            long startTime = System.nanoTime();
            
            int N = in.nextInt();
            
            Double[][] ptsByX = new Double[N][2];
            Double[][] ptsByY = new Double[N][2];
            for (int i = 0; i < N; i++) {
                Double[] coords = {in.nextDouble(), in.nextDouble()};
                ptsByX[i] = coords; ptsByY[i] = coords;
            }
            
            // sort by x coord
            Arrays.sort(ptsByX, new Comparator<Double[]>(){
                @Override
                public int compare(Double[] doubles, Double[] otherDoubles) {
                    return doubles[1].compareTo(otherDoubles[1]); 
                }
            }); 
            Arrays.sort(ptsByX, new Comparator<Double[]>(){
                @Override
                public int compare(Double[] doubles, Double[] otherDoubles) {
                    return doubles[0].compareTo(otherDoubles[0]); 
                }
            }); 
            
            // sort by y-coord
            Arrays.sort(ptsByY, new Comparator<Double[]>(){
                public int compare(Double[] doubles, Double[] otherDoubles) {
                    return doubles[0].compareTo(otherDoubles[0]); 
                }
            }); 
            Arrays.sort(ptsByY, new Comparator<Double[]>(){
                public int compare(Double[] doubles, Double[] otherDoubles) {
                    return doubles[1].compareTo(otherDoubles[1]); 
                }
            }); 
            
            
            Double[][][] P = new Double[2][N][2];
            P[0] = ptsByX; P[1] = ptsByY;
            
            //long endTime = System.nanoTime();
            //long duration = (endTime - startTime)/1000000;
            //System.out.printf("%d ms\n", duration);
            
            Double[][][] res = new Double[2][2][2];
            res = solve(P);
           
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(10);
            System.out.println(df.format(Math.pow(dist(res[0][0], res[0][1]), 0.5)));
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;
           
            //System.out.printf("Num points: %d\n", N);
            //System.out.printf("%d ms\n", duration);
            
        }
}
 
class InputReader {
    public BufferedReader reader;
    public StringTokenizer tokenizer;
 
    public InputReader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream), 32768);
        tokenizer = null;
    }
 
    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
    }
 
    public int nextInt() {
        return Integer.parseInt(next());
    }
 
    public double nextDouble() {
        return Double.parseDouble(next());
    }
 
}
