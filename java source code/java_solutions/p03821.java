import java.util.*;
class Main{
    /*
     * 整数 -> nextInt() (遅い場合は下のni())
     * 単語 -> next()
     * 1行 -> nextLine()
     * 注意 : nextLIneをnext(),nextInt()のあとに呼び出す場合、次が改行だと改行だけ取ってしまうので注意
     */
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        long[] a = new long[N];
        long[] b = new long[N];
        for(int i=0;i<N;i++){
            a[i] = sc.nextLong();
            b[i] = sc.nextLong();
        }

        long add = 0;
        for(int i=N-1;i>=0;i--){
            a[i] += add;

            long t = (a[i] + b[i] - 1) / b[i] * b[i] - a[i];
            add += t;
        }

        System.out.println(add);
    }
}
