import java.util.*;
import java.util.Map.*;
import java.util.function.*;

class LazySegmentTree<V,A>{
	/**
	 * a+0=a
	 * a+(b+c)=(a+b)+c
	 */
	public static class Monoid<T,R> implements BiFunction<T,T,R>{
		T identity;

		BiFunction<T,T,R> f;
		public Monoid(T identity,BiFunction<T,T,R> f){
			this.identity=identity;
			this.f=f;
		}
		@Override
		public R apply(T t,T u){
			return f.apply(t,u);
		}
	}

	private final V[] array;
	private final A[] lazy;
	final int size;
	private final int highestbit,len[];

	/**
	 * (v1,v2)->v
	 */
	private final Monoid<V,V> merger;
	/**
	 * (v,lazy)->v
	 */
	private final BiFunction<V,A,V> applyer;
	/**
	 * (apply,apply)->apply
	 */
	private final Monoid<A,A> unifyer;
	/**
	 * (v,length)->v
	 */
	private final BiFunction<A,Integer,A> multiplier;

	@SuppressWarnings("unchecked")
	LazySegmentTree(int size,
			Monoid<V,V> merger,
			BiFunction<V,A,V> applyer,
			Monoid<A,A> unifyer,
			BiFunction<A,Integer,A> multiplier){
		this.size=size;
		this.highestbit=Integer.highestOneBit(size);
		this.merger=merger;
		this.applyer=applyer;
		this.unifyer=unifyer;
		this.multiplier=multiplier;

		this.array=(V[])new Object[size*2];
		this.lazy=(A[])new Object[size*2];
		this.len=new int[size*2];

		Arrays.fill(array,merger.identity);
		Arrays.fill(lazy,unifyer.identity);
		for(int i=len.length-1;i>=0;--i)
			len[i]=i>=size?1:len[i<<1]+len[i<<1|1];
	}

	private void set(int i,A laz){
		array[i]=applyer.apply(array[i],
				multiplier.apply(laz,len[i]));
		if(i<size)
			lazy[i]=unifyer.apply(lazy[i],laz);
	}

	/**
	 * [l, r)
	 */
	void apply(int l,int r,A value){
		int l0=l+=size,r0=r+=size;
		for(;l<r;l>>=1,r>>=1){
			if((l&1)>0) set(l++,value);
			if((r&1)>0) set(--r,value);
		}
		updateParents(l0);
		updateParents(r0-1);
	}
	private void updateParents(int p){
		while(p>1){
			p>>=1;
			array[p]=applyer.apply(
					merger.apply(array[p<<1],array[p<<1|1]),
					multiplier.apply(lazy[p],len[p]));
		}
	}

	/**
	 * [l, r)
	 */
	V query(int l,int r){
		V res=merger.identity;
		spreadLazy(l+=size);
		spreadLazy((r+=size)-1);
		for(;l<r;l>>=1,r>>=1){
			if((l&1)>0) res=merger.apply(res,array[l++]);
			if((r&1)>0) res=merger.apply(res,array[--r]);
		}
		return res;
	}
	private void spreadLazy(int p){
		for(int s=highestbit;s>0;s>>=1){
			int i=p/s;
			if(!unifyer.identity.equals(lazy[i])){
				set(i<<1,lazy[i]);
				set(i<<1|1,lazy[i]);
				lazy[i]=unifyer.identity;
			}
		}
	}
}

public class Main{
	public static void main(String[] $){
		final Scanner s=new Scanner(System.in);
		final int n=s.nextInt();
		final long d=s.nextInt();
		final int a=s.nextInt();
		int[] x=new int[n];

		LazySegmentTree<Long,Long> seg=new LazySegmentTree<>(
				n,
				new LazySegmentTree.Monoid<>(0L,Long::sum),
				Long::sum,
				new LazySegmentTree.Monoid<>(0L,Long::sum),
				(o,l)->o*l);

		TreeMap<Integer,Integer> m=new TreeMap<>();
		{
			for(int i=0;i<n;++i)
				m.put(s.nextInt(),s.nextInt());
			int i=0;
			for(Entry<Integer,Integer> e:m.entrySet()){
				x[i]=e.getKey();
				seg.apply(i,i+1,e.getValue().longValue());
				e.setValue(i++);
			}
		}

		long r=0;
		for(int i=0;i<n;++i){
			long right=x[i]+d*2;
			int j=m.floorEntry(right>x[n-1]?x[n-1]:(int)right).getValue();
			long v=seg.query(i,i+1);
			if(v>0){
				long t=(v+a-1)/a;
				r+=t;
				seg.apply(i,j+1,-t*a);
			}
		}
		System.out.println(r);
	}
}
