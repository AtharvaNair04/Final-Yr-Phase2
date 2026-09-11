import java.util.*;
class Main{
    Main(){
	Scanner sc =new Scanner(System.in);
	int[] c={1,5,10,50,100,500};
	int x=0;
	for(int i=0;i<6;i++){
	    int a=sc.nextInt();
	    x=x+(c[i]*a);
	}
	int y=0;
	if(x>=1000)y=1;
	System.out.println(y);
    }
    public static void main (String[]arg){
	new Main();
    }
}
