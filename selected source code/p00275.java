import java.util.*;
class Main{
    Scanner sc=new Scanner(System.in);
    void main(){
	while(true){
	    int n=sc.nextInt();
	    if(n==0)break;
	    int[]pr=new int[n];
	    int fld=0;
	    String cd=sc.next();
	    for(int i=0;i<100;i++){
		char ch=cd.charAt(i);
		if(cd.charAt(i)=='M')pr[i%n]++;
		if(cd.charAt(i)=='S'){
		    fld+=pr[i%n]+1;
		    pr[i%n]=0;
		}
		if(cd.charAt(i)=='L'){
		    pr[i%n]+=fld+1;
		    fld=0;
		}
	    }
	    for(int i=0;i<n-1;i++){
		int min=i;
		for(int j=i+1;j<n;j++){
		    if(pr[min]>pr[j]){
			int cj=pr[min];
			pr[min]=pr[j];
			pr[j]=cj;
		    }
		}
	    }
	    for(int i=0;i<n;i++)System.out.print(pr[i]+" ");
	    System.out.println(fld);
	}
    }
    public static void main(String[]ag){
	new Main().main();
    }
}
