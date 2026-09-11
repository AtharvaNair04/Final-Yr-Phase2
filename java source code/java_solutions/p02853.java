import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Vector;
public class Main implements Runnable
	                {
	                    static final long MAX = 464897L;
	                    static class InputReader
	                    {
	                        private InputStream stream;
	                        private byte[] buf = new byte[1024];
	                        private int curChar;
	                        private int numChars;
	                        private SpaceCharFilter filter;
	                        private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	                 
	                        public InputReader(InputStream stream)
	                        {
	                            this.stream = stream;
	                        }
	                        
	                        public int read()
	                        {
	                            if (numChars==-1) 
	                                throw new InputMismatchException();
	                            
	                            if (curChar >= numChars)
	                            {
	                                curChar = 0;
	                                try 
	                                {
	                                    numChars = stream.read(buf);
	                                }
	                                catch (IOException e)
	                                {
	                                    throw new InputMismatchException();
	                                }
	                                
	                                if(numChars <= 0)                
	                                    return -1;
	                            }
	                            return buf[curChar++];
	                        }
	                     
	                        public String nextLine()
	                        {
	                            String str = "";
	                            try
	                            {
	                                str = br.readLine();
	                            }
	                            catch (IOException e)
	                            {
	                                e.printStackTrace();
	                            }
	                            return str;
	                        }
	                        public int nextInt()
	                        {
	                            int c = read();
	                            
	                            while(isSpaceChar(c)) 
	                                c = read();
	                            
	                            int sgn = 1;
	                            
	                            if (c == '-') 
	                            {
	                                sgn = -1;
	                                c = read();
	                            }
	                            
	                            int res = 0;
	                            do 
	                            {
	                                if(c<'0'||c>'9') 
	                                    throw new InputMismatchException();
	                                res *= 10;
	                                res += c - '0';
	                                c = read();
	                            }
	                            while (!isSpaceChar(c)); 
	                            
	                            return res * sgn;
	                        }
	                        
	                        public long nextLong() 
	                        {
	                            int c = read();
	                            while (isSpaceChar(c))
	                                c = read();
	                            int sgn = 1;
	                            if (c == '-') 
	                            {
	                                sgn = -1;
	                                c = read();
	                            }
	                            long res = 0;
	                            
	                            do 
	                            {
	                                if (c < '0' || c > '9')
	                                    throw new InputMismatchException();
	                                res *= 10;
	                                res += c - '0';
	                                c = read();
	                            }
	                            while (!isSpaceChar(c));
	                                return res * sgn;
	                        }
	                        
	                        public double nextDouble() 
	                        {
	                            int c = read();
	                            while (isSpaceChar(c))
	                                c = read();
	                            int sgn = 1;
	                            if (c == '-') 
	                            {
	                                sgn = -1;
	                                c = read();
	                            }
	                            double res = 0;
	                            while (!isSpaceChar(c) && c != '.') 
	                            {
	                                if (c == 'e' || c == 'E')
	                                    return res * Math.pow(10, nextInt());
	                                if (c < '0' || c > '9')
	                                    throw new InputMismatchException();
	                                res *= 10;
	                                res += c - '0';
	                                c = read();
	                            }
	                            if (c == '.') 
	                            {
	                                c = read();
	                                double m = 1;
	                                while (!isSpaceChar(c)) 
	                                {
	                                    if (c == 'e' || c == 'E')
	                                        return res * Math.pow(10, nextInt());
	                                    if (c < '0' || c > '9')
	                                        throw new InputMismatchException();
	                                    m /= 10;
	                                    res += (c - '0') * m;
	                                    c = read();
	                                }
	                            }
	                            return res * sgn;
	                        }
	                        
	                        public String readString() 
	                        {
	                            int c = read();
	                            while (isSpaceChar(c))
	                                c = read();
	                            StringBuilder res = new StringBuilder();
	                            do 
	                            {
	                                res.appendCodePoint(c);
	                                c = read();
	                            } 
	                            while (!isSpaceChar(c));
	                            
	                            return res.toString();
	                        }
	                     
	                        public boolean isSpaceChar(int c) 
	                        {
	                            if (filter != null)
	                                return filter.isSpaceChar(c);
	                            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
	                        }
	                     
	                        public String next() 
	                        {
	                            return readString();
	                        }
	                        
	                        public interface SpaceCharFilter 
	                        {
	                            public boolean isSpaceChar(int ch);
	                        }
	                    }
	                     
	                    public static void main(String args[]) throws Exception
	                    {
	                        new Thread(null, new Main(),"Main",1<<26).start();
	                    }   
	                    static long gcd(long a, long b) 
	                    { 
	                      if (b == 0) 
	                        return a; 
	                      return gcd(b, a % b);  
	                    } 
	                    static long lcm(long a,long b) {
	                        return (a*b)/gcd(a,b);
	                    }
	                    int maxn = 1000005;
	                    long MOD = 1000000007;
	                    long prime = 29;
	                    ArrayList<Node> adj[];
	                    HashMap<Integer,Integer> tmap = new HashMap();
	                    int[] val;
	                    static class Node implements Comparable<Node>{
	            			int destination;
	            			long weight;
	            			Node(int destination,long weight){
	            				this.destination = destination;
	            				this.weight = weight;
	            			}
	            			Node(){
	            				
	            			}
	            			@Override
	            			public int compareTo(Node n) {
	            				return Long.signum(weight - n.weight);
	            			}
	            		}
	                   
	                	public void run() 
	                    {
	                        InputReader sc = new InputReader(System.in);
	                        PrintWriter w = new PrintWriter(System.out);
	                        int n = sc.nextInt();
	                        int m = sc.nextInt();
	                        int[] a = new int[4];
	                        a[1] = 300000;
	                        a[2] = 200000;
	                        a[3] = 100000;
	                        int ans = 0;
	                        if(n == 1 && m == 1) {
	                        	ans += 400000;
	                        }
	                        if(n <= 3) {
	                        	ans += a[n];
	                        }
	                        if(m <= 3) {
	                        	ans += a[m];
	                        }
	                        //ans += a[n] + a[m];
	                        w.println(ans);
	                        w.close();
	                    } 
	                	long dijkstra(int a,int b){
	    					int[] parent = new int[adj.length];
	    					Arrays.fill(parent,-1);
	    					long[] dist = new long[adj.length];
	    					Arrays.fill(dist,Long.MAX_VALUE);
	    					PriorityQueue<Node> q = new PriorityQueue();
	    					q.add(new Node(a,0));
	    					dist[a] = 0;
	    					while(!q.isEmpty()){
	    						Node n = q.poll();
	    						if(dist[n.destination] < n.weight){
	    							continue;
	    						}
	    						for(Node v: adj[n.destination]){
	    							Node newer = new Node(v.destination,v.weight + n.weight);
	    							if(newer.weight < dist[newer.destination]){
	    								parent[newer.destination] = n.destination;
	    								dist[newer.destination] = newer.weight;
	    								q.add(newer);	
	    							}
	    						}
	    					}
	    					//for finding the path from last node to first node
	    					return dist[b];
	    				}
	    			
	                	void rec(Pair[] arr,ArrayList<Integer> ans,ArrayList<Character> config,int length,int prev) {
	                		Pair curr = new Pair(-1,-1,1);
	                		if(prev != -1) {
	                			if(config.get(config.size()-1) == 'b') {
	                				curr.a = arr[prev].b;
	                				curr.b = arr[prev].a;
	                			}else {
	                				curr = arr[prev];
	                			}
	                		}
	                		if(length == arr.length) {
	                			for(int i = 0;i < ans.size();i++) {
	                				System.out.println(ans.get(i) + 1 + " " + config.get(i));
	                			}
	                			System.exit(0);
	                		}
	                		for(int i = 0;i < arr.length;i++) {
	                			if(ans.contains(i)) {
	                				continue;
	                			}
	                			if(curr.a == -1) {
	                				ans.add(i);
	                				config.add('a');
	                				rec(arr,ans,config,length+1,i);
	                				ans.remove(ans.size()-1);
	                				config.remove(config.size()-1);
	                				ans.add(i);
	                				config.add('b');
	                				rec(arr,ans,config,length+1,i);
	                				ans.remove(ans.size()-1);
	                				config.remove(config.size()-1);
	                			}else {
	                				if(arr[i].a == curr.b) {
	                					ans.add(i);
		                				config.add('a');
		                				rec(arr,ans,config,length+1,i);
		                				ans.remove(ans.size()-1);
		                				config.remove(config.size()-1);
	                				}else if(arr[i].b == curr.b) {
	                					ans.add(i);
		                				config.add('b');
		                				rec(arr,ans,config,length+1,i);
		                				ans.remove(ans.size()-1);
		                				config.remove(config.size()-1);
	                				}
	                			}
	                		}
	                	}
	                	static final int MAXN = 10001; 
	                    
	                    // stores smallest prime factor for every number 
	                    static int spf[] = new int[MAXN]; 
	                       
	                    // Calculating SPF (Smallest Prime Factor) for every 
	                    // number till MAXN. 
	                    // Time Complexity : O(nloglogn) 
	                    static void sieve() 
	                    { 
	                        spf[1] = 1; 
	                        for (int i=2; i<MAXN; i++) 
	                       
	                            // marking smallest prime factor for every 
	                            // number to be itself. 
	                            spf[i] = i; 
	                       
	                        // separately marking spf for every even 
	                        // number as 2 
	                        for (int i=4; i<MAXN; i+=2) 
	                            spf[i] = 2; 
	                       
	                        for (int i=3; i*i<MAXN; i++) 
	                        { 
	                            // checking if i is prime 
	                            if (spf[i] == i) 
	                            { 
	                                // marking SPF for all numbers divisible by i 
	                                for (int j=i*i; j<MAXN; j+=i) 
	                       
	                                    // marking spf[j] if it is not  
	                                    // previously marked 
	                                    if (spf[j]==j) 
	                                        spf[j] = i; 
	                            } 
	                        } 
	                    } 
	                       
	                    // A O(log n) function returning primefactorization 
	                    // by dividing by smallest prime factor at every step 
	                    static void getFactorization(int x) 
	                    { 
	                        Vector<Integer> ret = new Vector<>(); 
	                        while (x != 1) 
	                        { 
	                            ret.add(spf[x]); 
	                            x = x / spf[x]; 
	                        } 
	                       
	                    } 
	                	int check(char[][] ch,int a,int b) {
	                		if(ch[a][b] == 'R'){
	                				return 1;
	                		}
	                		return 0;
	                	}
	                	static long power(long a,long b,long MOD) {
                            long ans = 1;
                            a = a % MOD;
                            while(b != 0) {
                                if(b % 2 == 1) {
                                    ans = (ans * a) % MOD;
                                }
                                a = (a * a) % MOD;
                                b = b/2;
                            }
                            return ans;
                        }
	                    class Equal implements Comparable<Equal>{
	                    	Character a;
	                    	int b;
	                    	public Equal(char a,int b){
	                    		this.a = a;
	                    		this.b = b;
	                    	}
							@Override
							public int compareTo(Equal an) {
								// TODO Auto-generated method stub
								return this.a.compareTo(an.a);
								
							}
	                    }
	                    class Pair implements Comparable<Pair>{
	                        long a;
	                        long b;
	                        long c;
	                        Pair(long a,long b,long c){
	                            this.b = b;
	                            this.a = a;
	                            this.c = c;
	                        }
	                        public boolean equals(Object o) {
	                            Pair p = (Pair)o;
	                            return this.a == p.a && this.b == p.b && this.c == p.c;
	                        }
	                        public int hashCode(){
	                            return Long.hashCode(a)*27 + Long.hashCode(b)*31;
	                        }
	                        public int compareTo(Pair p) {
	                       	     return Long.compare(this.a,p.a);
	                        }
	                    }
	                    
	                    class Pair3 implements Comparable<Pair3>{
	                        int a;
	                        int b;
	                        int c;
	                        Pair3(int a,int b,int c){
	                            this.b = b;
	                            this.a = a;
	                            this.c = c;
	                        }
	                        public boolean equals(Object o) {
	                            Pair3 p = (Pair3)o;
	                            return this.a == p.a && this.b == p.b && this.c == p.c;
	                        }
	                        public int hashCode(){
	                            return Long.hashCode(a)*27 + Long.hashCode(b)*31;
	                        }
	                        public int compareTo(Pair3 p) {
	                            return Long.compare(this.b,p.b);
	                        }
	                    }
	                    class Pair2 implements Comparable<Pair2>{
	                        int a;
	                        int b;
	                        int c;
	                        Pair2(int a,int b,int c){
	                            this.b = b;
	                            this.a = a;
	                            this.c = c;
	                        }
	                        public boolean equals(Object o) {
	                            Pair2 p = (Pair2)o;
	                            return this.a == p.a && this.b == p.b && this.c == p.c;
	                        }
	                        public int hashCode(){
	                            return Long.hashCode(a)*27 + Long.hashCode(b)*31 + Long.hashCode(c)*3;
	                        }
	                        public int compareTo(Pair2 p) {
	                            return Long.compare(p.a,this.a);
	                        }
	                    }
	                }
