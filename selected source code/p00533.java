import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);	
		int h = sc.nextInt();
		int w = sc.nextInt();
		//String data[][] = new String[h][w];
		//String cc[] = new String[w];
		char [][] cdata = new char[h][w];
		int [][] resultdata = new int[h][w];
		int cnt=0,ccnt=0;
		for(int k = 0;k<h;k++){
			cdata[k] = sc.next().toCharArray();	
		//	System.out.println(cdata[k]);
		}
		for(int i = 0;i < h;i++){
			
			for(int j = 0;j < w;j++){	
				if(cdata[i][j]==('.')){
					if(ccnt != 1){
						resultdata[i][j] = -1;
					}else{
						cnt++;
						resultdata[i][j] = cnt;
					}
				}
				else if(cdata[i][j]==('c')){
					 cnt = 0;
	    			 ccnt = 1;
	    			 resultdata[i][j] = 0;
				}
			}
			ccnt = 0;
			 
		}
		for(int i = 0;i < h;i++){
			for(int j = 0;j < w;j++){
				if((j+1)==w){
					System.out.println(resultdata[i][j]);
				}else{
					System.out.print(resultdata[i][j]+" ");
				}
			}
		}
			
	}
}
