import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);

		
		HashMap<Character, HashSet<String>> map = new HashMap<Character, HashSet<String>>();
		for(char ch = 'a'; ch <= 'z'; ch++){
			map.put(ch, new HashSet<String>());
		}
		
		for(char first = 'a'; first <= 'z'; first++){
			map.get(first).add(first + "" + 'a');
			
			for(char second = 'a'; second <= 'z'; second++){
				map.get(first).add(first + "" + second + "" + 'a');
			}
		}
		
		//System.out.println(set);
		
		boolean first = true;
		String input  = "";
		String output = "";
		HashSet<String> set = new HashSet<String>();
		LinkedList<String> list = new LinkedList<String>();
		while(true){
			if(first){
				input = "aa";
				map.get('a').remove("aa");
				first = false;
			}else{
				char[] out = output.toCharArray();
				
				char[] prev_in = input.toCharArray();
				
				if(out[0] != prev_in[prev_in.length - 1]){
					System.out.println("!OUT");
					System.out.flush();
					return;
				}else if(set.contains(output)){
					System.out.println("!OUT");
					System.out.flush();
					return;
				}
				set.add(output);
				map.get(out[out.length - 1]).remove(output);
				
				for(String str : map.get(out[out.length - 1])){
					input = str;
					break;
				}
				
				map.get(out[out.length - 1]).remove(input);
			}
			
			list.add(input);
			
			System.out.println("?" + input);
			System.out.flush();
			set.add(input);
			try{
				output = sc.next();
				list.add(output);
			}catch(Exception e){
				System.err.println(list);
				//System.err.println(input + " " + output);
			}
		}
		
	}

}
