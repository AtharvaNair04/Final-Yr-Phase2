/*	/ ﾌﾌ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ム
	/ )\⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  Y
	(⠀⠀| ( ͡° ͜ʖ ͡°）⠀⌒(⠀ ノ
	(⠀ ﾉ⌒ Y ⌒ヽ-く __／
	| _⠀｡ノ| ノ｡ |/
	(⠀ー '_人`ー ﾉ
	⠀|\ ￣ _人'彡ﾉ
	⠀ )\⠀⠀ ｡⠀⠀ /
	⠀⠀(\⠀ #⠀ /
	⠀/⠀⠀⠀/ὣ====================D-
	/⠀⠀⠀/⠀ \ \⠀⠀\
	( (⠀)⠀⠀⠀⠀ ) ).⠀)
	(⠀⠀)⠀⠀⠀⠀⠀( | /
	|⠀ /⠀⠀⠀⠀⠀⠀ | /
	[_] ⠀⠀⠀⠀⠀[___]                     */
// Main Code at the Bottom
import java.util.*;
import java.lang.*;
import java.io.*;
import java.math.BigInteger; 
public class Main {
	//Fast IO class
    static class FastReader {
        BufferedReader br; 
        StringTokenizer st; 
        public FastReader() {
        	boolean env=System.getProperty("ONLINE_JUDGE") != null;
        	if(env) {
        		try {
					br=new BufferedReader(new FileReader("src\\input.txt"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
        	}
        	else br = new BufferedReader(new InputStreamReader(System.in)); 
        } 
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine()); 
                } 
                catch (IOException  e) {
                    e.printStackTrace(); 
                } 
            } 
            return st.nextToken(); 
        } 
        int nextInt() {
            return Integer.parseInt(next()); 
        } 
        long nextLong() {
            return Long.parseLong(next()); 
        } 
        double nextDouble() {
            return Double.parseDouble(next()); 
        } 
        String nextLine() {
            String str = ""; 
            try {
                str = br.readLine(); 
            } 
            catch (IOException e) {
                e.printStackTrace(); 
            } 
            return str; 
        } 
    }     
    static long MOD=1000000000+7;
    //Euclidean Algorithm
    static long gcd(long A,long B){
    	return (B==0)?A:gcd(B,A%B);
    }
    //Modular Exponentiation
    static long fastExpo(long x,long n){
        if(n==0) return 1;
        if((n&1)==0) return fastExpo((x*x)%MOD,n/2)%MOD;
        return ((x%MOD)*fastExpo((x*x)%MOD,(n-1)/2))%MOD;
    }
    //Modular Inverse
    static long inverse(long x) {
    	return fastExpo(x,MOD-2);
    }
    //Prime Number Algorithm
    static boolean isPrime(long n){
        if(n<=1) return false;
        if(n<=3) return true;
        if(n%2==0 || n%3==0) return false;
        for(int i=5;i*i<=n;i+=6) if(n%i==0 || n%(i+2)==0) return false;
        return true;
    }
    //Reverse an array
    static void reverse(long arr[],int l,int r){
    	while(l<r) {
    		long tmp=arr[l];
    		arr[l++]=arr[r];
    		arr[r--]=tmp;
    	}
    }
    //Print array
    static void print1d(int arr[]) {
    	out.println(Arrays.toString(arr));
    }
    static void print2d(int arr[][]) {
    	for(int a[]: arr) out.println(Arrays.toString(a));
    }
    // Pair
    static class pair{
    	int x,y;
    	pair(int a,int b){
    		this.x=a;
    		this.y=b;
    	}
    	public boolean equals(Object obj) {
    		if(obj == null || obj.getClass()!= this.getClass()) return false;
            pair p = (pair) obj;
            return (this.x==p.x && this.y==p.y);
        }
    	public int hashCode() {
            return Objects.hash(x,y);
        }
    }
    static FastReader sc=new FastReader();
    static PrintWriter out=new PrintWriter(System.out);  
    //Main function(The main code starts from here)
    static long fact[]=new long[1000000+1];
    static long C(long n,long r) {
    	long num=1;
    	for(long i=n-r+1;i<=n;i++) num=((num%MOD)*(i%MOD))%MOD;
    	long denom=fact[(int)r];
    	long ans=((num%MOD)*(inverse(denom)%MOD))%MOD;
    	return ans;
    }
    public static void main (String[] args) throws java.lang.Exception {
    	int test;
    	test=1;
    	//test=sc.nextInt();
    	while(test-->0){
    		fact[0]=1;
    		for(int i=1;i<=1000000;i++) fact[i]=((i%MOD)*(fact[i-1]%MOD))%MOD;
    		long n=sc.nextLong(),a=sc.nextLong(),b=sc.nextLong();
    		long sum=(fastExpo(2,n)%MOD-1%MOD+MOD)%MOD;
    		long res=(C(n,a)%MOD+C(n,b)%MOD)%MOD;
    		long ans=(sum%MOD-res%MOD+MOD)%MOD;
    		out.println(ans%MOD);
    		
        } 	
        out.flush();
        out.close();
    }
}
