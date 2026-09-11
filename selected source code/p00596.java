import java.util.Scanner;

//Dominoes Arrangement
public class Main{

	void f(int k){
		u[k] = true;
		for(int i=0;i<7;i++){
			if(!u[i]&&r[k][i])f(i);
		}
	}
	
	boolean[][] r;
	boolean[] u;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			int n = sc.nextInt();
			int[] c = new int[7];
			r = new boolean[7][7];
			for(int i=0;i<n;i++){
				char[] s = sc.next().toCharArray();
				int a = s[0]-'0';
				int b = s[1]-'0';
//				if(a==b)continue;
				c[a]++;
				c[b]++;
				r[a][b] = r[b][a] = true;
			}
			u = new boolean[7];
			for(int i=0;i<7;i++){
				if(c[i]>0){
					f(i);
					break;
				}
			}
			boolean ok = true;
			for(int i=0;i<7;i++)if(!u[i]&&c[i]>0)ok = false;
			int o = 0;
			for(int i=0;i<7;i++)if(c[i]%2==1)o++;
			System.out.println((o==0||o==2)&&ok?"Yes":"No");
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
