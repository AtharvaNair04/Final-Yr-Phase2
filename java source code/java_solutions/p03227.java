import java.util.*;
import java.io.*;

class Main
{
    public static void main(String[] args)
    {
	    // Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
		// Scanner in = new Scanner(Syste8
    	Scanner in = new Scanner(System.in);
    	Main s = new Main();
    	s.qa(in.nextLine());
	}

	void qa(String str)
	{
		String rev = new StringBuilder(str).reverse().toString();
		if(str.length() == 2) System.out.println(str);
		else System.out.println(rev);
	}
}
