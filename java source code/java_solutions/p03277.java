import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] asort = new int[n];
        int[] s = new int[n+1];
        for(int i=0; i<n; i++){
            a[i] = sc.nextInt();
            asort[i] = a[i];
        }
        sc.close();

        Arrays.sort(asort);

        int l = 0, r = n;
        long mans = 0;
        long m = ((long)n*(long)(n+1))%4 == 0 ? (long)n*(long)(n+1)/4 : (long)n*(long)(n+1)/4;

        while(r > l+1){
            int mid = (l+r)/2;
            int amid = asort[mid];
            for(int i=0; i<n; i++){
                if(a[i] >= amid){
                    s[i+1] = 1;
                }else{
                    s[i+1] = -1;
                }
            }
            for(int i=1; i<n+1; i++){
                s[i] = s[i-1] + s[i];
            }
            BinaryIndexTree bit = new BinaryIndexTree(2*n+1);
            mans = 0;
            for(int i=0; i<n+1; i++){
                mans += bit.sum(s[i] + n);
                bit.add(s[i]+n, 1);
            }
            if(mans >= m){
                l = mid;
            }else{
                r = mid;
            }
        }

        System.out.println(asort[l]);
    }
}

class BinaryIndexTree{
    private long[] bit;
 
    public BinaryIndexTree(int size){
        bit = new long[size];
    }

    public long sum(int i){
        long s = 0;
        while(i > 0){
            s += bit[i];
            i -= i & -i;
        }
        return s;
    }

    public void add(int i, long x){
        while(i < bit.length){
            bit[i] += x;
            i += i & -i;
        }
    }
}
