import java.util.*;
import java.io.*;

public class Main{
	public static void main(String[] args){
		new Main().solve();
	}

	void solve(){

		Scanner cin = new Scanner(System.in);
		while(true)
		{
			int h = cin.nextInt();
			int d = cin.nextInt();
			if(h==0) return;
			int[] a = new int[h];
			int[] b = new int[d];
			for(int i=0;i<h;i++){
				a[i] = cin.nextInt();
			}
			for(int i=0;i<d;i++){
				b[i] = cin.nextInt();
			}
			int ret = 0;
			boolean[] acheck = new boolean[h];
			boolean[] bcheck = new boolean[d];
			for(int i = 20;i >= 1; i--){
				boolean flag = false;
				do{
					flag = false;
					for(int j=0;j<h;j++){
						if(!acheck[j] && a[j] == i){
							acheck[j] = true;
							flag = true;
							break;
						}
					}
					for(int j=0;j<d;j++){
						if(!bcheck[j] && b[j] == i){
							bcheck[j] = true;
							flag = true;
							break;
						}
					}
					
					if(flag) ret += i;
				}while(flag);
			}
			System.out.println(ret);
			
		}
	}
}
