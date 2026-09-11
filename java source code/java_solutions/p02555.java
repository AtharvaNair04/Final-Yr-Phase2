import java.util.*;
import java.io.*;
import java.math.*;
public class Main {
    public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		PrintWriter ou = new PrintWriter(System.out);
		int s = Integer.parseInt(sc.next());
		sc.close();
		int mod = (int)Math.pow(10 , 9) + 7;
		int[] q = new int[s + 1];
		Arrays.fill(q , 0);
		q[0] = 1;
		for(int i = 3 ; i <= s ; i++) q[i] = (q[i - 1] + q[i - 3]) % mod;
		ou.println(q[s]);
		ou.flush();
	}
}
