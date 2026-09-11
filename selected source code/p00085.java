// ヨセフのおいも
import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }

    Scanner kbd = new Scanner(System.in);

    void run(){
	while(kbd.hasNext()){
	    boolean[] p = new boolean[kbd.nextInt()]; // 0 = 最後の番号
	    int t = kbd.nextInt();
	    if(p.length==0 && t==0) break;

	    
	    Arrays.fill(p, true);
	    int tcnt = p.length; // いる人の数
	    int remove = 0; // 抜ける人の番号
	    while(tcnt!=1){
		int i=0;
		while(i<t){
		    remove++;
		    remove = remove%p.length;
		    if(p[remove]){
			i++;
		    }
		}		
		//System.out.println(remove);
		p[remove] = false;
		tcnt--;
	    }
	    int ans = 0;
	    while(!p[ans]) ans++;
	    if(ans==0) ans = p.length;
	    System.out.println(ans);
	}
    }
}
