import java.util.LinkedList;
import java.util.Scanner;
 
 
public class Main{
	
    public static void main(String[] args){
    	
    	final Scanner sc = new Scanner(System.in);
    	
    	String input = sc.next();
    	
    	LinkedList<LinkedList<String>> parse = new LinkedList<LinkedList<String>>();
    	
    	String prev = "";
    	LinkedList<String> tmp = new LinkedList<String>();
    	while(!input.isEmpty()){
    		//System.out.println(input);
    		
    		if(input.startsWith("egg")){
    			if(prev.equals("egg")){
    				parse.add(tmp);
    				tmp = new LinkedList<String>();
    			}
    			tmp.add("egg");
    			prev = "egg";
    			
    			input = input.substring("egg".length());
    		}else{
    			if(prev.equals("chicken")){
    				parse.add(tmp);
    				tmp = new LinkedList<String>();
    			}
    			tmp.add("chicken");
    			prev = "chicken";
    			
    			input = input.substring("chicken".length());
    		}
    	}
    	
    	if(!tmp.isEmpty()){
    		parse.add(tmp);
    	}
    	
    	//System.out.println(parse);
    	
    	int max = Integer.MIN_VALUE;
    	String str = null;
    	for(LinkedList<String> list : parse){
    		if(max < list.size()){
    			max = list.size();
    			str = list.getLast();
    		}
    	}
    	
    	System.out.println(str);
    }
     
}
