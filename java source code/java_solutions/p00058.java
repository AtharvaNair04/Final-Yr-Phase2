import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try {
	    String st;
	    while((st=sc.readLine())!=null){
		String[] sp = st.split(" ");
		double[] map = new double[8];
		for(int i=0; i<8; i++)
		    map[i] = Double.valueOf(sp[i]);
		if(map[0] == map[2])
		    if(map[5] == map[7])
			System.out.println("YES");
		    else
			System.out.println("NO");
		else if(map[4] == map[6])
		    if(map[1] == map[3])
			System.out.println("YES");
		    else
			System.out.println("NO");
		else{
		    if((map[3]-map[1])/(map[2]-map[0])*(map[7]-map[5])/(map[6]-map[4]) == -1)
			System.out.println("YES");
		    else
			System.out.println("NO");
		}
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
