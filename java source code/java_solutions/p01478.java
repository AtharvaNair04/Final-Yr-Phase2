import java.util.*; 
   
public class Main{ 
   
    int n; 
    int[][] c; 
    char[] per; 
    boolean[] used; 
    int goal,ans; 
    int[] v;
   
    void solve(){ 
        Scanner sc = new Scanner(System.in); 
   
        n = sc.nextInt(); 
        c = new int[n][n]; 
        for(int i=0; i<n; i++){ 
            for(int j=0; j<n; j++){ 
                int cc = sc.nextInt(); 
                c[i][j] = cc; 
                c[j][i] = cc; 
            } 
        }

        v = new int[8765433];
        Arrays.fill(v,-1);
        dijkstra();
 
        int max = 0;
        for(int i=0; i<v.length; i++) max = Math.max(max,v[i]);
   
        System.out.println(max); 
    } 
   
    void dijkstra(){ 
        //permutation,cost 
        PriorityQueue<int[]>  
            q = new PriorityQueue<int[]>(n, new Comparator<int[]>(){ 
                    public int compare(int[] a, int[] b){ 
                        return a[1] - b[1]; 
                    } 
                }); 
   
        String sss = "";
        for(int i=1; i<=n; i++) sss += Integer.toString(i);
        q.add(new int[]{Integer.parseInt(sss),0}); 
 
        while(q.size()>0){ 
            int[] pp = q.poll(); 
            int perm = pp[0]; 
            int cost = pp[1]; 
   
            if(v[perm/10]!=-1) continue; 
            v[perm/10] = cost; 
   
            char[] permm = Integer.toString(perm).toCharArray(); 
   
            for(int i=0; i<n; i++){ 
                for(int j=i+1; j<n; j++){ 
                    char tmp = permm[i]; 
                    permm[i] = permm[j]; 
                    permm[j] = tmp; 
                    q.add(new int[]{Integer.parseInt(new String(permm)), cost+c[i][j]}); 
                    tmp = permm[i]; 
                    permm[i] = permm[j]; 
                    permm[j] = tmp; 
                }    
            } 
        } 
    } 
   
    public static void main(String[] args){ 
        new Main().solve(); 
    } 
}
