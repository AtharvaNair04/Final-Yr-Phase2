import java.util.Scanner;

class Main {
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		String str;
		
		int[] blood = new int[4];
		
		while(scan.hasNext()){
			str = scan.next();
			String[] num = str.split(",", 0);
			if(num[1].equals("A"))blood[0]++;
			if(num[1].equals("B"))blood[1]++;
			if(num[1].equals("O"))blood[3]++;
			if(num[1].equals("AB"))blood[2]++;
		}
		for(int i = 0; i < 4; i++){
			System.out.println(blood[i]);
		}
	}
}
