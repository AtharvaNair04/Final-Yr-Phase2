import java.util.*;

class Main{

    char[] in;
    int p;

    void solve(){
	Scanner sc = new Scanner(System.in);

	while(sc.hasNext()){
	    String s = sc.next();

	    in = s.toCharArray();

	    int ans = -1;
	    for(int num = 0; num <= 9; num++){
		for(int i=0; i<in.length; i++){
		    if(s.charAt(i)=='X'){
			in[i] = (char)(num + '0');
		    }
		}
		p = 0;
		String a = digit();
		p++;
		String b = digit();
		p++;
		String c = digit();
		if(a.length()!=1 && a.charAt(0)=='0') continue;
		if(b.length()!=1 && b.charAt(0)=='0') continue;
		if(a.length()!=1 && c.charAt(0)=='0') continue;

		String left = sum(a, b);
		if(left.equals(c)){
		    ans = num;
		    break;
		}
	    }

	    if(ans==-1) System.out.println("NA");
	    else System.out.println(ans);
	}
    }

    String sum(String a, String b){
	int n1 = a.length();
	int n2 = b.length();
	if(n1<n2){
	    String tmp = a;
	    a = b;
	    b = tmp;
	}

	String[] line = new String[Math.max(a.length(), b.length())+1];
	for(int i=0; i<a.length(); i++) line[i] = String.valueOf(a.charAt(a.length()-i-1));
	for(int i=0; i<b.length(); i++){
	    int num = Integer.valueOf(line[i]);
	    int num2 = b.charAt(b.length()-i-1) - '0';
	    num += num2;
	    if(num<10) line[i] = String.valueOf(num);
	    else{
		line[i] = String.valueOf(num % 10);
		int num3 = 0;
		if(line[i+1]!=null) num3 = Integer.parseInt(line[i+1]);
		line[i+1] = String.valueOf(num3 + num/10);
	    }
	}

	String res = "";
	for(int i=line.length-1; i>=0; i--){
	    if(line[i]==null) continue;
	    res += line[i];
	}
	return res;
    }

    String digit(){
	String res = "";
	while(p<in.length && Character.isDigit(in[p])) res += in[p++];
	return res;
    }

    public static void main(String[] args){
	new Main().solve();
    }
}
