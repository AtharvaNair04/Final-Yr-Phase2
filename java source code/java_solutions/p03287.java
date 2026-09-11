import java.util.*;

public class Main{
    public void main(Scanner sc){
        int N=sc.nextInt();
        int M=sc.nextInt();
        int[] a=new int[N];
        long sum[]=new long[N+1];

        for(int i=0;i<N;i++){
            a[i]=sc.nextInt();
            sum[i+1]=sum[i]+a[i];            
        }
        Map<Integer,Long>map=new HashMap<>();
        for(int i=0;i<=N;i++){
            int mod=(int)(sum[i]%M);
            map.compute(mod,(k,v)->(v==null)?1:v+1);
        }

        long ans=0;
        for(Map.Entry<Integer,Long> entry:map.entrySet()){
            long v=entry.getValue();
            if(v>=2){
                ans+=v*(v-1)/2;
            }
        }
        System.out.println(ans);
    }

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        new Main().main(sc);
        sc.close();
    }
}
