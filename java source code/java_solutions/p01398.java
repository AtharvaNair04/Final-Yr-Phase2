import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.Point;

class Main{


      public static void main(String args[])throws IOException
    {
	  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	  while(true)
	      {
		  int N;
		  N = Integer.parseInt(in.readLine());
		  if(N == 0)
		      break;

		  StringBuilder message = new StringBuilder(in.readLine());
		  List<Point> list = new ArrayList<Point>();
		  for(int i=0;i<N;i++)
		      {
			  String ab = in.readLine();
			  int a,b;
			  a = Integer.parseInt(ab.substring(0,ab.indexOf(" ")));
			  b = Integer.parseInt(ab.substring(ab.indexOf(" ")+1));
			  Point p = new Point(a,b);
			  list.add(p);
		      }
		  Collections.reverse(list);

		  for(int i=0;i<N;i++)
		      {
			  int a,b,dif;
			  a = list.get(i).x;
			  b = list.get(i).y;
			  
			  a--;
			  b--;
			  dif = Math.abs(a-b);
			  char ca,cb;
			  ca = message.charAt(a);
			  cb = message.charAt(b);

			  ca = (char)((ca+dif-'a')%26 + 'a');
			  cb = (char)((cb+dif-'a')%26 + 'a');


			  message.replace(a,a+1,String.valueOf(cb));
			  message.replace(b,b+1,String.valueOf(ca));
			 
		      }
		  System.out.println(message);

	      }
      }

}
