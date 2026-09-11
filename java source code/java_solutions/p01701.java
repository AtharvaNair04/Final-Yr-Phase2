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
 String s;
 for(;;){
  s=ir.next();
  if(s.equals("#")) return;
  ArrayList<Integer> dir=new ArrayList<>();
  int n=0,w=0;
  for(int i=0;i<s.length();){
   if(s.charAt(i)=='n'){
    n++;
    dir.add(0);
    i+=5;
   }
   else if(s.charAt(i)=='w'){
    w++;
    dir.add(1);
    i+=4;
   }
  }
  out.println(ang(0,n+w,dir).toString());
 }
}

public static Fraction ang(int dep,int tot,ArrayList<Integer> dir){
 if(dep==tot-1){
  if(dir.get(dep)==0) return new Fraction(0,1);
  else return new Fraction(90,1);
 }
 int now=dir.get(dep);
 if(now==0) return ang(dep+1,tot,dir).subtract(new Fraction(90,1<<(tot-dep-1)));
 else return ang(dep+1,tot,dir).add(new Fraction(90,1<<(tot-dep-1)));
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
