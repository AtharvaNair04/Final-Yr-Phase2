import java.util.Scanner;

public class Main {
	public static void main(String args[]){
		run();
	}
	static void run(){
		Scanner scan = new Scanner(System.in);
		while(true){
			int x, y;
			int max;
			y = scan.nextInt();
			x = scan.nextInt();
			if(x == 0 && y == 0) break;
			int[][] osenbeiField = new int[y][x];
			for(int i = 0; i < y; i++){
				for(int j = 0; j < x; j++){
					osenbeiField[i][j] = scan.nextInt();
				}
			}
			max = enumeration(osenbeiField, y, x);
			System.out.println(max);
		}
	}
	
	static int enumeration(int[][] osenbeiField, int y, int x){
		int max;
		int sum;
		int tmp;
		int[][] osenbeiFieldTmp = new int[y][x];
		for(int i = 0; i < y; i++){
			for(int j = 0; j < x; j++){
				osenbeiFieldTmp[i][j] = osenbeiField[i][j];
			}
		}
		max = 0;
		tmp = 0;
		
		for(int i = 0; i < Math.pow(2, y); i++){
			sum = 0;
			String[] turn = new String[10];
			for(int j = 0; j < 10; j++){
				turn[j] = "0";
			}
			
			turn = toBinary(i).split("");
			for(int j = 0; j < turn.length; j++){
				if(turn[j].equals("1")){
					horizontalLine(osenbeiField, j, x);
				}
			}

			for(int j = 0; j < x; j++){
				if(sumVerLine(osenbeiField, y, j) < sumVerLine(verticalLine(osenbeiField, y, j), y, j)){
					verticalLine(osenbeiField, y, j);
				}
			}
			
			for(int j = 0; j < y; j++){
				for(int k = 0; k < x; k++){
					sum += osenbeiField[j][k];
				}
			}
			
			tmp = x * y - sum;
			max = Math.max(tmp, max);
			for(int j = 0; j < y; j++){
				for(int k = 0; k < x; k++){
					osenbeiField[j][k] = osenbeiFieldTmp[j][k];
				}
			}
			
		}
		return max;
	}
	
	//??????
	static int turnOver(int osenbei){
		if(osenbei == 0){
			return 1;
		}
		return 0;
	}
	
	//????????????
	static int[][] verticalLine(int[][] osenbeiField, int a, int b){
		for(int i = 0; i < a; i++){
			osenbeiField[i][b] = turnOver(osenbeiField[i][b]);
		}
		return osenbeiField;
	}
	
	//?¨??????????
	static int[][] horizontalLine(int[][] osenbeiField, int a, int b){
		for(int i = 0; i < b; i++){
			osenbeiField[a][i] = turnOver(osenbeiField[a][i]);
		}
		return osenbeiField;
	}
	
	//?¨????????¨?
	static int sumVerLine(int[][] osenbeiField, int a, int b){
		int sum;
		sum = 0;
		for(int i = 0; i < a; i++){
			sum += osenbeiField[i][b];
		}
		return sum;
	}
	
	//??????????¨?
	static int sumHorLine(int[][] osenbeiField, int a, int b){
		int sum;
		sum = 0;
		for(int i = 0; i < b; i++){
			sum += osenbeiField[a][i];
		}
		return sum;
	}
	
	//????????°??????
	static String toBinary(int a){
		String binary;
		binary = Integer.toBinaryString(a);
		return binary;
	}
}
