import java.util.*;

public class Main {
	int[] a;
	int sum;
	int n;
	int x;
	
	boolean check(){
		for(int i=0;i<x;i++){
			if(a[i]!=i+1) return false;
		}
		return true;
	}
	
	void sort(){
		boolean go;
		for(int i=0;i<n;i++) a[i]--;
		a[n] = n;
		for(int i=0;i<=n+1;i++){
			go = false;
			if(a[i]==0){
				for(int j=i+1;j<=n+1;j++){
					if(a[j]!=0){
						a[i] = a[j];
						a[j] = 0;
						go = true;
						break;
					}
				}
				if(go==false){
					n = i;
					i = n+2;
				}
			}
		}
	}
	
	void solve(){
		Scanner sc = new Scanner(System.in);
		
		while(true){
			n = sc.nextInt();
			if(n==0) break;
			sum = 0;
			int[] temp = new int[n];
			for(int i=0;i<n;i++){
				temp[i] = sc.nextInt();
				sum += temp[i];
			}
			a = new int[sum+2];
			boolean flag = true;
			int tri = 0;
			for(int i=1;;i++){
				tri += i;
				if(sum==tri){
					x = i;
					break;
				}
				if(sum<tri){
					flag = false;
					break;
				}
			}
			if(flag==false){
				System.out.println("-1");
			}else{
				for(int i=0;i<n;i++) a[i] = temp[i];
				flag = false;
				for(int i=0;i<10000;i++){
					if(check()==true){
						System.out.println(i);
						flag = true;
						break;
					}else{
						sort();
					}
				}
				if(flag==false) System.out.println("-1");
			}
		}
	}
	
	
	public static void main(String[] args) {
		new Main().solve();
	}	
}
