/**
 * icpc??´?????????
* 2006??´ ?????? B??????
**/
import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;

class Main{
	static String a;
	static String b;
	public static void main(String[] args) {
		try {
			run();
		}catch(IOException e) {
			System.out.println(e);
		}
	}
	
	static void run() throws IOException{
		int m;
		String str;
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			m = Integer.parseInt(in.readLine()); //???????????£?????????
			
			for(int k=0;k<m;k++) {
				str = in.readLine();
				a = "";
				set.clear();
				
				for(int i=0;i<str.length()-1;i++) {
					a = a + str.charAt(i);
					b = "";
					for(int j=i+1;j<str.length();j++) {
						b = b + str.charAt(j);
						
					}
					
					//a??????
					set.add(a+b);
					set.add(re(a)+b);
					set.add(re(a)+re(b));
					set.add(a+re(b));
					
					//b??????
					set.add(b+a);
					set.add(re(b)+a);
					set.add(re(b)+re(a));
					set.add(b+re(a));
				}
				
				System.out.println(set.size());
			}
			break;
		}
	}
	
	static String re(String str) {
		StringBuffer sb = new StringBuffer(str);
		return sb.reverse().toString();
	}
}
