import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.Point;

class Main{

      public static void main(String args[])throws IOException
    {
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	int N = Integer.parseInt(in.readLine());	
	LinkedList<Point> list = new LinkedList<Point>();	
	for(int i=0;i<N;i++)
	    {
		String line = in.readLine();
		int A,B;
		A = Integer.parseInt(line.substring(0,line.indexOf(" ")));
		B = Integer.parseInt(line.substring(line.indexOf(" ")+1));
		list.add(new Point(B-A,B));
	    }
	

	Collections.sort(list,new Comparator<Point>(){
                        @Override
                            public int compare(Point a,Point b)
                        {
			    if(a.x < b.x)
				{
				    return 1;
				}	
			    else if(a.x == b.x)
				{
				    return 0;
				}	
			    else
				{
				    return -1;
				}	    
                        }
	    });

	int cnt = 0;
	for(int i=0;i<list.size();i++)
	    {
		if(list.get(i).x >= 0)
		    {
			cnt++;
			for(int j=i+1;j<list.size();j++)
			    {
				
				if(list.get(j).y > list.get(i).x)
				    {
					int dif = Math.min(list.get(j).y-list.get(i).x,list.get(i).y-list.get(i).x);
					Point p = list.get(j);
					list.set(j,new Point(p.x-dif,p.y));
					
				    }
				

			    }

			Collections.sort(list,new Comparator<Point>(){
				@Override
				    public int compare(Point a,Point b)
				{
				    if(a.x < b.x)
				{
				    return 1;
				}	
				    else if(a.x == b.x)
					{
					    return 0;
					}	
				    else
					{
				    return -1;
					}	    
				}
			    });
			
		    }
		
		
		
	    }

	
	System.out.println(cnt);	

    }

}
