import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner sc = new Scanner(System.in);
		while(true) {
			char[][] area = new char[9][9];
			String s = sc.next();
			if(s.equals("#")) {break;}
			int i=0;
			try {
			for(int h=0;h<9;h++) {
				for(int w=0;w<10;w++) {
					if(s.charAt(i) == '/') {
						i++;
						break;
					}else if(s.charAt(i) == 'b') {
						i++;
						area[h][w] = 'b';
					}else {
						int blank = Character.getNumericValue(s.charAt(i));
						i++;
						for(int a=0;a<blank;a++) {
							area[h][w] = 'a';
							w++;
						}
						w--;
					}
				}
			}
			}
			catch(IndexOutOfBoundsException e) {
				
			}
			area[sc.nextInt()-1][sc.nextInt()-1] = 'a';
			area[sc.nextInt()-1][sc.nextInt()-1] = 'b';
			
			s = "";
			int p=0;
			
			try {
				for(int h=0;h<9;h++) {
					p=0;
					for(int w=0;w<9;w++) {
						if(area[h][w] == 'b') {
							if(p>0) {
								s += p;
							}
							s += "b";
							p=0;
						}else if(area[h][w] == 'a'){
							p++;
						}
					}
					if(p>0) {
						s += p;
					}
					
					s+="/";
				}
			}catch(IndexOutOfBoundsException e) {
				
			}

			s=s.replace("//", "/");
			s=s.replace("//", "/");
			s=s.replace("//", "/");
			s=s.replace("//", "/");
			s=s.replace("//", "/");
			
			System.out.println(s.substring(0, s.length()-1));
			
		}
	sc.close();
	}

}
