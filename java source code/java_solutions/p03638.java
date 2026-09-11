import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class Main{
	
	public static void main(String[] args) throws IOException{
		//File file = new File("input.txt");
		//Scanner in = new Scanner(file);
		Scanner in = new Scanner(System.in);
		
		int H = in.nextInt();
		int W = in.nextInt();
		int N = in.nextInt();
		String[][] sList = new String[H][W];
		int indexH = 0;
		int indexW = 0;
		
		for(int i = 0; i < N; i++){
			int a = in.nextInt();
			int c = i + 1;
			for(int j = 0; j < a; j++){
				sList[indexH][indexW] = "" + c;
				indexW++;
				if(indexW == W){
					indexH++;
					indexW = 0;
				}
			}
		}
		
		for(int i = 0; i < H; i++){
			if(i % 2 == 0){
				for(int j = 0; j < W - 1; j++){
					System.out.print(sList[i][j] + " ");
				}
				System.out.println(sList[i][W-1]);
			}
			else{
				for(int j = W - 1; j >= 1; j--){
					System.out.print(sList[i][j] + " ");
				}
				System.out.println(sList[i][0]);
			}
		}
	}
}
