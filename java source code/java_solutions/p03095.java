import static java.lang.System.*;
import java.util.*;

public class Main {
	public static final int MOD = 1000000007;

    public static void main(String[] args) {
        Scanner sc = new Scanner(in);

        int n = sc.nextInt();
        char[] str = sc.next().toCharArray();
		
		int[] count = new int[26];
		Arrays.fill(count, 0);
		for(char c: str) count[c-'a']++;
		
		long ans = 1;
		for(int c: count)
			if(c!=0) {
				ans *= (c+1);
				ans %= MOD;
			}
		
        out.println(ans-1);
    }
}
