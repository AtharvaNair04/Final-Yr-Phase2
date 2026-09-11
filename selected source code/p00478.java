import java.util.*;
class Main{
    public static void main(String[] args)
    {
	Scanner in = new Scanner(System.in);
	String s,s2;
	int n,de=0;
	s = in.next();
	n = in.nextInt();
	for(int i=0;i<n;i++)
	    {
		s2 = in.next();
		int ln = s2.length();
s2 = s2 + s2;
		while(ln-- > 0)
		    {

			if(s2.contains(s))
			    {
				de++;
				break;
			    }
			
		    }
	    }
	System.out.println(de);
    }
} //Res***2Dツつウツづアツづ個づーツ参ツ考
