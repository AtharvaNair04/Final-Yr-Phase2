import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
static BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
static PrintWriter pw =new PrintWriter(new OutputStreamWriter(System.out));
static StringTokenizer st;
static int N,K =0;
static int INF = (int) (1e9+5);
    public static void main(String[] args) throws IOException {
	// write your code here
        //st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(br.readLine());
        //K = Integer.parseInt(st.nextToken());
        ArrayList<Integer> dp = new ArrayList<>();
        dp.add(0);
        dp.add(0);
        dp.add(0);
        for (int day =0;day<N;day++){
            st = new StringTokenizer(br.readLine());
            ArrayList<Integer> newDp = new ArrayList<>();
            ArrayList<Integer> cost = new ArrayList<>();
            for (int i=0;i<3;i++) {
                cost.add(Integer.parseInt(st.nextToken()));
                newDp.add(0);
            }
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    if (i!=j)
                    newDp.set(j,Math.max(newDp.get(j),dp.get(i)+cost.get(j)));
                }
            }
            dp.clear();
           for (int i=0;i<3;i++)
               dp.add(newDp.get(i));
        }




        pw.println(Math.max(Math.max(dp.get(0),dp.get(1)),dp.get(2)));
        pw.flush();
    }
}
