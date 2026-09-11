import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    // 整数秒である必要はない
    double min, y, t;
    int ans;

    void run(){
	while(sc.hasNext()){
	    min = sc.nextDouble();
	    
	    t = min/9.8;

	    y = t*t*4.9;
	    ans = 0;
	    while(5*ans-5 < y)	ans++;
	    //System.out.println(v+" "+y+" "+t);
	    System.out.println(ans);
	}
    }
}
