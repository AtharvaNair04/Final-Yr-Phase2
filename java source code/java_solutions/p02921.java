import java.util.*;
public class Main{
	public static void main(String args []){
    	Scanner sc=new Scanner(System.in);
      	char a[]=sc.next().toCharArray();
      	char b[]=sc.next().toCharArray();
      	int count=0;
      	for(int i=0;i<a.length;i++){
        	if(a[i]==b[i]){
            	count++;
            }
        }
      	System.out.println(count);
      
    }
}
