import java.util.*;

class Stack{
    Object[] stack;
    int sp;
 
    Stack(int n){
	stack = new Object[n];
	sp = 0;
    }
    public void push(Object x){
	stack[sp++] = x;
    }
    public Object pop(){
	return stack[--sp];
    }
    public boolean isEmpty(){
	return sp==0;
    }
    public void clear(){
	sp = 0;
    }
}

public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    int m = kbd.nextInt();
	    if(n!=0 && m!=0){
		solve(n, m);
	    }
	}
    }

    static void solve(int n, int m){
	int i;
	int r = kbd.nextInt();
	int[][] time = new int[1000][m+1];
	Stack[] in = new Stack[m+1];
	for(i=0; i<in.length; i++){
	    in[i] = new Stack(r);
	}

	while(r>0){
	    put(time, in);
	    r--;
	}

	int q = kbd.nextInt();
	while(q>0){
	    answer(time);
	    q--;
	}
	for(i=0; i<in.length; i++){
	    in[i].clear();
	}
    }

    static void answer(int[][] time){
	int ts = kbd.nextInt();
	int te = kbd.nextInt();
	int m = kbd.nextInt();
	int i=0;
	int usetime = 0;

	while(time[i][m]!=0 && ts!=te){
	    if(ts < time[i][m]){
		if(te > time[i][m]){
		    if(te <= time[i+1][m]){
			usetime += te-time[i][m];
			ts = te;
		    }
		    else if(te > time[i+1][m]){
			usetime += time[i+1][m]-time[i][m];
			ts = time[i+1][m];
		    }
		}
	    }
	    else if(ts >= time[i][m]){
		if(ts < time[i+1][m]){
		    if(te <= time[i+1][m]){
			usetime += te-ts;
			ts = te;
		    }
		    else if(te > time[i+1][m]){
			usetime += time[i+1][m]-ts;
			ts = time[i+1][m];
		    }
		}
	    }
	    i+=2;
	}
	System.out.println(usetime);
    }

    static void put(int[][]time, Stack[] in){
	int t = kbd.nextInt();
	int n = kbd.nextInt();
	int m = kbd.nextInt();
	int s = kbd.nextInt();

	if(s==1){
	    in[m].push(t);
	}
	else if(s==0){
	    int e = (Integer)in[m].pop();
	    if(in[m].isEmpty()){
		int i=0;
		while(time[i][m]!=0) i++;
		time[i][m] = e;
		time[i+1][m] = t;
	    }
	}
    }
}
