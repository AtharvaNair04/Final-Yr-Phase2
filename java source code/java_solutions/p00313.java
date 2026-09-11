import java.util.Scanner;

public class Main{
	public static void main (String[] args) {

		Scanner sc=new Scanner(System.in);
		int a=sc.nextInt();

		int[] mono_bit=new int[a+1];
		int tmp=0;
		int[] bits= {1,2,4};
		for(int i=0; i<3; i++) {
			int kazu=sc.nextInt();
			for(int j=0; j<kazu; j++) {
				tmp=sc.nextInt();
				mono_bit[tmp]+=bits[i];
			}
		}
		int ninzu=0;

		for(int i=1; i<=a; i++) {
			if(mono_bit[i]==4 || mono_bit[i]==6 || mono_bit[i]==7) {
				ninzu++;
			}
		}
		System.out.println(ninzu);
	}
}
