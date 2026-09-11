import java.util.Scanner;
public class Main {

	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		
		int h = scan.nextInt();
		int w = scan.nextInt();
		String c = scan.next();
		String[][] flag = new String[w][h];
		flag[w/2][h/2] = c;
		
		
		for(int i = 0 ; i < w ; i++) {
			for(int j = 0 ; j < h ;j++) {
				if(flag[i][j] != null){
				}else if(i == 0 || i == w-1){
					flag[i][j] = "-";
				}else if(j == h-1 || j == 0) {
					flag[i][j] = "|";
				}else {
					flag[i][j] = ".";
				}
				if((i == 0  && j == 0)||(i == w-1 && j == 0)||(i == 0 && j == h-1)||(i == w-1 && j == h-1)) {
					flag[i][j] = "+";
				}
				System.out.print(flag[i][j]);
			}
			System.out.println();
		}
	}

}
