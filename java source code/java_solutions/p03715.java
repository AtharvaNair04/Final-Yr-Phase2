import java.util.*;
public class Main {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		int w=sc.nextInt();
		int h=sc.nextInt();
		long min=Integer.MAX_VALUE;
		if(w%3==0||h%3==0){
			System.out.println("0");
		}else{
			if(w%2==0&&h%2==0){
				//System.out.println(a);
				min=Math.min(c(w,h),t(w,h));
				min=Math.min(Math.min(c(h,w),t(h,w)),min);
				System.out.println(min);
			}else if((w%2!=0&&h%2==0)||(w%2==0&&h%2!=0)){
				
				min=Math.min(c(w,h),t(w,h));
				min=Math.min(Math.min(c(h,w),t(h,w)),min);
				System.out.println(min);
			}else{
				min=Math.min(c(w,h),t(w,h));
				min=Math.min(Math.min(c(h,w),t(h,w)),min);
				System.out.println(min);
			}
		}
		
	}
	public static long c(long w,long h){
		long a=h/2;
		long b=h/2+h%2;
		long min=Integer.MAX_VALUE;
		//System.out.println(w+" "+h+" "+a+" "+b);
		long d[]=new long[3];
		for(int i=1;i<=w;i++){	
			long p=i;long q=w-i;
			//System.out.println(p+q);
			d[0]=(q*a);
			d[1]=(q*b);
			d[2]=p*h;
			//System.out.println(d[0]+d[1]+d[2]);
			//System.out.println(sum1+" "+sum2+" "+sum3 );
			Arrays.sort(d);
			long sum=d[2]-d[0];
			if(sum<min){
				//System.out.println(p+" "+q+" "+(p*h)+" "+s1);
				min=(long)sum;
		
			}
			
		}
		return min;
	}
	public static long t(long w,long h){
		
		long min=Integer.MAX_VALUE;
		//System.out.println(w+" "+h+" "+a+" "+b);
		long d[]=new long[3];
		for(int i=1;i<=w;i++){	
			long p=i;long q=w-i;
			//System.out.println(p+q);
			long a=(q/2);
			long b=q-a;
			d[0]=(h*a);
			d[1]=(h*b);
			d[2]=p*h;
			//System.out.println(a+b+p);
			Arrays.sort(d);
			long sum=d[2]-d[0];
			if(sum<min){
				//System.out.println(p+" "+q+" "+(p*h)+" "+s1);
				min=(long)sum;
		
			}
			
		}
		return min;
	}
 
}
