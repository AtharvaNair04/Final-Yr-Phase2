import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		double ans=0;
		Scanner sc=new Scanner(System.in);
		double x1=Double.parseDouble(sc.next());
		double y1=Double.parseDouble(sc.next());
		double x2=Double.parseDouble(sc.next());
		double y2=Double.parseDouble(sc.next());
		ans= Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
		String strAns=String.valueOf(ans);
		BigDecimal bd=new BigDecimal(strAns);
		BigDecimal A=bd.setScale(8, BigDecimal.ROUND_HALF_UP);
		//System.out.println(A);
		System.out.println(ans);
	}

}
