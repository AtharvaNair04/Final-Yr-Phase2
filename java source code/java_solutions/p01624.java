import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	//int INF = 1 << 28;
	long INF = 1L << 62;
	double EPS = 1e-10;

	void run() {
		Scanner sc = new Scanner(System.in);
		for(;;) {
			int n = sc.nextInt(); String str = sc.next() + "=";
			if(n == 0) break;
			
			depth = n%2==0? 2 : 1;
			System.out.println(minmax(0, str));
		}
	}
	
	int depth;
	long minmax(int d, String l) {
		if(d == depth) {
			line = l; p = 0;
			return d%2==0? or(): -or();
		}
		long max = -INF;
		for(String next: nextStr(l)) {
			max = max(max, -minmax(d+1, next));
		}
		return max;
	}
	char[] ops = {'|', '^', '&', '+', '-', '*'};
	String[] nextStr(String l) {
		HashSet<String> ret = new HashSet<String>();
		int n = l.length() - 1;
		
		for(int i=0;i<n;i++) {
			if( '0' <= l.charAt(i) && l.charAt(i) <= '9' ) {
				if(i>0 && '0' <= l.charAt(i-1) && l.charAt(i-1) <= '9') ret.add(l.substring(0, i) + l.substring(i+1)); 
				for(int j=0;j<10;j++) {
					if(j != 0) ret.add(l.substring(0, i) + j + l.substring(i));
					ret.add(l.substring(0, i+1) + j + l.substring(i+1));
				}
				if( '1' <= l.charAt(i+1) && l.charAt(i+1) <= '9' ) {
					for(char op: ops) ret.add(l.substring(0, i+1) + op + l.substring(i+1));
					ret.add(l.substring(0, i) + l.substring(i+1));
				}
			}
			else if( l.charAt(i) != '(' && l.charAt(i) != ')' ) {
				if('0' <= l.charAt(i-1) && l.charAt(i-1) <= '9' && '0' <= l.charAt(i+1) && l.charAt(i+1) <= '9')
					ret.add(l.substring(0, i) + l.substring(i+1));
			}
		}
		
		return ret.toArray(new String[]{});
	}
	
	String line; int p;
	char next() {
		return line.charAt(p++);
	}
	
	long or() {
		long a = xor();
		char c = next();
		while( c == '|' ) {
			a |= xor();
			c = next();
		}
		p--;
		return a;
	}
	
	long xor() {
		long a = and();
		char c = next();
		while( c == '^' ) {
			a ^= and();
			c = next();
		}
		p--;
		return a;
	}
	
	long and() {
		long a = add();
		char c = next();
		while( c == '&' ) {
			a &= add();
			c = next();
		}
		p--;
		return a;
	}
	
	long add() {
		long a = mlt();
		char c = next();
		while( c == '+' || c == '-' ) {
			if(c == '+') a += mlt();
			else 		 a -= mlt();	
			c = next();
		}
		p--;
		return a;
	}
	
	long mlt() {
		long a = num();
		char c = next();
		while( c == '*' ) {
			a *= num();
			c = next();
		}
		p--;
		return a;
	}
	
	long num() {
		char c = next();
		if( c == '(' ) { long a = or(); p++; return a; }
		String val = "";
		while( '0' <= c && c <= '9') {val += c; c = next();}
		p--;
		return Long.valueOf(val);
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
