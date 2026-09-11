import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int H=sc.nextInt();
		int W=sc.nextInt();
		int K=sc.nextInt();
		char[][] c=new char[H][W];

		for(int i=0;i<H;i++) {
			String s=sc.next();
			c[i]=s.toCharArray();
		}

		int answer=0;
		for(int h=0;h<(Math.pow(2, H));h++) {     //行はh通りある
			for(int w=0;w<(Math.pow(2, W));w++) {  //列はw通りある
        int blackcount=0;
				for(int row=0;row<H;row++) {        //＃かどうか確認する行
					for(int col=0;col<W;col++) {      //＃かどうか確認する列
						if(((h>>row & 1)==1) && ((w>>col & 1)==1)) { //行はrow個、列はcol個ずらす
							if (c[row][col] == '#')blackcount++; //＃があった
						}
					}
				}
				if(blackcount==K)answer++;
			}
		}

	    System.out.println(answer);
	}
}
