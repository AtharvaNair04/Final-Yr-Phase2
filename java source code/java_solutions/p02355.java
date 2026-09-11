import java.util.Scanner;

public class Main {
    //http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=DSL_3_A
    //http://judge.u-aizu.ac.jp/onlinejudge/review.jsp?rid=2692179#1 -- reference
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int K = in.nextInt();//target
        int a[] = new int[n+1];
        for(int i = 1; i<=n ;i++){
            a[i] = in.nextInt();
        }
        int tag[] = new int[K+1];
        //use two pointer
        int p1 = 0, p2 = 0;
        int min = n+1;//smallest size
        int sum = 0;
        //moving the pointer and check the sum < k
        while(p1<=n){
            //p2 = p1;
            if(sum==K) {
                min = Math.min(min, p2-p1);
                //System.out.println(min);
            }
            //main body
            //move right pointer
            if(sum<K){
                p2++;
                if(p2>n) break;
                if(a[p2] <= K){
                    if(tag[a[p2]]==0) sum++;
                    tag[a[p2]]++;
                }
            }else {
                p1++;
                if(p1>n) break;
                if(a[p1]<=K){
                    tag[a[p1]]--;
                    if(tag[a[p1]]==0) sum--;
                }
            }

        }
        if(min == n+1) System.out.println(0);
        else System.out.println(min);

    }

}
