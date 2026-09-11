import java.util.Scanner;
public class Main{
	public static void main(String[]args){
		Scanner sc=new Scanner(System.in);
		int i;
		char ch,c;
		int[] chars = new int[256];
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			for(i=0;i<line.length();i++){
				ch = Character.toLowerCase(line.charAt(i));
				chars[ch]++;
			}
		}
		for(ch='a';ch<='z';ch++){
			System.out.println(ch+" : "+chars[ch]);
		}
	}
}
