import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int cnt = 0;
		String[] s = new String[16];
		for(int a=0;a<2;a++){
			for(int b=0;b<2;b++){
				for(int c=0;c<2;c++){
					s[cnt] = a + "" + b + "" + c + "0";
					s[cnt+1] = a + "" + b + "" + c + "1";
					cnt+=2;
				}
			}
		}
		
		double[] u = new double[31];
		double v = 0.0078125;
		for(int i=0;i<31;i++){
			u[i] = v;
			v *= 2;
		}
		
		int n = sc.nextInt();
		for(int i=0;i<n;i++){
			String t = sc.next();
			StringBuilder sb = new StringBuilder();
			int id = -1;
			for(int j=0;j<8;j++){
				char c = t.charAt(j);
				if(Character.isDigit(c)) id = c-48;
				else id = c-87;
				sb.append(s[id]);
			}
			
			if(sb.charAt(0)=='1') System.out.print("-");
			int d = 0;
			for(int j=7;j<31;j++){
				id = sb.charAt(31-j)-48;
				d += id*u[j];
			}
			System.out.print(d + ".");
			double dd = 0;
			for(int j=0;j<7;j++){
				id = sb.charAt(31-j)-48;
				dd += id*u[j];
			}
			String ddd = Double.toString(dd);
			System.out.println(ddd.substring(2,ddd.length()));
		}
	}
}
