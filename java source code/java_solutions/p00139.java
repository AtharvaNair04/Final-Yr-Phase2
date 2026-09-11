import java.util.*;
import java.lang.*;
import java.io.*;

class Main{

    private static boolean snake_A(String snake)
    {
	if(snake.charAt(snake.length()-1) != '~')
	    return false;

	if(snake.charAt(snake.length()-2) != '=')
	    return false;

	if(!snake.substring(0,2).equals(">'"))		
	    return false;

	if(snake.indexOf("#") == -1)
	    return false;


	if(snake.indexOf("#") > snake.indexOf("~"))
	    return false;

	if(snake.indexOf("#")-snake.indexOf("'") != snake.indexOf("~")-snake.indexOf("#"))
	    return false;

	int left,right;
	left = right = 0;
	for(int i=2;i<snake.indexOf("#");i++)
	    {
	
		if(snake.charAt(i) == '=')
		    left++;
		else 
		    {
			return false;
		    }
	    }

	for(int i=snake.indexOf("#")+1;i<snake.indexOf("~");i++)
	    {
	
		if(snake.charAt(i) == '=')
		    right++;
		else 
		    return false;
	    }	

	
	if(left != right)
	    return false;

	return true;
    }

    private static boolean snake_B(String snake)
    {

	if(!snake.substring(snake.length()-2,snake.length()).equals("~~"))
	    return false;

	if(snake.charAt(snake.length()-3) != '=')
	    return false;
 
	if(!snake.substring(0,2).equals(">^"))
	    return false;

	int cnt = 0;
	for(int i=2;i<snake.indexOf("~");i+=2)
	    {
		if(snake.substring(i,i+2).equals("Q="))
		    cnt++;
		else 
		    return false;
	    }

	if(cnt < 1)
	    return false;
	
	return true;
    }


      public static void main(String args[])throws IOException{
	  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	  
	  int n;
	  n = Integer.parseInt(in.readLine());

	  while(n-- > 0)
	      {
		  String snake = in.readLine();
		 
		  if(snake_A(snake))
		      {
			  System.out.println("A");
		      }
		  else if(snake_B(snake))
		      {
			  System.out.println("B");
		      }
		  else
		      {
			  System.out.println("NA");
		      }


	      }
      }

}
