import java.util.*;
import java.math.*;

public class Main 
{
	static int n;
	static int [] a=new int[20];
	public static int check1(BigInteger x)
	{
		BigInteger ans=BigInteger.ZERO,q=BigInteger.ONE;
		for(int i=0;i<=n;++i) 
		{
			ans=ans.add(q.multiply(BigInteger.valueOf(a[i])));
			q=q.multiply(x);
		}
		if(ans.compareTo(BigInteger.ZERO)==0) return 1;
		else return 0;
	}
	public static int check2(BigInteger x,BigInteger y)
	{
		BigInteger q1=BigInteger.ONE,q2=BigInteger.ZERO;
		BigInteger ax=BigInteger.ZERO,ay=BigInteger.ZERO;
		for(int i=0;i<=n;++i)
		{
			ax=ax.add(q1.multiply(BigInteger.valueOf(a[i])));
			ay=ay.add(q2.multiply(BigInteger.valueOf(a[i])));
			BigInteger tp1,tp2;
			tp1=q1.multiply(x);
			tp1=tp1.subtract(q2.multiply(y));
			tp2=q1.multiply(y);
			tp2=tp2.add(q2.multiply(x));
			q1=tp1;
			q2=tp2;
		}
		if(ax.compareTo(BigInteger.ZERO)==0&&ay.compareTo(BigInteger.ZERO)==0) return 1;
		else return 0;
	}
	public static void pt(BigInteger x,BigInteger y)
	{
		if(y.compareTo(BigInteger.ZERO)==0) System.out.print(x);
		else 
		{
			if(x.compareTo(BigInteger.ZERO)!=0) 
			{
				System.out.print(x);
				if(y.compareTo(BigInteger.ZERO)>0) 
				{
					if(y.compareTo(BigInteger.ONE)==0) System.out.print("+i");
					else System.out.print("+"+y+"i");
				}
				else
				{
					if(y.compareTo(BigInteger.valueOf(-1))==0) System.out.print("-i");
					else System.out.print(y+"i");
				}
			}
			else	
			{
				if(y.compareTo(BigInteger.ZERO)>0) 
				{
					if(y.compareTo(BigInteger.ONE)==0) System.out.print("i");
					else System.out.print(y+"i");
				}
				else
				{
					if(y.compareTo(BigInteger.valueOf(-1))==0) System.out.print("-i");
					else System.out.print(y+"i");
				}
			}
			
		}
	}
	public static void main(String[] args) 
	{
		Scanner cin=new Scanner(System.in);
		n=cin.nextInt();
		for(int i=0;i<=n;++i) a[i]=cin.nextInt();
		int l=0;
		while(l<=n)
		{
			if(a[l]==0) l++;
			else break;
		}
		int m=0;
		BigInteger [] rx=new BigInteger[20];
		BigInteger [] ry=new BigInteger[20];
		if(l>0)
		{
			rx[m]=BigInteger.ZERO;
			ry[m]=BigInteger.ZERO;
			m++;
			for(int i=l;i<=n;++i) a[i-l]=a[i];
			n-=l;
		}
		int w;
		if(a[0]<0) w=-a[0];
		else w=a[0];
		for(int i=1;i*i<=w;++i)
		{
			if(w%i!=0) continue;
			if(check1(BigInteger.valueOf(i))==1)
			{
				rx[m]=BigInteger.valueOf(i);
				ry[m]=BigInteger.ZERO;
				m++;
			}
			if(check1(BigInteger.valueOf(-i))==1)
			{
				rx[m]=BigInteger.valueOf(-i);
				ry[m]=BigInteger.ZERO;
				m++;
			}
			if(i*i<w&&check1(BigInteger.valueOf(w/i))==1)
			{
				rx[m]=BigInteger.valueOf(w/i);
				ry[m]=BigInteger.ZERO;
				m++;
			}
			if(i*i<w&&check1(BigInteger.valueOf(-w/i))==1)
			{
				rx[m]=BigInteger.valueOf(-w/i);
				ry[m]=BigInteger.ZERO;
				m++;
			}
		}
		for(int i=-1000;i<=1000;++i)
		{
			for(int j=-1000;j<=1000;++j)
			{
				if(j==0) continue;
				int s=i*i+j*j;
				if(w%s!=0) continue;
				if(check2(BigInteger.valueOf(i),BigInteger.valueOf(j))==1)
				{
					rx[m]=BigInteger.valueOf(i);
					ry[m]=BigInteger.valueOf(j);
					m++;
				}
			}
		}
		BigInteger tp;
		for(int i=0;i<m;++i)
		{
			for(int j=i+1;j<m;++j)
			{
				w=rx[i].compareTo(rx[j]);
				if(w==0) 
				{
					if(ry[i].compareTo(ry[j])>0)
					{
						tp=rx[i];
						rx[i]=rx[j];
						rx[j]=tp;
						tp=ry[i];
						ry[i]=ry[j];
						ry[j]=tp;
					}
				}
				else if(w>0)
					 {
						tp=rx[i];
						rx[i]=rx[j];
						rx[j]=tp;
						tp=ry[i];
						ry[i]=ry[j];
						ry[j]=tp;
					 }
			}
		}
		System.out.print(m+"\n");
		if(m>0) pt(rx[0],ry[0]);
		for(int i=1;i<m;++i)
		{
			System.out.print(" ");
			pt(rx[i],ry[i]);		
		}
		System.out.print("\n");
	}
	
}
