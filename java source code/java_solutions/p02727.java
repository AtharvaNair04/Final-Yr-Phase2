import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		int y = sc.nextInt();
		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = sc.nextInt();
		int[] p =new int[a];
		int[] q=new int[b];
		int[] r =new int[c]; 
		for(int i=0;i<a;i++){
			p[i]= sc.nextInt();
		}
		for(int i=0;i<b;i++){
			q[i]= sc.nextInt();
		}
		for(int i=0;i<c;i++){
			r[i]= sc.nextInt();
		}
		Arrays.sort(p);
		Arrays.sort(q);
		Arrays.sort(r);
      	int[] u=new int[x+y+c];
      	for(int i=0;i<x;i++){
          u[i]=p[a-1-i];
        }      	
      	for(int i=0;i<y;i++){
          u[x+i]=q[b-1-i];
        }
      	for(int i=0;i<c;i++){
          u[x+y+i]=r[c-1-i];
        }
		Arrays.sort(u);
      	long s=0;
		for(int i=0;i<x+y;i++){
			s+=u[x+y+c-1-i];
		}
		System.out.println(s);
    }
}
