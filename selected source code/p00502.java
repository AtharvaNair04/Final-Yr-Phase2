import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.PriorityQueue;
import java.math.BigInteger;
    
public class Main{

static final InputStream in=System.in;
static final PrintWriter out=new PrintWriter(System.out);
static final int INF=Integer.MAX_VALUE/2;
static final long LINF=Long.MAX_VALUE/2;
static int D,N;
static int[] T,A,B,C;
static int[][] dp;

public static void main(String[] args) throws IOException{
InputReader ir=new InputReader(in);
D=ir.nextInt();
N=ir.nextInt();
T=ir.toIntArray(D);
A=new int[N];
B=new int[N];
C=new int[N];
for(int i=0;i<N;i++){
A[i]=ir.nextInt();
B[i]=ir.nextInt();
C[i]=ir.nextInt();
}
dp=new int[D][N];
for(int i=0;i<D-1;i++) Arrays.fill(dp[i],Integer.MIN_VALUE);
for(int i=D-2;i>=0;i--){
for(int j=N-1;j>=0;j--){
if(T[i]>=A[j]&&T[i]<=B[j]){
for(int k=0;k<N;k++){
if(T[i+1]>=A[k]&&T[i+1]<=B[k]&&dp[i+1][k]>=0) dp[i][j]=Math.max(dp[i][j],dp[i+1][k]+Math.abs(C[k]-C[j]));
}
}
}
}
int ans=0;
for(int i=0;i<N;i++) ans=Math.max(ans,dp[0][i]);
out.println(ans);
out.flush();
}

static class InputReader {
private InputStream in;
private byte[] buffer=new byte[1024];
private int curbuf;
private int lenbuf;

public InputReader(InputStream in) {this.in=in;}
  
public int readByte() {
if(lenbuf==-1) throw new InputMismatchException();
if(curbuf>=lenbuf){
curbuf= 0;
try{
lenbuf=in.read(buffer);
}
catch (IOException e) {
throw new InputMismatchException();
}
if(lenbuf<=0)
return -1;
}
return buffer[curbuf++];
}

public boolean isSpaceChar(int c){return !(c>=33&&c<=126);}

private int skip() {int b; while((b = readByte())!=-1&&isSpaceChar(b)); return b;}

public String next() {
int b=skip();
StringBuilder sb=new StringBuilder();
while(!isSpaceChar(b)){
sb.appendCodePoint(b);
b=readByte();
}
return sb.toString();
}
 
public int nextInt() {
int c=readByte();
while (isSpaceChar(c)) c=readByte();
boolean minus=false;
if (c=='-') {
minus=true;
c=readByte();
}
int res=0;
do{
if(c<'0'||c>'9') throw new InputMismatchException();
res=res*10+c-'0';
c=readByte();
}while(!isSpaceChar(c));
return (minus)?-res:res;
}

public long nextLong() {
int c=readByte();
while (isSpaceChar(c)) c=readByte();
boolean minus=false;
if (c=='-') {
minus=true;
c=readByte();
}
long res = 0;
do{
if(c<'0'||c>'9') throw new InputMismatchException();
res=res*10+c-'0';
c=readByte();
}while(!isSpaceChar(c));
return (minus)?-res:res;
}

public int[] toIntArray(int n){
int[] a=new int[n];
for(int i=0;i<n;i++) a[i]=nextInt();
return a;
}
}
}
