import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	int xx=0, ip;
	double[] array = new double[1000];
	Scanner sc = new Scanner(System.in);
	
	while(true){
	    int suma=0, sumb=0;
	    ip = sc.nextInt();
	    if(ip==0)
		break;
	    int[] x = new int[ip];
	    int[] y = new int[ip];
	    for(int i=0; i<ip; i++){
		x[i]=sc.nextInt();
		y[i]=sc.nextInt();
	    }
	    
	    for(int i=0; i<ip-1; i++){
		suma+=x[i]*y[i+1];
		sumb+=x[i+1]*y[i];
	    }
	    suma+=x[ip-1]*y[0];
	    sumb+=x[0]*y[ip-1];
	    array[xx] = Math.abs((double)(suma-sumb)/2.0);
	    xx++;
	}
	for(int pp=0; array[pp]!=0; pp++)
	    System.out.println((pp+1)+" "+array[pp]);
    }
}
