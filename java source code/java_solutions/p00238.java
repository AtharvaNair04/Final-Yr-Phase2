import java.util.Scanner;

public class Main{
    public static void main(String[] args){
	Scanner sc = new Scanner(System.in);
	int t=0,n=0,s=0,f=0,st=0;

	while(sc.hasNext()){
	    st = 0;

	    t = sc.nextInt();

	    if(t==0) break;

	    n = sc.nextInt();

	    for(int i = 0;i<n;i++){
		s = sc.nextInt();
		f = sc.nextInt();
		st += f-s;
	    }

	    if(st>=t){
		System.out.println("OK");
	    }else{
		System.out.println(t-st);
	    }
	}
    }
}
