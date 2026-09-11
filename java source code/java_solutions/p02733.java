import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Main{
	final Scanner s=new Scanner(System.in);

	final int h=gInt();
	final int w=gInt();
	final int k=gInt();

	int[][] f(int v,byte[][] a){
		int c=0;
		int[][] r=new int[1+Integer.bitCount(v)][w];
		for(int i=0;i<h;++i){
			for(int j=0;j<w;++j)
				r[c][j]+=a[i][j];

			if((v&(1<<i))>0)
				++c;
		}
		//System.err.println(Integer.toBinaryString(v));
		return r;
	}

	int g(int[][] a){
		int h=a.length;
		int[] c=new int[h];

		int r=0;

		for(int j=0;j<w;++j){
			boolean f=false;
			for(int i=0;i<h&&!f;++i) {
				f|=c[i]+a[i][j]>k;
				if(a[i][j]>k)
					return 999999999;
			}

			if(f){
				++r;
				for(int i=0;i<h;++i)
					c[i]=a[i][j];
			}else{
				for(int i=0;i<h;++i)
					c[i]+=a[i][j];
			}
			//System.err.println(Arrays.toString(c));
		}

		return r;
	}

	void solve(){
		byte[][] a=new byte[h][w];
		for(int i=0;i<h;++i){
			String t=s.next();
			for(int j=0;j<w;++j)
				a[i][j]=(byte)(t.charAt(j)-'0');
		}

		System.out.println(IntStream.range(0,1<<(h-1))
				.map(i->Integer.bitCount(i)+g(f(i,a)))
				.min().getAsInt());
	}

	public static void main(String[] A){
		new Main().solve();
	}

	int gInt(){
		return Integer.parseInt(s.next());
	}
	long gLong(){
		return Long.parseLong(s.next());
	}
	double gDouble(){
		return Double.parseDouble(s.next());
	}

	SupplyingIterator<Integer> ints(int n){
		return new SupplyingIterator<>(n,this::gInt);
	}
	SupplyingIterator<Long> longs(int n){
		return new SupplyingIterator<>(n,this::gLong);
	}
	SupplyingIterator<Double> doubles(int n){
		return new SupplyingIterator<>(n,this::gDouble);
	}
	SupplyingIterator<String> strs(int n){
		return new SupplyingIterator<>(n,s::next);
	}

	Range rep(int i){
		return Range.rep(i);
	}
	Range rep(int f,int t,int d){
		return Range.rep(f,t,d);
	}
	Range rep(int f,int t){
		return rep(f,t,1);
	}
	Range rrep(int f,int t){
		return rep(t,f,-1);
	}

	IntStream REP(int v){
		return IntStream.range(0,v);
	}
	IntStream REP(int l,int r){
		return IntStream.rangeClosed(l,r);
	}

	IntStream INTS(int n){
		return IntStream.generate(this::gInt).limit(n);
	}
	LongStream LONGS(int n){
		return LongStream.generate(this::gLong).limit(n);
	}
	Stream<String> STRS(int n){
		return Stream.generate(s::next).limit(n);
	}

}

class SupplyingIterator<T> implements Iterable<T>,Iterator<T>{
	int t;
	Supplier<T> supplier;

	SupplyingIterator(int t,Supplier<T> supplier){
		this.t=t;
		this.supplier=supplier;
	}

	@Override
	public Iterator<T> iterator(){
		return this;
	}

	@Override
	public boolean hasNext(){
		return t>0;
	}

	@Override
	public T next(){
		--t;
		return supplier.get();
	}

}

class Range implements Iterable<Integer>,PrimitiveIterator.OfInt{
	int to,cur,d;

	Range(int from,int to,int d){
		this.cur=from-d;
		this.to=to;
		this.d=d;
	}

	Range(int n){
		this(0,n-1,1);
	}

	@Override
	public Iterator<Integer> iterator(){
		return this;
	}

	@Override
	public boolean hasNext(){
		return (cur+d==to)||(d<0^cur+d<to);
	}

	@Override
	public int nextInt(){
		return cur+=d;
	}
	static Range rep(int i){
		return new Range(i);
	}
	static Range rep(int f,int t,int d){
		return new Range(f,t,d);
	}
	static Range rep(int f,int t){
		return rep(f,t,1);
	}
	static Range rrep(int f,int t){
		return rep(f,t,-1);
	}
}
