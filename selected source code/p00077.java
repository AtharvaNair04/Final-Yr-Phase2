import java.io.PrintWriter;
import java.util.Scanner;
class Main{
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		while(sc.hasNext()){
			String str = sc.nextLine();
			for(int i = 0; i < str.length();i++){
				//System.out.println(str.charAt(i));
				if(String.valueOf(str.charAt(i)).equals("@")){
					String temp = String.valueOf(str.charAt(i+1));
					int n = Integer.parseInt(temp);
					for(int j = 0; j < n; j++){
						pw.write(str.charAt(i+2));
					}
					i+=2;
				}else{	
					pw.write(str.charAt(i));
				}
			}
			pw.println();
			pw.flush();
		}
	}	
}
