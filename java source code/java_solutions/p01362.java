import static java.lang.System.*;

import java.util.Random;
import java.util.Scanner;

class Main {
	public static Scanner sc = new Scanner(in);
	public static Random rand=new Random();


	public boolean check(char c1,char c2,char c3,char d1,char d2,char d3){
		return (c1=='*' ||c2=='*' || c3=='*') &&  (d1=='*' ||d2=='*' || d3=='*');
	}

	public void run() {
		TCase:while(true){
			char[][][] dice=new char[6][3][3];

			for(int i=0;i<6;i++){
				for(int j=0;j<3;j++){
					dice[i][j]=sc.next().toCharArray();
					if(dice[i][j][0]=='#')return;
				}
			}

			if(check(dice[0][2][0],dice[0][2][1],dice[0][2][2],
					dice[2][2][0],dice[2][2][1],dice[2][2][2])){
				ln(0);continue;
			}

			if(check(dice[0][0][0],dice[0][1][0],dice[0][2][0],
					dice[2][0][2],dice[2][1][2],dice[2][2][2])){
				ln(1);continue;
			}
			if(check(dice[0][0][2],dice[0][1][2],dice[0][2][2],
					dice[2][0][0],dice[2][1][0],dice[2][2][0])){
				ln(1);continue;
			}
			if(check(dice[4][2][0],dice[4][2][1],dice[4][2][2],
					dice[5][0][0],dice[5][0][1],dice[5][0][2])){
				ln(1);continue;
			}
			if(check(dice[4][0][0],dice[4][0][1],dice[4][0][2],
					dice[5][2][0],dice[5][2][1],dice[5][2][2])){
				ln(1);continue;
			}

			if(check(dice[0][0][0],dice[0][0][1],dice[0][0][2],
					dice[2][0][0],dice[2][0][1],dice[2][0][2])){
				ln(2);continue;
			}
			if(check(dice[4][0][0],dice[4][1][0],dice[4][2][0],
					dice[5][0][0],dice[5][1][0],dice[5][2][0])){
				ln(2);continue;
			}
			if(check(dice[4][0][2],dice[4][1][2],dice[4][2][2],
					dice[5][0][2],dice[5][1][2],dice[5][2][2])){
				ln(2);continue;
			}
			if(check(dice[1][0][0],dice[1][1][0],dice[1][2][0],
					dice[3][0][2],dice[3][1][2],dice[3][2][2])){
				ln(2);continue;
			}
			if(check(dice[1][0][2],dice[1][1][2],dice[1][2][2],
					dice[3][0][0],dice[3][1][0],dice[3][2][0])){
				ln(2);continue;
			}


			if(check(dice[1][0][0],dice[1][0][1],dice[1][0][2],
					dice[3][0][0],dice[3][0][1],dice[3][0][2])){
				ln(3);continue;
			}
			if(check(dice[1][2][0],dice[1][2][1],dice[1][2][2],
					dice[3][2][0],dice[3][2][1],dice[3][2][2])){
				ln(3);continue;
			}
		}
	}
	public static void main(String[] args) {
		new Main().run();
	}

	public int[] nextIntArray(int n){
		int[] res=new int[n];
		for(int i=0;i<n;i++){
			res[i]=sc.nextInt();
		}
		return res;
	}
	public static void pr(Object o) {
		out.print(o);
	}
	public static void ln(Object o) {
		out.println(o);
	}
	public static void ln() {
		out.println();
	}
}
