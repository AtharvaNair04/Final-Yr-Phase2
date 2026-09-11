import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//Beehives
public class Main{

	int[][] d = {{0,1},{-1,0},{-1,-1},{0,-1},{1,0},{1,1}};
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt(), M = 150;
		sc.nextLine();
		while(T--!=0){
			char[] s = sc.nextLine().toCharArray(), t = sc.nextLine().toCharArray();
			sc.nextLine();
			Set<Integer> set = new HashSet<Integer>();
			int x = 100, y = 100;
			set.add(x*M+y);
			for(char c:s){
				x+=d[c-'a'][0]; y+=d[c-'a'][1];
				set.add(x*M+y);
			}
			boolean res = false;
			if(s.length==t.length){
				for(int S:set){
					if(res)break;
					for(int k=0;k<6;k++){
						x = S/M; y = S%M;
						int cnt = 1;
						for(char c:t){
							x+=d[(c-'a'+k)%6][0]; y+=d[(c-'a'+k)%6][1];
							if(!set.contains(x*M+y))break;
							cnt++;
						}
						if(cnt==set.size()){
							res = true; break;
						}
					}
				}
			}
			System.out.println(res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
