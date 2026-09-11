import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			int L = sc.nextInt();
			
			if(L == 0){
				break;
			}
			
			int over = -1;
			for(int i = 0; i < 12; i++){
				L -= (sc.nextInt() - sc.nextInt());
				if(over == -1 && L <= 0){
					over = i+1;
				}
			}
			
			System.out.println(over > 0 ? over+"" : "NA");
			
		}
		
	}

}
