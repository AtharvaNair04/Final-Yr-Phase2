import java.util.Scanner;

public class Main{

	int get(int t, int m){
		int r = 0;
		while(true){
			if(r+t<=m)r+=t;
			else return r;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if((n|m)==0)break;
			int[] a = new int[n];
			for(int i=0;i<n;i++)a[i]=sc.nextInt();
			int assign = 0;
			int time = get(a[0], m);
			int d = a[0];
			boolean f = false;
			for(int i=1;i<n;i++){
				int t = get(a[i], m);
				if(time < t)continue;
				if(time==t){
					if(a[i]<d){
						f = false;
						assign = i;
						d = a[i];
					}
					else if(a[i]==d){
						f = true;
					}
				}
				else{
					f = false;
					assign = i;
					d = a[i];
					time = t;
				}
			}
			System.out.println(f?n+"":++assign+"");
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
