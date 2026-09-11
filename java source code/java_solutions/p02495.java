import java.util.Scanner;
public class Main{
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		int s=0,i=0,x=0,h=1,w=1;
		while(h != 0 && w != 0){
			h = sc.nextInt();
			w = sc.nextInt();
			if(w==0&&h==0){
				break;
			}
			for(x=0;x<h;x++){
			if(s==0){
				while(true){
					System.out.printf("#");
					i++;
					if(i >= w){
						break;
					}
					System.out.printf(".");
					i++;
					if(i >= w){
						break;
					}
				}
				System.out.printf("\n");
				i = 0;
				s++;
			}
			else if(s==1){
				while(true){
					System.out.printf(".");
					i++;
					if(i >= w){
						break;
					}
					System.out.printf("#");
					i++;
					if(i >= w){
						break;
					}
				}
				System.out.printf("\n");
				i = 0;
				s--;
			}
			}
			System.out.printf("\n");
			s=i=x=0;
			
		}
	}
}
