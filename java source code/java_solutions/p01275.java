import java.util.Scanner;

//Dial Lock
public class Main{

	int n, res;
	int[] now, ans;
	
	void f(int k, int cnt){
		if(res<=cnt)return;
		if(k==n){
			res = cnt; return;
		}
		if(now[k]==ans[k])f(k+1, cnt);
		else{
			int dif = ans[k]-now[k];
			if(dif<0)dif+=10;
			for(int j=k;j<n;j++){
				now[j] = (now[j]+dif)%10;
				f(k+1, cnt+1);
			}
			for(int j=k;j<n;j++)now[j] = (now[j]+10-dif)%10;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			n = sc.nextInt();
			if(n==0)break;
			now = new int[n];
			ans = new int[n];
			char[] s = sc.next().toCharArray();
			for(int i=0;i<n;i++)now[i]=s[i]-'0';
			s = sc.next().toCharArray();
			for(int i=0;i<n;i++)ans[i]=s[i]-'0';
			res = n;
			f(0, 0);
			System.out.println(res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
