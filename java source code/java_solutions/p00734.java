import java.io.*;
import java.util.*;
import java.math.*;
class Main{
    public static void main(String[] args){
    BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
    try {
		while(true){
		String[] st = sc.readLine().split(" ");
		int n = Integer.valueOf(st[0]);
		int m = Integer.valueOf(st[1]);
		if(n==0 && m==0)
			break;
		int sumA=0, sumB=0, min = 300, first=0, last=0, flag = 0;
		int a[] = new int[n];
		int b[] = new int[m];
		for(int i=0; i<n; i++){
			a[i] = Integer.valueOf(sc.readLine());
			sumA += a[i];
			}
		for(int i=0; i<m; i++){
			b[i] = Integer.valueOf(sc.readLine());
			sumB += b[i];
			}
		for(int i=0; i<n; i++)
			for(int j=0; j<m; j++){
				if((sumA-a[i]+b[j] == sumB+a[i]-b[j]) && (a[i]+b[j]<min)){
					first = a[i];
					last = b[j];
					min = a[i]+b[j];
					flag = 1;
					}
				}
		if(flag==1)
			System.out.println(first+" "+last);
		else
			System.out.println(-1);
		}
    }catch(Exception e){
        System.out.println("Error");
    }
    }
}
