import java.util.Scanner;

class Main {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		int n;
		String str;
		boolean[] ch = new boolean[4];
		int count, out;
		
		n = scan.nextInt();
		
		for(int i = 0; i < n; i++){
			count = 0;
			out = 0;
			for(int j = 0; j < 4; j++){
			ch[j] = false;	
			}
			
			while(out != 3){
				str = scan.next();
				if(str.equals("HIT")){
					for(int j = 0; j < 4; j++){
						if(ch[j] == false){
							ch[j] = true;
							if(j == 3){
								count++;
								ch[j] = false;
							}
							break;
						}
					}
				}else if(str.equals("HOMERUN")){
					for(int j = 0; j < 4; j++){
						if(ch[j] == true){
							count++;
							ch[j] = false;
						}
					}
					count++;
				}else if(str.equals("OUT")){
					out++;
				}
			}
			System.out.println(count);
		}
	}
}
