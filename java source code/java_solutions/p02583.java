import java.io.*;
import java.util.*;

public class Main{
	
	public static void main(String[] args){
	
	MyScanner sc = new MyScanner();
	out = new PrintWriter(new BufferedOutputStream(System.out));
	
	/**/
	
	int numOfSticks = sc.nextInt();
	int[] sticks = new int[numOfSticks];
	
	int counter = 0;
	
	for(int i = 0; i < numOfSticks; i++){
		sticks[i] = sc.nextInt();
	}
	
	if(numOfSticks < 3){
		System.out.println(0);
		return;
	}
	
	
	
	for(int i = 0; i < numOfSticks; i++){
		
		for(int i2 = i + 1; i2 < numOfSticks; i2++){
			
			for(int i3 = i2 +1; i3 < numOfSticks; i3++){
				
				if(i != i2 && i2 != i3 && i != i3){
					
					if(sticks[i] != sticks[i2] && sticks[i2] != sticks[i3] && sticks[i] != sticks[i3]){
						
						if(sticks[i] + sticks[i2] > sticks[i3] && Math.max(sticks[i], sticks[i2]) - Math.min(sticks[i],sticks[i2]) < sticks[i3]){
						counter++;
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	System.out.println(counter);

	/**/
	 
	 out.close();	
	}
	
	public static PrintWriter out;
	
	public static class MyScanner{
		
		BufferedReader br;
		StringTokenizer st;
		
		public MyScanner(){
			br = new BufferedReader(new InputStreamReader(System.in));
		}
		
		String next() {
          while (st == null || !st.hasMoreElements()) {
              try {
                  st = new StringTokenizer(br.readLine());
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return st.nextToken();
      }
 
      int nextInt() {
          return Integer.parseInt(next());
      }
 
      long nextLong() {
          return Long.parseLong(next());
      }
 
      double nextDouble() {
          return Double.parseDouble(next());
      }
 
      String nextLine(){
          String str = "";
	  try {
	     str = br.readLine();
	  } catch (IOException e) {
	     e.printStackTrace();
	  }
	  return str;
      }

   }
	
}
