import java.util.Scanner;
 
public class Main {
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		new Main();
	}
	public Main() {
		new aoj230().doIt();
	}
	class aoj230{
		
		void up(int a[],int b[],int a2[],int b2[],int n){
			for(int i = 0;i < n-1;i++){
				if(a[i] == 1 && a[i+1] == 1 && a2[i] == 1){
					a2[i+1] = a2[i];
					a2[i] = 0;
				}
				if(b[i] == 1 && b[i+1] == 1 && b2[i] == 1){
					b2[i+1] = b2[i];
					b2[i] = 0;
				}
			}
		}
		
		void down(int a[],int b[],int a2[],int b2[],int n){
			for(int i = n - 1;i > 0;i--){
				if(a[i] == 2 && a2[i] == 1){
					a2[i-1] = a2[i];
					a2[i] = 0;
				}
				if(b[i] == 2 && b2[i] == 1){
					b2[i - 1] = b2[i];
					b2[i] = 0;
				}
			}
		}
		
		void jump(int a[],int b[],int n){
			int a2[] = new int [n+2];
			int b2[] = new int [n+2];
			for(int i = 0;i < n;i++){
				if(a[i] == 1){
					b2[i] = a[i];
					b2[i+1] = a[i];
					b2[i+2] = a[i];
				}
				if(b[i] == 1){
					a2[i] = b[i];
					a2[i+1] = b[i];
					a2[i+2] = b[i];
				}
			}
			for(int i = 0;i < n;i++){
				a[i] = a2[i];
				b[i] = b2[i];
			}
		}
		
		int tower(int a[],int b[],int n){
			int result = 0;
			int a2[] = new int [n];
			int b2[] = new int [n];
			a2[0] = 1;b2[0] = 1;
//			for(int i = n-1;i >= 0;i--)System.out.println(i+":"+a[i]+" "+b[i]);
//			System.out.println("---------------");
			while(true){
				up(a,b,a2,b2,n);
				down(a,b,a2,b2,n);
				if(a2[n-1] == 1 || b2[n-1] == 1)return result;
				if(result == 1000)break;
//				for(int i = n-1;i >= 0;i--)System.out.println(i+":"+a2[i]+" "+b2[i]);
//				System.out.println("---------------");
				jump(a2,b2,n);
				result++;
			}
			return -1;
		}
		void doIt() {
			while(true){
				int n = sc.nextInt();
				int a[] = new int [n];
				int b[] = new int [n];
				if(n == 0)break;
				for(int i = 0;i < n;i++)a[i] = sc.nextInt();
				for(int i = 0;i < n;i++)b[i] = sc.nextInt();
				int cnt = tower(a,b,n);
				if(cnt != -1)System.out.println(cnt);
				else System.out.println("NA");
			}
		}
	}
}
