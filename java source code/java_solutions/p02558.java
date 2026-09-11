import java.util.*;
public class Main {
static Scanner scan=new Scanner(System.in);
static int id[];
static void connect(int u,int v){
	int i = root(u);
	int j = root(v);
	id[i] = j;
	
}

static int root(int i) {
	while(i!=id[i]) {
		i=id[i];
		
	}
	return i;
}
static int connected(int u,int v){
	if( root(u)==root(v))
		return 1;
	else return 0;
}
	public static void main(String[] args) {
	//	int isconetted[]=new int [1000];
		id = new int[1000000];
		for (int i = 0; i < 1000000; i++) id[i] = i;		
int n=scan.nextInt(),Q=scan.nextInt();
int i=0;
while(i!=Q) {
	int q=scan.nextInt();
int u=scan.nextInt();
int v=scan.nextInt();
if(q==0) 
	connect(u, v);
	else if(q==1)
	System.out.println(connected(u, v));
	
i++;
}
/*for(int j=0;j<isconetted.length;j++)
{
System.out.println(isconetted[j]);	
}
	}
*/
}}
