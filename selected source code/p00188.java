import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		new Main().run();
	}
	void run(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int b = sc.nextInt();
			if(b == 0){
				//System.out.println("break");
				break;
			}
			int[] a = new int[b];
			int left,right,searchvalue;
			for(int i=0; i < a.length; i++){
				a[i] = sc.nextInt();
			}
			searchvalue = sc.nextInt();
			left = 0; right = a.length-1;
			System.out.println(solve(left, right, searchvalue, a));
		}
	}

	int solve(int left, int right,int sv,int[] a){
		int mid = (left + right) / 2;
		if(a[mid] == sv){
			return 1;
		}else if(left > right){
			return 0;
		}else if(a[mid] < sv){
			return 1 + solve(++mid, right, sv, a);
		}else if(a[mid] > sv){
			return 1 + solve(left, --mid, sv, a);			
		}
		return 0;
	}
}
