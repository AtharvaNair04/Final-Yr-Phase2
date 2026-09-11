import java.util.*;
public class Main {



	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		int k=0;
		if(s.equals("RRR")) {
			k=3;
		}else if(s.equals("RRS")||s.equals("SRR"))  {
			k=2;
		}else if(s.equals("SSS"))  {
			k=0;
		}else {
			k=1;
		}
		System.out.println(k);

	}

}
