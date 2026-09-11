import java.io.*;
import java.util.*;

public class Main{
	public static void main(String args[]){
		try{
			new Main();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Main() throws IOException{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		List<Integer> Ans = new ArrayList<Integer>();
		String line;
		
		int[] prime = Eratos();
		
		while((line = in.readLine()) != null){
			int size = Integer.parseInt(line);
			if(size==0) break;
			
			int pay = 0;
			for(int n=0; n<size; n++){
				line = in.readLine();
				String[] dst = line.split(" ");
				int center = Integer.parseInt(dst[0]);
				int range = Integer.parseInt(dst[1]);
				
				
				int min = Math.max(2, center-range);
				int max = Math.min(center+range, 999983);
				
				if(prime[min] != prime[min-1]){
					pay += prime[max] - prime[min];
				}
				else{
					pay += prime[max] - prime[min] - 1;
				}
			}
			
			Ans.add(Math.max(0, pay));
		}
		
		for(int n=0; n<Ans.size(); n++){
			System.out.println(Ans.get(n));
		}		
	}
	
	public int[] Eratos(){
		int[] prime = new int[1000000];
		int c = 0;
		for(int i=2; i<1000000; i++){
			if(prime[i]==0){
				c++;
				prime[i] = c;
				long big = i*i;
				if(big > 1000000){
					for(int j=i; j<1000000; j++){
						if(prime[j]==0){
							c++;
							prime[j] = c;
						}
						else{
							prime[j] = c;
						}
					}
					break;
				}
				for(int j=i*i; j<1000000; j+=i){
					prime[j] = -1;
				}
			}
			if(prime[i]==-1){
				prime[i] = c;
			}
		}
		
		return prime;
	}
}
