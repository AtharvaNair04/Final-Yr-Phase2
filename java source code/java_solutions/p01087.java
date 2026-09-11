import java.util.Scanner;

public class Main {
	
	static int n;
	static char[][] buff=new char[17][10];
	static int[] length=new int[17];
	
	static int calc(int line) {
		char c;
		int level, val;
		int arg;
		level=length[line]-1;
		c=buff[line][level];
		if(c=='*') {
			line++;
			val=1;
			while(true) {
				if(length[line]-1<=level) break;
				if(length[line]-1==level+1) {
					arg=calc(line);
					val*=arg;
				}
				line++;
				if(line>=n) break;
			}
			return val;
		}
		else if(c=='+') {
			line++;
			val=0;
			while(true) {
				if(length[line]-1<=level) break;
				if(length[line]-1==level+1) {
					arg=calc(line);
					val+=arg;
				}
				line++;
				if(line>=n) break;
			}
			return val;
		}
		return c-'0';
	}
	
	public static void main(String args[]){
		try(Scanner sc=new Scanner(System.in)){
			while(sc.hasNext()) {
				n=sc.nextInt();
				if(n==0) break;
				for(int i=0; i<n; i++) {
					String str=sc.next();
					length[i]=str.length();
					for(int j=0; j<str.length(); j++) {
						buff[i][j]=str.charAt(j);
					}
				}
				System.out.println(calc(0));
			}
			
		}
	}
}
