import java.util.Scanner;
public class Main {
	public static void main(String[] args){
		Scanner don = new Scanner(System.in);
		String[] an,bn;
		String as,bs;
		as = don.next();
		boolean ak = true;
		for(int i=0;i<as.length();i++){
			if(String.valueOf(as.charAt(as.length()-i-1)).equals("d")&&String.valueOf(as.charAt(i)).equals("b")){
			}else if(String.valueOf(as.charAt(as.length()-i-1)).equals("b")&&String.valueOf(as.charAt(i)).equals("d")){
			}else if(String.valueOf(as.charAt(as.length()-i-1)).equals("p")&&String.valueOf(as.charAt(i)).equals("q")){
			}else if(String.valueOf(as.charAt(as.length()-i-1)).equals("q")&&String.valueOf(as.charAt(i)).equals("p")){
			}else{
				ak = false;
			}
		}
		if(ak){
			System.out.println("Yes");
		}else{
			System.out.println("No");
		}
	}
}
