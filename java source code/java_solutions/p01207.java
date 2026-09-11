import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//Hit and Blow
public class Main{

	int get(int x, int k){
		return k==0?x/1000:k==1?x/100%10:k==2?x/10%10:x%10;
	}
	
	int[] f(int A, int B){
		int[] a = new int[4], b = new int[4];
		for(int i=0;i<4;i++){
			a[i] = get(A, i);
			b[i] = get(B, i);
		}
		int hit = 0, blow = 0;
		for(int i=0;i<4;i++)for(int j=0;j<4;j++)if(a[i]==b[j]){
			if(i==j)hit++;
			else blow++;
		}
		return new int[]{hit, blow};
	}
	
	void run(){
		boolean[] v = new boolean[10000];
		for(int i=123;i<=9876;i++){
			Set<Integer> s = new HashSet<Integer>();
			for(int j=0;j<4;j++)s.add(get(i, j));
			v[i] = s.size()==4;
		}
		Scanner sc = new Scanner(System.in);
		boolean[] cand = new boolean[10000];
		int[][] p = new int[5][5];
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			for(int i=0;i<10000;i++)cand[i] = v[i];
			int[] x = new int[n], h = new int[n], b = new int[n];
			for(int i=0;i<n;i++){
				x[i] = sc.nextInt(); h[i] = sc.nextInt(); b[i] = sc.nextInt();
				for(int j=123;j<=9876;j++)if(cand[j]){
					int[] r = f(j, x[i]);
					cand[j]&=r[0]==h[i]&&r[1]==b[i];
				}
			}
			List<Integer> l = new ArrayList<Integer>();
			for(int i=123;i<=9876;i++)if(cand[i])l.add(i);
			if(l.size()==1){
				System.out.printf("%04d\n", l.get(0)); continue;
			}
			int res = -1;
			for(int i=123;i<=9876;i++)if(v[i]){
				for(int[]a:p)Arrays.fill(a, 0);
				for(int s:l){
					int[] r = f(s, i);
					p[r[0]][r[1]]++;
					if(1 < p[r[0]][r[1]])break;
				}
				boolean ok = true;
				for(int j=0;j<5;j++)for(int k=0;k<5;k++)if(1 < p[j][k])ok = false;
				if(ok){
					res = i; break;
				}
			}
			if(res==-1)System.out.println("????");
			else System.out.printf("%04d\n", res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
