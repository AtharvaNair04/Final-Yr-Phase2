import java.util.*;
class Main{
	void aa(){
		Scanner sc=new Scanner(System.in);
		while(true){
			int A=sc.nextInt();
			if(A==0)break;
			long B=0;
			int[] D=new int[A-1];
			for(int i=0;i<A;i++)B+=sc.nextInt();
			for(int i=0;i<A-1;i++)D[i]=sc.nextInt();
			Arrays.sort(D);
			long max=0;
			for(int i=A;i>0;i--){
				if(max<B*i)max=B*i;
				if(i!=1)B+=D[i-2];
			}
			System.out.println(max);
		}
	}
	public static void main(String[]arg){
		new Main().aa();
	}
}
