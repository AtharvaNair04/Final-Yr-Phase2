import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n, s;
    int ans;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    s = sc.nextInt();
	    if(n!=0){
		ans = 0;
		solve(0, 0, 0);
		System.out.println(ans);
	    }
	}
    }

    void solve(int level, int sum, int use){
	if(use==n){
	    if(sum==s) ans++;
	    return;
	}
	if(level>=10 || use>n || sum>s) return;

	solve(level+1, sum+level, use+1);
	solve(level+1, sum, use);
    }
}
