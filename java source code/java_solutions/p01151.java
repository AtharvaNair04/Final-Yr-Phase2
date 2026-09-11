import java.util.Scanner;

//Divisor is the Conqueror
public class Main{

	int n;
	int[] c, res;
	
	boolean dfs(int k, int rest){
		if(k<0)return true;
		for(int i=1;i<14;i++){
			if(c[i]==0||(rest-i)%i!=0)continue;
			c[i]--; res[k] = i;
			if(dfs(k-1, rest-i))return true;
			c[i]++;
		}
		return false;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			n = sc.nextInt();
			if(n==0)break;
			c = new int[14]; res = new int[n];
			int s = 0;
			for(int i=0;i<n;i++){
				int x = sc.nextInt();
				s += x;
				c[x]++;
			}
			if(dfs(n-1, s)){
				for(int i=0;i<n;i++)System.out.print(res[i]+(i==n-1?"\n":" "));
			}
			else System.out.println("No");
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
