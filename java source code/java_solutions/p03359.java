import java.util.*;

class Main{
	static Scanner s=new Scanner(System.in);
	static int gInt(){
		return Integer.parseInt(s.next());
	}
	public static void main(String[]A){
		int a=gInt(),b=gInt();
		System.out.println(a<=b?a:a-1);
	}
}
