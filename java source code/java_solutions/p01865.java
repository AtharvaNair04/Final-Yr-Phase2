import java.io.*;
import java.util.*;
public class Main
{
	
	public void solve()
	{
		int L = nextInt();
		int N = nextInt();
		int left = 0;
		int right = 0;
		for(int i = 0;i < N;i++)
		{
			int x = nextInt();
			int w = nextInt();
			if(x > 0)
			{
				right += Math.abs(x) * w;
			}else if(x < 0)
			{
				left += Math.abs(x) * w;
			}
		}

		if(right == left)
		{
			out.println(0);
		}else if(right < left)
		{
			out.println(1);
			out.println(1 + " " + (left - right));
		}else
		{
			out.println(1);
			out.println(-1 + " " + (right - left));
		}
	}
	public static void main(String[] args)
	{
		out.flush();
		new Main().solve();
		out.close();
	}
	/*Input*/
	private static final InputStream in = System.in;
	private static final PrintWriter out = new PrintWriter(System.out);
	private final byte[] buffer = new byte[2048];
	private int p = 0;
	private int buflen = 0;
 
	private boolean hasNextByte()
	{
		if(p < buflen)return true;
		p = 0;
		try
		{
			buflen = in.read(buffer);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		if(buflen <= 0)return false;
		return true;
	}
	public boolean hasNext()
	{
		while(hasNextByte() && !isPrint(buffer[p]))
		{
			p++;
		}
		return hasNextByte();
	}
 
	private boolean isPrint(int ch)
	{
		if(ch >= '!' && ch <= '~')return true;
		return false;
	}
 
	private int nextByte()
	{
		if(!hasNextByte())return -1;
		return buffer[p++];
	}
 
	public String next()
	{
		if(!hasNext()) throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = -1;
		while(isPrint((b = nextByte())))
		{
			sb.appendCodePoint(b);
		}
		return sb.toString();
	}
 
	public int nextInt()
	{
		return Integer.parseInt(next());
	}
 
	public long nextLong()
	{
		return Long.parseLong(next());
	}
 
	public double nextDouble()
	{
		return Double.parseDouble(next());
	}
}
