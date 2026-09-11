import java.util.*;
import java.math.RoundingMode;
import java.math.BigDecimal;
 
 
public class Main{
	public static void main (String[] args){
		Scanner sc = new Scanner(System.in);
		long n = sc.nextLong();
		long x = sc.nextInt();
		long m = sc.nextInt();
		List<Long> list = new ArrayList<Long>(); 
		Set<Long> set = new HashSet<Long>();
		boolean exist = false;
		long now = x;
		int loopst = 0;
		int loopfin = 0;
		long last = 0;
		list.add(now);
		set.add(now);
		for (long i = 0; i < n; i++) {
            now = now * now % m;
            if (set.contains(now)) {
                last = now;
                break;
            } else {
                set.add(now);
                list.add(now);
            }
        }
		for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == last) {
                loopst = i;
            }
        }
		//ループが始まる前の和
		long sumbef = 0;
		for(int i = 0; i < loopst; i++) {
			sumbef+=list.get(i);
		}
		//ループ内のA_nの和
		long sumloop = 0;
		for(int i = loopst; i < list.size(); i++) {
			sumloop+=list.get(i);
		}
		long loopnum = list.size() - loopst;
		long block = (n-loopst)/loopnum;
		sumloop*=block;
		//ループのあまり分の和
		long summod = 0;
		long loopmod = (n-loopst)%(list.size()-loopst);
		for(int i = 0; i < loopmod; i++) {
			summod+=list.get(loopst+i);
		}
		long ans = sumbef + sumloop+ summod;
		System.out.println(ans);
	}
}
