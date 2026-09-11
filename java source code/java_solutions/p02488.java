import java.util.*;
class Main{
public static void main(String[] args){
Scanner s=new Scanner(System.in);
int a=s.nextInt();
String []r=new String[a];
for(int i=0;i<a;i++)r[i]=s.next();
Arrays.sort(r);System.out.println(r[0]);
}
}
