import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int w;
    int[] fd = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
    boolean[] use;

    void run(){
	while(sc.hasNext()){
	    w = sc.nextInt();
	    use = new boolean[10];
	    solve();
	    show(use);
	}
    }

    void solve(){
	for(int i=fd.length-1; i>=0; i--)
	    if(w>=fd[i]){
		w -= fd[i];
		use[i] = true;
	    }
    }

    void show(boolean[] a){
	StringBuffer sb = new StringBuffer();
	int i=0;
	while(!use[i]) i++;
	sb.append(fd[i]);
	i++;
	for(; i<a.length; i++)
	    if(a[i]) sb.append(" "+fd[i]);
	System.out.println(sb.toString());
    }
}
