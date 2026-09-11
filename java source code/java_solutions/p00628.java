import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try {
	    while(true){
		String st = sc.readLine();
		if(st.equals("END OF INPUT"))
		    break;
		String[] sp = st.split(" ");
		for(int i=0; i<sp.length; i++)
		    System.out.print(sp[i].length());
		System.out.println();
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
