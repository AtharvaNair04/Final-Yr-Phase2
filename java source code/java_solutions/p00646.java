import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//No Story
public class Main{
	
	int[] c;
	int n, res;
	
	void dfs(int k, int r){
		if(k==n){
			res+=r; return;
		}
		for(int i=0;i<c[k];i++)dfs(k+1, r);
		dfs(k+1, r*(c[k]+1));
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int N = 1000000;
		boolean[] p = new boolean[N+1];
		Arrays.fill(p, true);
		p[0]=p[1]=false;
		List<Integer> l = new ArrayList<Integer>();
		for(int i=2;i<=N;i++)if(p[i]){
			l.add(i);
			for(int j=i+i;j<=N;j+=i)p[j]=false;
		}
		for(;;){
			long L = sc.nextLong();
			if(L==0)break;
			c = new int[100];
			n = 0;
			int i = 0;
			while(L!=1&&i<l.size()){
				long x = l.get(i);
				if(L%x==0){
					while(L!=1&&L%x==0){
						c[n]++;
						L/=x;
					}
					n++;
				}
				i++;
			}
			if(i==l.size()){
				c[n] = 1; n++;
			}
			res = 0;
			dfs(0, 1);
			System.out.println(++res>>1);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
