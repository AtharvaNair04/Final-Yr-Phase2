import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//Lattice Practices
public class Main{

	String[] s;
	String[] rev;
	char[][] m;
	boolean[] u;
	Set<String> set;
	int c = 0;

	char[][] rotate(char[][] t){
		char[][] r = new char[5][5];
		for(int i=0;i<5;i++)for(int j=0;j<5;j++)r[i][j] = t[4-j][i];
		return r;
	}

	char[][] mirror(char[][] t){
		char[][] r = new char[5][5];
		for(int i=0;i<5;i++)for(int j=0;j<5;j++)r[i][j] = t[4-i][j];
		return r;
	}

	String get(char[][] t){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<5;i++)for(int j=0;j<5;j++)sb.append(t[i][j]);
		return sb.toString();
	}

	boolean match(String a, String b){
		for(int i=0;i<5;i++)if(a.charAt(i)==b.charAt(i))return false;
		return true;
	}

	int x = 0;

	void f(int k){
		if(k==5){
			String[] v = new String[5];
			for(int j=0;j<5;j++){
				char[] t = new char[5];
				for(int i=0;i<5;i++)t[i]=m[i][j];
				v[j] = new String(t);
			}
			boolean[] w = new boolean[10];
			char[][] f = new char[5][5];
			for(int i=0;i<5;i++){
				boolean match = false;
				for(int j=0;j<10;j++){
					if(u[j]||w[j])continue;
					if(match(v[i], s[j])){
						w[j] = true;
						match = true;
						f[i] = s[j].toCharArray();
						break;
					}
					else if(match(v[i], rev[j])){
						w[j] = true;
						match = true;
						f[i] = rev[j].toCharArray();
						break;
					}
				}
				if(!match)return;
			}
			String ans = get(m);
			if(!set.contains(ans)){
				c++;
				set.add(ans);
				set.add(get(mirror(m)));
				set.add(get(rotate(rotate(m))));
				set.add(get(mirror(rotate(rotate(m)))));
				set.add(get(f));
				set.add(get(mirror(f)));
				set.add(get(rotate(rotate(f))));
				set.add(get(mirror(rotate(rotate(f)))));
			}
			return;
		}
		for(int i=0;i<10;i++){
			if(!u[i]){
				u[i] = true;
				m[k] = s[i].toCharArray();
				f(k+1);
				if(!s[i].equals(rev[i])){
					m[k] = rev[i].toCharArray();
					f(k+1);
				}
				u[i] = false;
			}
		}
	}

	void run(){
		Scanner sc = new Scanner(System.in);
		while(true){
			s = sc.nextLine().split(" ");
			if(s.length==1)break;
			rev = new String[10];
			for(int i=0;i<10;i++){
				StringBuilder sb = new StringBuilder(s[i]);
				rev[i] = sb.reverse().toString();
			}
			u = new boolean[10];
			m = new char[5][5];
			c = 0;
			set = new HashSet<String>();
			f(0);
			System.out.println(c);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
