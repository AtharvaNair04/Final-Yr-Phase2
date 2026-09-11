import java.util.Scanner;

public class Main {
static int max(int x,int y,int z){
	int max=-10001;
	if(x>max)max=x;
	if(y>max)max=y;
	if(z>max)max=z;
	return max;
}
public static void main(String[] args) {
	Scanner sc=new Scanner(System.in);
	int a=sc.nextInt();
	int b =sc.nextInt();
	System.out.println(max((a-b), (a+b), (a*b)));
	sc.close();
}
}
