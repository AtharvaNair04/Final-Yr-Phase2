import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		while(s.hasNext()){
			String a = s.next();
			int sum = 0;
			int n[]=new int[a.length()];
			for(int i = 0; i < a.length(); i++){

				switch (a.charAt(i)){
				case 'I': n[i] = 1; break;
				case 'V': n[i] = 5; break;
				case 'X': n[i] = 10; break;
				case 'L': n[i] = 50; break;
				case 'C': n[i] = 100; break;
				case 'D': n[i] = 500; break;
				case 'M': n[i] = 1000; break;

				}
			}
			for(int i = 0; i < a.length();i++){
				if(i < a.length() - 1 && n[i] < n[i+1])sum -= n[i];
				else sum += n[i];
			}
			System.out.println(sum);
		}
	}
}
