import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		//配列の作成
		int[][][] OHouse=new int [4][3][10];
		for(int z=0;z<4;z++) {
			for(int y=0;y<3;y++) {
				for(int x=0;x<10;x++) {
					OHouse[z][y][x]=0;
				}
			}
		}

		//配列の加工(入力処理)
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n=Integer.parseInt(br.readLine());
		int count=1;

		while(n>=count) {
			StringTokenizer st=new StringTokenizer(br.readLine());
			int b=Integer.parseInt(st.nextToken());
			int f=Integer.parseInt(st.nextToken());
			int r=Integer.parseInt(st.nextToken());
			int v=Integer.parseInt(st.nextToken());
			OHouse[b-1][f-1][r-1]=OHouse[b-1][f-1][r-1]+v;
			if(OHouse[b-1][f-1][r-1]<0) {
				OHouse[b-1][f-1][r-1]=0;
			}
			count++;
		}

		//配列の出力
		for(int z=0;z<4;z++) {
			for(int y=0;y<3;y++) {
				for(int x=0;x<10;x++) {
					System.out.print(" "+OHouse[z][y][x]);
					if(x==9) {
						System.out.println("");
					}
				}
			}
			if(z<3) {
				for(int i=0;i<20;i++) {
					System.out.print("#");
					if(i==19) {
						System.out.println("");
					}
				}
			}
		}
	}

}
