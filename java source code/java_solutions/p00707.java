import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			int w = sc.nextInt();
			int h = sc.nextInt();
			sc.nextLine();
			if(w == 0 && h == 0)break;
			else calc(w, h);
			
		}
	}
	public void calc(int w, int h){
		String[][] matrix = new String[h][w];
		for(int i = 0; i < h; i++){
			String line = sc.nextLine();
			for(int j = 0; j < w; j++){
				String c = line.substring(j, j+1);
				matrix[i][j] = c;
			}
		}
		num = new String[h][w];
		String max = "0";
		for(int i = 0; i < w; i++){
			for(int j = 0; j < h; j++){
				if(num[j][i] == null) numCalc(matrix, i, j, w, h);
				if(num[j][i] != "-1") max = compare(retNum(num[j][i]) , max);
			}
		}
		System.out.println(max);
	}
	String[][] num;
	public String retNum(String t){
		int size = t.length();
		for(int i = 0; i < size - 1; i++){
			if(t.charAt(0) == '0') t = t.substring(1);
			else break;
		}
		return t;
	}
	
	public String compare(String i, String j){
		if(i.length() > j.length()){
			return i;
		}
		else if(i.length() < j.length()){
			return j;
		}
		else if(i.compareTo(j) < 0) return j;
		else return i;
	}
	public String numCalc(String[][] matrix, int x, int y, int w, int h){
		if(num[y][x] != null) return num[y][x];
		else{
			String c = matrix[y][x];
			if(c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || c.equals("4")
					|| c.equals("5") || c.equals("6") || c.equals("7") || c.equals("8") || c.equals("9")){
				String a1 = null;
				if(x < w-1 && num[y][x+1] == null) {
					String t = numCalc(matrix, x+1, y, w, h);
					if(!t.equals("-1")) a1 = c + t;
				}
				else if(x < w-1 && !(num[y][x+1].equals("-1"))) a1 = c + num[y][x+1];
				String a2 = null;
				if(y < h-1 && num[y+1][x] == null) {
					String t = numCalc(matrix, x, y+1, w, h);
					if(!t.equals("-1")) a2 = c + t;
				}
				else if(y < h-1 && !(num[y+1][x].equals("-1"))) a2 = c + num[y+1][x];
				
				if(a1 != null && a2 != null){
					num[y][x] = compare(a1, a2);
				}
				else if(a1 == null && a2 == null) num[y][x] = c;
				else if(a1 == null) num[y][x] = a2;
				else num[y][x] = a1;
			}
			else num[y][x] = "-1";
			return num[y][x];
		}
	}

	
	public static void main(String[] args) {
		new Main().run();
	}
}
