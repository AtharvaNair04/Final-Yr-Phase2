import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.math.BigInteger;
import java.text.DecimalFormat;
 
public class Main{
 
static PrintWriter out;
static InputReader ir;
 
static void solve(){
 for(;;){
  int n=ir.nextInt();
  int w=ir.nextInt();
  if(n==0&&w==0) return;
  int[] v=ir.nextIntArray(n);
  int ma=0;
  int ma_h=0;
  int[] h=new int[11];
  for(int i=0;i<n;i++){
   int p=(int)(v[i]/w);
   h[p]++;
   ma=Math.max(ma,p);
  }
  for(int i=0;i<=ma;i++) ma_h=Math.max(ma_h,h[i]);
  Fraction ans=new Fraction(1,100);
  DecimalFormat df=new DecimalFormat("0.000000000");
  for(int i=0;i<ma;i++){
   ans=ans.add(new Fraction(ma-i,ma).multiply(new Fraction(h[i],ma_h)));
  }
  out.println(df.format(ans.doubleValue()));
 }
}
 
public static long gcd(long a,long b){
 if(b==0) return a;
 return gcd(b,a%b);
}
 
static class Fraction{
  
 long de,nu;
  
 public Fraction(long nu,long de){
  this.nu=nu;
  this.de=de;
  long g=gcd(nu,de);
  this.nu/=g;
  this.de/=g;
  if(nu==0) this.de=1;
 }
  
 public Fraction add(Fraction a){
  return reduct(new Fraction(this.nu*a.de+this.de*a.nu,this.de*a.de));
 }
  
 public Fraction subtract(Fraction a){
  return reduct(new Fraction(this.nu*a.de-this.de*a.nu,this.de*a.de));
 }
  
 public Fraction multiply(Fraction a){
  return reduct(new Fraction(this.nu*a.nu,this.de*a.de));
 }
  
 public Fraction divide(Fraction a){
  return reduct(new Fraction(this.nu*a.de,this.de*a.nu));
 }
  
 public Fraction reduct(Fraction a){
  long g=gcd(a.nu,a.de);
  return new Fraction(a.nu/g,a.de/g);
 }
 
 public double doubleValue(){
  return (double)this.nu/(double)this.de;
 }
 
 public String toString(){
  if(this.de==1) return Long.toString(this.nu);
  else return Long.toString(this.nu)+"/"+Long.toString(this.de);
 }
}
 
 
public static void main(String[] args) throws Exception{
 ir=new InputReader(System.in);
 out=new PrintWriter(System.out);
 solve();
 out.flush();
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
