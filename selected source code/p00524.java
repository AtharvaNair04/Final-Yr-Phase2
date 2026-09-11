import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.math.BigInteger;

public class Main{

static PrintWriter out;
static InputReader ir;

static void solve(){
 int n=ir.nextInt();
 int m=ir.nextInt();
 int x=ir.nextInt();
 int[] H=ir.nextIntArray(n);
 G g=new G(n,true,H);
 for(int i=0;i<m;i++){
  int from=ir.nextInt()-1;
  int to=ir.nextInt()-1;
  int cost=ir.nextInt();
  g.addEdge(from,to,cost);
 }
 long[] d=g.dijkstra(0,x);
 if(d[n-1]>=1L<<(long)55) out.println(-1);
 else if(d[n-1]<=x) out.println(2*d[n-1]+H[n-1]-x);
 else out.println(d[n-1]+H[n-1]);
}

public static void main(String[] args) throws Exception{
 ir=new InputReader(System.in);
 out=new PrintWriter(System.out);
 solve();
 out.flush();
}

static class G{

 AL[] g,rg;
 private int V;
 private boolean ndir;
 int[] H;

 public G(int V,boolean ndir,int[] H){
  this.V=V;
  this.ndir=ndir;
  g=new AL[V];
  for(int i=0;i<V;i++) g[i]=new AL();
  this.H=H;
 }

 public void addEdge(int u,int v,int t){
  g[u].add(new int[]{v,t});
  if(this.ndir) g[v].add(new int[]{u,t});
 }

 public void addEdge(int u,int v){
  addEdge(u,v,0);
 }

 public int to(int from,int ind){return g[from].get(ind)[0];}

 public int cost(int from,int ind){return g[from].get(ind)[1];}

 public int size(int from){return g[from].size();}

 public long[] dijkstra(int s,int x){
  long[] dist=new long[this.V];
  java.util.PriorityQueue<long[]> pque=new java.util.PriorityQueue<long[]>(11,new Comparator<long[]>(){
   public int compare(long[] a,long[] b){
    return Long.compare(a[0],b[0]);
   }
  });
  Arrays.fill(dist,1L<<(long)55);
  dist[s]=0;
  pque.offer(new long[]{0,s,x});
  while(!pque.isEmpty()){
   long[] p=pque.poll();
   int v=(int)p[1];
   long h=p[2];
   if(dist[v]<p[0]) continue;
   for(int i=0;i<g[v].size();i++){
    int to=to(v,i);
    long cost=(long)cost(v,i),re,nh;
    if(H[to]<h-cost){
     re=h-(long)H[to];
     nh=H[to];
    }
    else if(h-cost<=H[to]&&0<=h-cost){
     re=cost;
     nh=h-cost;
    }
    else if(H[v]>=cost){
     re=2*cost-h;
     nh=0;
    }
    else continue;
    if(dist[to]>dist[v]+re){
     dist[to]=dist[v]+re;
     pque.offer(new long[]{dist[to],to,nh});
    }
   }
  }
  return dist;
 }

 static class AL extends ArrayList<int[]>{};
}

static class InputReader {
 private InputStream in;
 private byte[] buffer=new byte[1024];
 private int curbuf;
 private int lenbuf;

 public InputReader(InputStream in) {this.in=in; this.curbuf=this.lenbuf=0;}
 
 public boolean hasNextByte() {
  if(curbuf>=lenbuf){
   curbuf= 0;
   try{
    lenbuf=in.read(buffer);
   }catch(IOException e) {
    throw new InputMismatchException();
   }
   if(lenbuf<=0) return false;
  }
  return true;
 }

 private int readByte(){if(hasNextByte()) return buffer[curbuf++]; else return -1;}
 
 private boolean isSpaceChar(int c){return !(c>=33&&c<=126);}
 
 private void skip(){while(hasNextByte()&&isSpaceChar(buffer[curbuf])) curbuf++;}
 
 public boolean hasNext(){skip(); return hasNextByte();}
 
 public String next(){
  if(!hasNext()) throw new NoSuchElementException();
  StringBuilder sb=new StringBuilder();
  int b=readByte();
  while(!isSpaceChar(b)){
   sb.appendCodePoint(b);
   b=readByte();
  }
  return sb.toString();
 }
 
 public int nextInt() {
  if(!hasNext()) throw new NoSuchElementException();
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
  if(!hasNext()) throw new NoSuchElementException();
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

 public double nextDouble(){return Double.parseDouble(next());}

 public int[] nextIntArray(int n){
  int[] a=new int[n];
  for(int i=0;i<n;i++) a[i]=nextInt();
  return a;
 }

 public long[] nextLongArray(int n){
  long[] a=new long[n];
  for(int i=0;i<n;i++) a[i]=nextLong();
  return a;
 }

 public char[][] nextCharMap(int n,int m){
  char[][] map=new char[n][m];
  for(int i=0;i<n;i++) map[i]=next().toCharArray();
  return map;
 }
}
}
