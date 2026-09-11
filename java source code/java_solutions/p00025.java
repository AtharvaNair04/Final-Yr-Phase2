import java.util.*;	
public class Main {
	public static void main(String[] args) {
					Scanner in =new Scanner(System.in);
					while(in.hasNext()){
						int[] a=new int[4];
						int[] b=new int[4];
						for(int i=0;i<4;i++)a[i]=in.nextInt();				
						for(int i=0;i<4;i++)b[i]=in.nextInt();
						int hits=0,blows=0;
						for(int i=0;i<4;i++){
							if(a[i]==b[i])
								hits++;
						}
						for(int i=0;i<4;i++){
							for(int j=0;j<4;j++)
							{
								if(b[j]==a[i]){
									blows++;
								}
							}
						}
						blows=blows-hits;
						System.out.println(hits+" "+blows);
					}
					in.close();
			}
	}
