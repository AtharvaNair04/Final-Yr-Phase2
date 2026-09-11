import java.io.*;
import java.util.*;
public class Main
{
	public void solve()
	{
		int w = nextInt();
		int h = nextInt();
		int N = nextInt();
		int in = 1;
		int ou = 1;
		int cnt = 0;
		for(int i = 0;i < N;i++)
		{
			int p = nextInt();
			if(p == 0)
			{
				ou++;
			}else
			{
				in++;
			}
			int s = 0;
			if(ou == w || ou == w + h - 1)continue;
			if(ou <= w - 1 && in <= w - 1)
			{
				s = 0;
			}else if(ou >= w + 1 && ou <= w + h - 2 && in >= w - 1 && in <= w - 2 + h - 2)
			{
				s = 2;
			}else if(ou <= 2 * w + (h - 2) && ou >= w + h && in >= w + h - 4 && in <= 2 * (w - 1) + (h - 4))
			{
				s = 4;
			}else continue;

			if(ou - s == in)
			{
				cnt++;
			}
		}
		out.println(cnt);
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
