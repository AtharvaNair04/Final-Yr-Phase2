import java.util.Scanner;
public class Main{
	int furo,puru;
	int fken,pken;
	int niti;
	int ryo;
	int gokei;
	int wgokei;
	
	public void solve(){
		Scanner sc = new Scanner(System.in);
		niti = sc.nextInt();
		for(int i=0;i<niti;i++){
			furo = sc.nextInt();
			puru = sc.nextInt();
			fken = sc.nextInt();
			pken = sc.nextInt();
			gokei = furo*fken+puru*pken;
			if(fken<5){
				fken = 5;
			}
			if(pken<2){
				pken = 2;
			}
			wgokei=(furo*fken+puru*pken)/10*8;
			if(gokei>wgokei){
				System.out.println(wgokei);
			}else{
				System.out.println(gokei);
			}
		}
	}
	public static void main(String[] args){
		Main obj = new Main();
		obj.solve();
	}
}
