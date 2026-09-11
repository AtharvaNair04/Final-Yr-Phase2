/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Main
{
	public static void main (String[] args) throws java.lang.Exception
	{
		// your code goes here
        Scanner sc = new Scanner(System.in);
		
		while(sc.hasNext()){
			int a = sc.nextInt();
			if (a != 0){
				int count = 0;
				while(a != 1){
					count++;
					if(a%2 == 0){
						a = a / 2;
					}else{
						a = a * 3 + 1;
					}
					//System.out.println(count + "回目：" + a);
				}
				System.out.println(count);
			}
		}
	}
}
