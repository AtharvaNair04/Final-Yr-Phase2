import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		   Scanner sc=new Scanner(System.in);
		    
		   String a=sc.next();
		   String b=sc.next();
		   
		  if(a.length()!=b.length()) {
			  if(a.length() > b.length()) {
				  System.out.println("GREATER");
				  System.exit(0);
			  }else {
				  System.out.println("LESS");
				  System.exit(0);
			  }
		  }
		   
		  if(a.compareTo(b)>0) {
			  System.out.println("GREATER");
		  }else if(a.compareTo(b)<0) {
			  System.out.println("LESS");
		  }else {
			  System.out.println("EQUAL");
		  }

	}

}
