import java.util.Scanner;

//Search of Concatenated Strings
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		byte[][] e = new byte[5000][1<<12];
		boolean[][] f = new boolean[5000][12];
		for(;;){
			int n = sc.nextInt(), m = sc.nextInt();
			if((n|m)==0)break;
			String[] s = new String[n];
			for(int i=0;i<n;i++)s[i]=sc.next();
			StringBuilder sb = new StringBuilder();
			while(m--!=0)sb.append(sc.next());
			String t = sb.toString();
			int N = t.length();
			for(int i=0;i<N;i++)for(int j=0;j<1<<n;j++)e[i][j]=0;
			for(int i=1;i<=N;i++){
				String sub = t.substring(0, i);
				for(int j=0;j<n;j++){
					f[i-1][j]=sub.endsWith(s[j]);
					if(f[i-1][j])e[i-1][1<<j]=1;
				}
			}
			int res = 0;
			for(int i=0;i<N;i++)for(int S=0;S<1<<n;S++){
				if(e[i][S]==0)continue;
				if(S==(1<<n)-1)res++;
				for(int j=0;j<n;j++)if(((S>>j)&1)==0){
					if(i+s[j].length()<N&&f[i+s[j].length()][j])e[i+s[j].length()][S|(1<<j)]=1;
				}
			}
			System.out.println(res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
