import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class Main {
    public static void main(String[] args) {
        FastReader read = new FastReader();
        int n = read.nextInt();
        int array[] = new int[n];
        long sum1 = 0;
        long sum2 = 0;
        for(int i = 0; i<n; i++){
            array[i] = read.nextInt();
                
        }
        int p1 = 0;
        int p2 = n-1;
        while(p1-1!=p2){
            if(sum1>sum2){
                sum2+=array[p2];
                p2--;
            }
            else{
                sum1+= array[p1];
                p1++;
            }
        }
        System.out.println(Math.abs(sum1-sum2));
    }
    static class FastReader 
    { 
        BufferedReader br; 
        StringTokenizer st; 
  
        public FastReader() 
        { 
            br = new BufferedReader(new
                     InputStreamReader(System.in)); 
        } 
  
        String next() 
        { 
            while (st == null || !st.hasMoreElements()) 
            { 
                try
                { 
                    st = new StringTokenizer(br.readLine()); 
                } 
                catch (IOException  e) 
                { 
                    e.printStackTrace(); 
                } 
            } 
            return st.nextToken(); 
        } 
  
        int nextInt() 
        { 
            return Integer.parseInt(next()); 
        } 
  
        long nextLong() 
        { 
            return Long.parseLong(next()); 
        } 
  
        double nextDouble() 
        { 
            return Double.parseDouble(next()); 
        } 
  
        String nextLine() 
        { 
            String str = ""; 
            try
            { 
                str = br.readLine(); 
            } 
            catch (IOException e) 
            { 
                e.printStackTrace(); 
            } 
            return str; 
        } 
    }
}
