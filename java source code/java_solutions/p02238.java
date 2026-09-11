import java.util.ArrayList;
import java.util.Scanner;

class adj_list{
    public int u,k;
    public int[] v;
}

class timestamp{
    public boolean d_flg = false;
    public int id,f,d;
}

public class Main {

    private static ArrayList<adj_list>  ar = new ArrayList<adj_list>();
    private static ArrayList<timestamp> t  = new ArrayList<timestamp>();

    private static final int U     =  0,
                             K     =  1,
                             START =  1,
                             END   = -1;

    private static int times = 0;

    public static void main(String[] args) {

        //input
         Scanner sc = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));
         int      N = sc.nextInt();

         adj_list a;String[] s;
         for(int i = 0;i < N;i++){
             s = sc.next().split("\\s");
             a = new adj_list();
             a.u = Integer.parseInt(s[U]);
             a.k = Integer.parseInt(s[K]);
             a.v = new int[a.k];
             for(int j=0;j<a.k;j++){a.v[j] = Integer.parseInt(s[j+2]);}
             ar.add(a);
             t.add(new timestamp());
         }
         for(int i=0;i<N;i++){t.get(i).id = i+1;}

         //calculation
         int V = START;
         while(V != END){
        	 DFS(V);
        	 V = getNextVertex(V);
         }

         //output
         for (timestamp ts:t) {System.out.println(ts.id + " " + ts.d + " " + ts.f);}
    }

    private static int getNextVertex(int v){
    	int ret = END;
    	for(timestamp ts:t){if(v < ts.id && !ts.d_flg){ret = ts.id;break;}}
    	return ret;
    }

    private static void DFS(int id){
        timestamp out;
        out = get_OUT_list(id);
        if   (out.d_flg){return;}
        else {out.d_flg = true;}
        out.d = ++times;
        adj_list in;
        in  = get_IN_list(id);
        for(int i=0;i<in.k;i++){DFS(in.v[i]);}
        out.f = ++times;
    }

    private static adj_list get_IN_list(int id){
        adj_list ret = null;
        for(adj_list a:ar){if(a.u == id){ret = a;}}
        return ret;
    }

    private static timestamp get_OUT_list(int id){
        timestamp ret = null;
        for(timestamp ts:t){if(ts.id == id){ret = ts;}}
        return ret;
    }
 }
