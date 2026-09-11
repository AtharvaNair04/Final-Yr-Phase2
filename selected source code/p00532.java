import java.io.*;
import java.util.StringTokenizer;

class Main{

static final PrintWriter out=new PrintWriter(System.out);

public static void main(String[] args) throws IOException{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
String line="";
while((line=br.readLine())!=null&&!line.isEmpty()){
int n=Integer.parseInt(line);
int m=Integer.parseInt(br.readLine());
int[] t=new int[m];
int[] p=new int[n];
int[][] sel=new int[m][n];
StringTokenizer st1=new StringTokenizer(br.readLine());
for(int i=0;i<m;i++) t[i]=Integer.parseInt(st1.nextToken());
for(int i=0;i<m;i++){
StringTokenizer st2=new StringTokenizer(br.readLine());
for(int j=0;j<n;j++){
sel[i][j]=Integer.parseInt(st2.nextToken());
}
}
for(int i=0;i<m;i++){
int cnt=n;
for(int j=0;j<n;j++){
if(sel[i][j]==t[i]){
p[j]++;
cnt--;
}
}
p[t[i]-1]+=cnt;
}
for(int i=0;i<n;i++) out.println(p[i]);
out.flush();
}
}
}
