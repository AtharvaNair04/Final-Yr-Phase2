import java.util.Scanner;

public class Main{
public static void main(String[]args){
Scanner sc = new Scanner(System.in);
int n=sc.nextInt();
while(n-->0){
int r=sc.nextInt(),t=sc.nextInt();String a="";
for(int y=0;y<(t%30>0?2:1);y++)
for(int x=0;x<(r%100>0?2:1);a+=(t/30+y)*5+r/100+x++)
if((x|y)==1)
a+=" ";
System.out.println(a);
}
}
}
