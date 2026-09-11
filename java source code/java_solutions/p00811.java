import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    boolean[] p;
    int m, a, b;

    void run(){
	p = new boolean[1000000];
	setP();
	while(sc.hasNext()){
	    m = sc.nextInt();
	    a = sc.nextInt();
	    b = sc.nextInt();

	    if(m!=0) solve();
	}
    }

    void solve(){
	double c = (double)a/b;
	int ans1 = -1;
	int ans2 = -1;
	int a = -1;
	for(int s=1; s<=m; s++)
	    if(p[s]){
		int t = m/s;
		while(t>=2){
		    if(p[t]){		
			double u = (double)s/t;
			if(s*t<=m && u<=1 && c<=u)
			    if(a < s*t){
				//System.out.println("**"+s+" "+t);
				ans1 = s;
				ans2 = t;
				a = s*t;
			    }
		    }
		    t--;
		}
	    }
	System.out.println(ans1+" "+ans2);
    }

    void setP(){
	Arrays.fill(p, true);
	p[0] = p[1] = false;
	for(int i=2; i<p.length; i++)
	    if(p[i])
		for(int k=i+i; k<p.length; k+=i)
		    p[k] = false;
    }
}
