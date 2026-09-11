import java.util.Arrays;
import java.util.Scanner;

//Usaneko Matrix
public class Main{
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(), U = sc.nextInt(), V = sc.nextInt(), m = sc.nextInt();
		int[] u = new int[1000001], v = new int[1000001];
		Arrays.fill(u, -1); Arrays.fill(v, -1);
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)u[sc.nextInt()]=i*n+j;
		for(int i=0;i<n;i++)for(int j=0;j<n;j++)v[sc.nextInt()]=i*n+j;
		int[] usagiV = new int[n], usagiH = new int[n], nekoV = new int[n], nekoH = new int[n];
		int uc1 = 0, uc2 = 0, vc1 = 0, vc2 = 0;
		int ru = 0, rv = 0;
		String res = "DRAW";
		while(m--!=0){
			int x = sc.nextInt();
			if(u[x]!=-1){
				int i = u[x]/n, j = u[x]%n;
				usagiV[j]++; usagiH[i]++;
				if(i==j){
					uc1++;
					if(uc1==n)ru++;
				}
				if(i+j==n-1){
					uc2++;
					if(uc2==n)ru++;
				}
				if(usagiV[j]==n)ru++;
				if(usagiH[i]==n)ru++;
			}
			if(v[x]!=-1){
				int i = v[x]/n, j = v[x]%n;
				nekoV[j]++; nekoH[i]++;
				if(i==j){
					vc1++;
					if(vc1==n)rv++;
				}
				if(i+j==n-1){
					vc2++;
					if(vc2==n)rv++;
				}
				if(nekoV[j]==n)rv++;
				if(nekoH[i]==n)rv++;
			}
			if(n==1){
				ru = Math.min(ru, 1); rv = Math.min(rv, 1);
			}
			if(U<=ru&&V<=rv)break;
			if(U<=ru){
				res = "USAGI"; break;
			}
			if(V<=rv){
				res = "NEKO"; break;
			}
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
