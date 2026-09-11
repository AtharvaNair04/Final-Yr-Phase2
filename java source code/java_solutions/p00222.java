//四つ子素数の大きさを配列に入れたらTime Limit Exceededでした.
//直しても直してもTime Limit Exceeded___ドツボです
//エラストテネスの篩を忘れていましたorz
// Respect2Dさんのコメントを見て気づきました。


import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);

    static int[] prime = new int[10000001];
    // 0:未判定
    // 1:素数
    // -1:素数ではない

    public static void main(String[] args){

	prepare();

	while(true){
	    int n = kbd.nextInt();
	    if(n==0) break;
	    solve(n);
	}
    }

    static void prepare(){
	prime[0] = prime[1] = -1;
	int i=2;
	for(; i<prime.length; i++){
	    if(prime[i] == 0){
		prime[i] = 1;
		for(int j=i+i; j<prime.length; j+=i) prime[j]=-1;
	    }
	}
    }

    static void solve(int n){

	while(!(prime[n]==1 && prime[n-2]==1 && prime[n-6]==1 && prime[n-8]==1)){
	    n--;
	}
	System.out.println(n);
    }

    /*
    static boolean prime(int a){
	int k = (int)Math.sqrt(a);
	for(int l=2; l<=k; l++){
	    if(a%l == 0) return false;
	}
	return true;
    }
    */
}
