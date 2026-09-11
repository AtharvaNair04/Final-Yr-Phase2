import java.io.*;

class Main{
    public static void main(String[] args){
	try{
	    BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
	    String st = sc.readLine();
	    System.out.println(st.toUpperCase());
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
