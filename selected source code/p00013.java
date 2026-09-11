import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try {
	    String st;
	    LinkedList<String> lot = new LinkedList<String>();
	    while((st=sc.readLine())!=null){
		if(st.equals("0"))
		    System.out.println(lot.pop());
		else
		    lot.push(st);
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
