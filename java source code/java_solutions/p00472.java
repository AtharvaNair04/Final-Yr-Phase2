import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n, m;
    int[] dis;
    int ans;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    m = sc.nextInt();
	    dis = new int[n+1];
	    for(int i=2; i<dis.length; i++)
		dis[i] = dis[i-1]+sc.nextInt();

	    ans = 0;
	    int here = 1;
	    for(int k=0; k<m; k++){
		int next = sc.nextInt() + here;
		ans += Math.abs(dis[next] - dis[here]);
		ans %= 100000;
		here = next;
	    }
	    System.out.println(ans);
	}
    }
}
