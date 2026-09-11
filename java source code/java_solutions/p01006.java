import java.util.Scanner;

public class Main {

	public static boolean isWrite(String s) {
	
		for(int i=0; i<s.length()-1; i++) {
			char c = s.charAt(i);
			if(c=='A') {
				if(s.charAt(i+1) == 'B' || s.charAt(i+1) == 'D'){}
				else return false;
			}
			else if(c=='B') {
				if(s.charAt(i+1) == 'A' || s.charAt(i+1) == 'C' || s.charAt(i+1) == 'E' ){}
				else return false;
			}
			else if(c=='C') {
				if(s.charAt(i+1) == 'B' || s.charAt(i+1) == 'F' ){}
				else return false;
			}
			else if(c=='D') {
				if(s.charAt(i+1) == 'A' || s.charAt(i+1) == 'E' || s.charAt(i+1) == 'G' ){}
				else return false;
			}
			else if(c=='E') {
				if(s.charAt(i+1) == 'B' || s.charAt(i+1) == 'D' || s.charAt(i+1) == 'F' || s.charAt(i+1) == 'H' ) {}
				else return false;
			}
			else if(c=='F') {
				if(s.charAt(i+1) == 'C' || s.charAt(i+1) == 'E' || s.charAt(i+1) == 'I' ){}
				else return false;
			}
			else if(c=='G') {
				if(s.charAt(i+1) == 'D' || s.charAt(i+1) == 'H' ){}
				else return false;
			}
			else if(c=='H') {
				if(s.charAt(i+1) == 'E' || s.charAt(i+1) == 'G' || s.charAt(i+1) == 'I' ){}
				else return false;
			}
			else if(c=='I') {
				if(s.charAt(i+1) == 'F' || s.charAt(i+1) == 'H' ){}
				else return false;
			}	
		
		}

		return true;
	
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String[] pass = new String[1000];
		
		for(int i=0; i<1000; i++) { 
			pass[i] = sc.next();
		}			
		for(int i=0; i<1000; i++) {
			if(isWrite(pass[i])) System.out.println(pass[i]);
		}
	
	}
}
