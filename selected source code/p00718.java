import java.util.Scanner;

//Numeral System
public class Main{

	static int f(char[] s){
		int r = 0;
		int x = 1;
		for(int i=0;i<s.length;i++){
			if(Character.isDigit(s[i]))x = s[i]-'0';
			else {
				switch(s[i]){
				case 'm': r+=x*1000;break;
				case 'c': r+=x*100;break;
				case 'x': r+=x*10;break;
				case 'i': r+=x;
				}
				x = 1;
			}
		}
		return r;
	}

	static String g(int r){
		String s = "";
		if(r/1000>0){
			if(r/1000>1)s+=r/1000;
			s+='m';
		}
		r%=1000;
		if(r/100>0){
			if(r/100>1)s+=r/100;
			s+='c';
		}
		r%=100;
		if(r/10>0){
			if(r/10>1)s+=r/10;
			s+='x';
		}
		r%=10;
		if(r>=2)s+=r+"i";
		else if(r==1)s+='i';
		return s;
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		while(t--!=0){
			System.out.println(g(f(sc.next().toCharArray()) + f(sc.next().toCharArray())));
		}
	}
}
