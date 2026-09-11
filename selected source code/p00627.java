import java.util.*;

class Main{
	public static void main(String[] args) {
		
		Scanner sc=new Scanner(System.in);
		while(sc.hasNextInt()){
		int n=sc.nextInt();
		if(n==0)break;
		int ans=0;
		for(int i=0;i<n/4;i++){
			ans+=sc.nextInt();
		}
		System.out.println(ans);
		}
	}
}
