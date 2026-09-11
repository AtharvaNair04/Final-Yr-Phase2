import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int m=sc.nextInt();
		for(int i=0; i<m; i++){
			int money=sc.nextInt();//初期資金
			int year=sc.nextInt();
			int n=sc.nextInt();
			int max=0;
			for(int j=0; j<n; j++){
				int f=sc.nextInt();
				double d=sc.nextDouble();
				int hand=sc.nextInt();
				int sum=0;
				int temp=money;
				if(f==0){//単利
					//この場合のsumは単利と手数料の合計
					for(int k=0; k<year ;k++){
						sum += (int)Math.floor(temp*d);
						temp -= hand;
					}
					sum += temp;//なので最後に初期資金を足す
				}
				else if(f==1){//複利
					//この場合のsumは最終金額
					sum=money;
					for(int k=0; k<year ;k++){
						sum += (int)Math.floor(sum*d);
						sum -= hand;
					}
				}
				//System.out.println("j="+(j+1)+" sum="+sum);
				max=Math.max(max, sum);
			}
			System.out.println(max);
		}

	}

}
