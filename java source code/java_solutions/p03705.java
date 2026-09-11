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
        long N = sc.nextLong();
        long A = sc.nextLong();
        long B = sc.nextLong();

        if(N==1){
            if(A==B) System.out.println(1);
            else System.out.println(0);
            return;
        }

        if(A == B){
            System.out.println(1);
        }else if(A > B){
            System.out.println(0);
        }else{
            System.out.println(B*(N-2)-A*(N-2)+1);
        }
    }
}
