import java.util.Scanner;
public class Main {
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			while(sc.hasNext()) {
				int m = sc.nextInt();
				int n = sc.nextInt();
				if(m + n == 0) break;
				
				boolean[] jud = new boolean[7500000];
				int pos = m;
				
				for(int i=0; i<n; i++) {
					while(jud[pos]) pos++;
					for(int j=pos; j<jud.length; j+=pos) jud[j] = true;
				}
				while(jud[pos]) pos++;
				
				System.out.println(pos);
			}
		}
	}
}
