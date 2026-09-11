import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		for(;;){
			String n = cin.next();
			String m = cin.next();
			if((n+m).equals("00"))break;
			char[] s1 = n.toCharArray();
			char[] s2 = m.toCharArray();
			int hit=0,blow=0;
			for(int i = 0;i<s1.length;i++){
				if(s1[i]==s2[i]){
					hit++;
					s1[i]=(char)i;
					s2[i]=(char)(i+4);
				}
			}
			for(int i = 0;i<s1.length;i++){
				for(int j = 0;j<s2.length;j++){
					if(s1[i]==s2[j]){
						blow++;
						s1[i]=(char)(i*1000);
						s2[j]=(char)(j*10000);
					}
				}
			}
			System.out.println(hit+" "+blow);
		}
	}

}
