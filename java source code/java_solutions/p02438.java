import java.io.*;
import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		PrintWriter out=new PrintWriter(System.out);
		int n=Integer.parseInt(in.next()),q=Integer.parseInt(in.next());
		mylist A[]=new mylist[n];
		for(int i=0;i<n;i++)A[i]=new mylist();
		
		for(int i=0;i<q;i++){
			int query=Integer.parseInt(in.next());
			
			switch(query){
			case 0:
				int t1=Integer.parseInt(in.next()),x=Integer.parseInt(in.next());
				A[t1].add(x);
				break;
				
			case 1:
				int t2=Integer.parseInt(in.next());
				if(A[t2].size==0)out.println();
				else{
					node NOW=A[t2].END.next;
					for(int j=0;j<A[t2].size-1;j++){
						out.print(NOW.v+" ");
						NOW=NOW.next;
					}
					out.println(NOW.v);
				}
				break;
				
			case 2:
				int t3=Integer.parseInt(in.next()),t4=Integer.parseInt(in.next());
				A[t3].END.prev.next=A[t4].END;
				A[t3].END.next.prev=A[t4].END.prev;
				A[t4].END.prev.next=A[t3].END.next;
				A[t4].END.prev=A[t3].END.prev;
				A[t4].size+=A[t3].size;
				A[t3].clearlist();
				break;
			}
		}
		out.flush();
	}

}

class node{
	int v;
	node prev,next;
}

class mylist{
	int size;
	node END;
	
	mylist(){
		END=new node();
		END.prev=END;
		END.next=END;
		size=0;
	}
	
	void add(int x){
		node NEW=new node();
		{
			NEW.v=x;
			NEW.next=END;
			NEW.prev=END.prev;
		}
		{
			NEW.prev.next=NEW;
			NEW.next.prev=NEW;
		}
		size++;
	}
	
	void clearlist(){
		END.prev=END;
		END.next=END;
		size=0;
	}
}
