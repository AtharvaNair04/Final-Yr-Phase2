/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Main
{
	public static void main (String[] args) throws java.lang.Exception
	{
		Scanner sc = new Scanner(System.in);
		
		int a = sc.nextInt(), b = sc.nextInt(),c = sc.nextInt();
		
            	if(a+b >= c){
            		System.out.println("Yes");
            	} else {
           			System.out.println("No");
		}
	}
}
