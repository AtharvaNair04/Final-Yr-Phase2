import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.TreeMap;
 
 
public class Main{
	public static class File{
		int identifer;
		long start;
		long size;
		
		public File(int identifer, long start, long size) {
			super();
			this.identifer = identifer;
			this.start = start;
			this.size = size;
		}
		
		public String toString(){
			return this.identifer + " " + this.start + "->" + (this.start + this.size - 1);
		}
	}
	
    public static void main(String[] args) throws IOException {
    	Scanner sc = new Scanner(System.in);
    	
    	while(true){
    		final int N = sc.nextInt();
    		
    		if(N == 0){
    			break;
    		}
    		
    		LinkedList<File> files = new LinkedList<File>();
    		
    		for(int i = 0; i < N; i++){
    			String ops = sc.next();
    			
    			//System.out.println(files);
    			
    			if("W".equals(ops)){
    				final int l = sc.nextInt();
    				final int S = sc.nextInt();
    				
    				long rest = S;
    				long pos = 0;
    				for(ListIterator<File> iter = files.listIterator(); iter.hasNext(); ){
    					File file = iter.next();
    					
    					if(pos < file.start){
    						iter.previous();
    						final long size = Math.min(rest, file.start - pos);
    						iter.add(new File(l, pos, size));
    						rest -= size;
    						iter.next();
    					}
    					
    					if(rest <= 0){
    						break;
    					}
    					
    					pos = file.start + file.size;
    				}
    				
    				if(rest > 0){
    					files.add(new File(l, pos, rest));
    				}
    				
    			}else if("D".equals(ops)){
    				final int l = sc.nextInt();
    				
    				for(ListIterator<File> iter = files.listIterator(); iter.hasNext(); ){
    					File file = iter.next();
    					
    					if(file.identifer == l){
    						iter.remove();
    					}
    				}
    				
    			}else if("R".equals(ops)){
    				final int P = sc.nextInt();
    				
    				boolean found = false;
    				for(ListIterator<File> iter = files.listIterator(); iter.hasNext(); ){
    					File file = iter.next();
    					
    					if(file.start <= P && P < file.start + file.size){
    						System.out.println(file.identifer);
    						found = true;
    						break;
    					}else if(file.start > P){
    						break;
    					}
    				}
    				
    				if(!found){
    					System.out.println(-1);
    				}
    			}
    		}
    		
    		System.out.println();
    		
    	}
    	
    	
    	
    }
     
}
