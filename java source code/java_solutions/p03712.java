import java.util.*;
public class Main
{
public static void main(String args[])
{
Scanner in=new Scanner(System.in);
int H=in.nextInt();
int W=in.nextInt();
String dum=in.nextLine();
String s[]=new String[H];
for (int i=0; i<H; i++)
s[i]=in.nextLine();
for(int i=0; i<W+2; i++)
System.out.print("#");
System.out.println("");
for (int i=0; i<H; i++)
System.out.println("#"+s[i]+"#");
for(int i=0; i<W+2; i++)
System.out.print("#");
}
}
