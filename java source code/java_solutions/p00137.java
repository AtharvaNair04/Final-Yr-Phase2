import java.util.Scanner;
public class Main{
	public static void main(String[] args){
		Scanner s=new Scanner(System.in);
		int n=s.nextInt();
		for(int i=0;i<n;i++){
			int x=s.nextInt();
			System.out.println("Case "+(i+1)+":");
			for(int j=0;j<10;j++){
				x*=x;
				String str=String.format("%08d",x);
				x=Integer.parseInt(str.substring(2, 6));
				System.out.println(x);
			}
		}
	}
	

}
