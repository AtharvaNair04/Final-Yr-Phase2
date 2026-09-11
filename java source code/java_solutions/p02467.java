import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n;
    boolean[] p;

    void run(){
	p = new boolean[100000000];
	setP();
	while(sc.hasNext()){
	    int n = sc.nextInt();
	    if(n>=2){
		System.out.print(n+":");
		solve(n);
	    }
	}
    }

    void solve(int n){
	int i=2;
	while(n>1){
	    // ??????????????????????卒???属??????
	    if(n<p.length && p[n]){
		System.out.print(" "+n);
		n = n/n;
		continue;
	    }
	    // ??????????其????????卒???属??????????????????????????????
	    if(i>=p.length){
		System.out.print(" "+n);
		n = 1;
		continue;
	    }
	    // ?卒???属??則????????????
	    while(p[i] && n%i==0){
		n = n/i;
		System.out.print(" "+i);
	    }
	    if(i>2) i+= 2;
	    else i++;
	}
	System.out.println();
    }

    void setP(){
	Arrays.fill(p, true);
	p[0] = p[1] = false;
	for(int i=2; i<p.length; i++){
	    if(p[i])
		for(int j=i*2; j<p.length; j+=i)
		    if(p[j]) p[j] = false;
	}
    }
}
