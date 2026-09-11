import java.math.*;
import java.io.*;


class Main
{
    
    public static void main(String args[])throws IOException
    {
	BigInteger a = new BigInteger("1");
	for(int i=2;i<=1501;i++)
	    a = a.multiply(new BigInteger(Integer.toString(i)));
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));   
	a = a.add(new BigInteger("2"));
	int n = Integer.parseInt(in.readLine());
	System.out.println(a);
	for(int i=2;i<n+2;i++)
	    System.out.println(i);

    }

}
