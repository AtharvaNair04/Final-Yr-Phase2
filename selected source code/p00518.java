import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt();
		String kye=sc.next();
		int sche[][]=new int[7][n];
		int v=10007;
		int patt=0;
		
		if(kye.charAt(0)=='J'){
			sche[0][0]=sche[3][0]=sche[4][0]=sche[6][0]=1;
		}
		else if(kye.charAt(0)=='O'){
			sche[3][0]=sche[6][0]=1;
		}
		else if(kye.charAt(0)=='I'){
			sche[4][0]=sche[6][0]=1;
		}//おk
		for(int i=1;i<kye.length();i++){
			if(kye.charAt(i)=='J'){
				sche[0][i]=(sche[0][i-1]+sche[3][i-1]+sche[4][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='O'){
				sche[1][i]=(sche[1][i-1]+sche[3][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='I'){
				sche[2][i]=(sche[2][i-1]+sche[4][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='J' || kye.charAt(i)=='O'){
				sche[3][i]=(sche[0][i-1]+sche[1][i-1]+sche[3][i-1]+sche[4][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='J' || kye.charAt(i)=='I'){
				sche[4][i]=(sche[0][i-1]+sche[2][i-1]+sche[3][i-1]+sche[4][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='O' || kye.charAt(i)=='I'){
				sche[5][i]=(sche[1][i-1]+sche[2][i-1]+sche[3][i-1]+sche[4][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
			if(kye.charAt(i)=='J' || kye.charAt(i)=='O' ||kye.charAt(i)=='I'){
			sche[6][i]=(sche[0][i-1]+sche[1][i-1]+sche[2][i-1]+sche[3][i-1]+sche[4][i-1]+sche[5][i-1]+sche[6][i-1])%v;
			}
		}
		for(int j=0;j<7;j++){
			patt=patt%v+sche[j][n-1]%v;
		}
		System.out.println(patt%v);
	}

}
