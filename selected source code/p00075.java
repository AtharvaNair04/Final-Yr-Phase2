import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try {
	    String st;
	    while((st=sc.readLine())!=null){
		String[] sp = st.split(",");
		double tai = Double.valueOf(sp[1]);
		double shin = Double.valueOf(sp[2]);
		if(tai/(shin*shin)>=25.0)
		    System.out.println(sp[0]);
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
