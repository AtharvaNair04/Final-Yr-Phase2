import java.io.*;
import java.util.*;

class Query {
  final int type, l, r;
  final AffineOperator affineOperator;

  Query(int type, int l, int r, AffineOperator affineOperator) {
    this.type = type;
    this.l = l;
    this.r = r;
    this.affineOperator = affineOperator;
  }
}

class AffineOperator {
  final long b, c;

  AffineOperator(long b, long c) {
    this.b = b;
    this.c = c;
  }
}

public class Main {
  private static final ModCalculator mc = new ModCalculator(998244353L);

  private static final Long ZERO = Long.valueOf(0L);

  private static final AffineOperator NO_OP = new AffineOperator(1, 0);

  /*
  private static final UpdateRangeGetRangeSegmentTree.Operator<AffineOperator, Long> OPERATOR = new UpdateRangeGetRangeSegmentTree.Operator<>() {

    @Override
    public AffineOperator multiplyLazyValue(AffineOperator lazy, int count) {
      return new AffineOperator(lazy.b, mc.mul(lazy.c, count));
    }

    @Override
    public AffineOperator applyOffset(AffineOperator lazy, int offset) {
      return lazy;
    }

    @Override
    public AffineOperator mergeLazyValue(AffineOperator oldLazy, AffineOperator newLazy) {
      if (oldLazy == null) {
        return newLazy;
      }
      // v -> oldLazy.b * v + lodLazy.c
      // oldLazy.b * v + lodLazy.c -> (oldLazy.b * v + oldLazy.c) * newLazy.b + newLazy.c
      // -> oldLazy.b * newLazy.b * v + (oldLazy.c * newLazy.b) + newLazy.c

      long b = mc.mul(oldLazy.b, newLazy.b);
      long c = mc.add(oldLazy.c * newLazy.b, newLazy.c);
      return new AffineOperator(b, c);
    }

    @Override
    public Long apply(Long value, AffineOperator lazy) {
      return mc.add(value * lazy.b, lazy.c);
    }

    @Override
    public Long mergeValue(Long smallIndexValue, Long largeIndexValue) {
      return mc.add(smallIndexValue, largeIndexValue);
    }

    @Override
    public Long getUnitValue() {
      return ZERO;
    }
  };
  */

  private static final LazySegmentTree.Operator<AffineOperator> OPERATOR = new LazySegmentTree.Operator<>() {
    @Override
    public long e() {
      return 0L;
    }

    @Override
    public AffineOperator id() {
      return NO_OP;
    }

    @Override
    public long mapping(AffineOperator lazyValue, long value, int count) {
      return mc.add(lazyValue.b * value, lazyValue.c * count);
    }

    @Override
    public AffineOperator composition(AffineOperator newLazy, AffineOperator oldLazy) {
      long b = mc.mul(oldLazy.b, newLazy.b);
      long c = mc.add(oldLazy.c * newLazy.b, newLazy.c);
      return new AffineOperator(b, c);
    }

    @Override
    public long op(long smallIndexValue, long largeIndexValue) {
      return mc.add(smallIndexValue, largeIndexValue);
    }

  };

  private static List<Long> solve(int n, int q, int[] as, Query[] queries) {
    long[] initialValues = new long[n];
    for (int i = 0; i < n; i++) {
      initialValues[i] = Long.valueOf(as[i]);
    }
    LazySegmentTree<AffineOperator> segmentTree = new LazySegmentTree<>(OPERATOR, initialValues);
    List<Long> answers = new ArrayList<>();
    for (Query query : queries) {
      if (query.type == 0) {
        segmentTree.apply(query.l, query.r, query.affineOperator);
      } else {
        answers.add(segmentTree.prod(query.l, query.r));
      }
    }
    return answers;
  }

  private static void execute(ContestReader reader, ContestWriter out) {
    int n = reader.nextInt();
    int q = reader.nextInt();
    int[] as = reader.nextInt(n);
    Query[] queries = new Query[q];
    for (int i = 0; i < q; i++) {
      int type = reader.nextInt();
      int l = reader.nextInt();
      int r = reader.nextInt();
      AffineOperator affineOperator = null;
      if (type == 0) {
        int b = reader.nextInt();
        int c = reader.nextInt();
        affineOperator = new AffineOperator(b, c);
      }
      queries[i] = new Query(type, l, r, affineOperator);
    }
    out.printList(solve(n, q, as, queries));
  }
  
  public static void main(String[] args) {
    ContestReader reader = new ContestReader(System.in);
    ContestWriter out = new ContestWriter(System.out);
    execute(reader, out);
    out.flush();
  }
}

class ModCalculator {
  private final long mod;
  private final ModInverseCache modInverseCache;
  private final ModCombinationCache modCombinationCache;
  
  ModCalculator(long mod) {
    this.mod = mod;
    this.modInverseCache = new ModInverseCache();
    this.modCombinationCache = new ModCombinationCache();
  }
  
  public long norm(long v) {
    long nogmalized = v % mod;
    if (nogmalized < 0) {
      nogmalized += mod;
    }
    return nogmalized;
  }
  
  public long add(long a, long b) {
    return norm(a + b);
  }
  
  public long sub(long a, long b) {
    return norm(a - b + mod);
  }
  
  public long mul(long a, long b) {
    return norm(a * b);
  }
  
  public long pow(long a, long b) {
    if (b == 0) {
      return 1;
    }
    long v = pow(mul(a, a), b / 2);
    if (b % 2 == 1) {
      return mul(v, a);
    } else {
      return v;
    }
  }
  
  public long inverse(long a) {
    return pow(a, mod - 2);
  }
  
  public long div(long a, long b) {
    return mul(a, inverse(b));
  }

  // Verify ARC 042 D
  // https://atcoder.jp/contests/arc042/tasks/arc042_d
  // a^x mod p === b
  // return -1 there is no such positive x
  public long log(long a, long b) {
    Map<Long, Long> map = new HashMap<>();
    long powA = 1;
    long rootP = 0;
    while (true) {
      if (powA == b && rootP != 0) {
        return rootP;
      }
      if (map.containsKey(powA)) {
        return -1;
      }
      map.put(powA, rootP);
      powA = mul(powA, a);
      rootP++;
      if (rootP * rootP > mod) {
        break;
      }
    }
    long inversePowA = inverse(powA);
    for (int i = 1; i <= rootP; i++) {
      b = mul(b, inversePowA);
      Long value = map.get(b);
      if (value != null && value + rootP * i > 0) {
        return value + rootP * i;
      }
    }
    return -1;
  }
  
  public long getF(int n) {
    return modCombinationCache.getF(n);
  }
  
  public long getP(int n, int r) {
    return modCombinationCache.getP(n, r);
  }
  
  public long getC(int n, int k) {
    return modCombinationCache.getC(n, k);
  }

  // Verify ttpc2019 J
  // https://atcoder.jp/contests/ttpc2019/tasks/ttpc2019_j
  class PrimitiveLongList {
    long[] values;
    int size;

    public PrimitiveLongList() {
      values = new long[10];
    }

    private void resize() {
      long[] newValues = new long[values.length * 2];
      System.arraycopy(values, 0, newValues, 0, values.length);
      values = newValues;
    }

    public void add(long value) {
      if (size >= values.length) {
        resize();
      }
      values[size] = value;
      size++;
    }

    private void validateIndex(int index) {
      if (index < 0 || size <= index) {
        throw new IndexOutOfBoundsException(String.format("size: %d, index: %d", size, index));
      }
    }

    public long get(int index) {
      validateIndex(index);
      return values[index];
    }

    public void set(int index, long value) {
      validateIndex(index);
      values[index] = value;
    }

    public int size() {
      return size;
    }
  }

  // Verify AGC 040 C
  // https://atcoder.jp/contests/agc040/tasks/agc040_c
  class ModInverseCache {
    private final PrimitiveLongList inverseCache;

    public ModInverseCache() {
      inverseCache = new PrimitiveLongList();
      inverseCache.add(0L);
      inverseCache.add(1L);
    }

    private void resize(int n) {
      for (int i = inverseCache.size(); i <= n; i++) {
        long k = mod / i;
        int r = (int)(mod % i);
        long inverse = mul(-k, inverseCache.get(r));
        inverseCache.add(inverse);
      }
    }

    long get(int n) {
      resize(n);
      return inverseCache.get(n);
    }
  }
  
  class ModCombinationCache {
    private final PrimitiveLongList factorialCache;
    private final PrimitiveLongList factorialInverseCache;
    
    public ModCombinationCache() {
      factorialCache = new PrimitiveLongList();
      factorialCache.add(1L);
      factorialInverseCache = new PrimitiveLongList();
      factorialInverseCache.add(1L);
    }
    
    private void resize(int n) {
      for (int i = factorialCache.size() - 1; i < n; i++) {
        factorialCache.add(mul(factorialCache.get(i), i + 1));
        factorialInverseCache.add(mul(factorialInverseCache.get(i), modInverseCache.get(i + 1)));
      }
    }
    
    long getF(int n) {
      resize(n);
      return factorialCache.get(n);
    }
    
    long getP(int n, int r) {
      resize(n);
      return mul(factorialCache.get(n), factorialInverseCache.get(n - r));
    }
    
    long getC(int n, int k) {
      resize(n);
      return mul(factorialCache.get(n), mul(factorialInverseCache.get(k), factorialInverseCache.get(n-k)));
    }
  }
}

// Verify ABC177 F
// https://atcoder.jp/contests/abc177/tasks/abc177_f

class UpdateRangeGetRangeSegmentTree<LazyType, ValueType> {
  public interface Operator<LazyType, ValueType> {
    LazyType multiplyLazyValue(LazyType lazy, int count);
    LazyType applyOffset(LazyType lazy, int offset);
    LazyType mergeLazyValue(LazyType oldLazy, LazyType newLazy);
    ValueType apply(ValueType value, LazyType lazy);
    ValueType mergeValue(ValueType smallIndexValue, ValueType largeIndexValue);
    ValueType getUnitValue();
  }
  
  private final Operator<LazyType, ValueType> operator;
  private final int n;
  private final LazyType[] lazies;
  private final ValueType[] values;
  
  public UpdateRangeGetRangeSegmentTree(Operator<LazyType, ValueType> operator, ValueType[] initialValues) {
    this.operator = operator;
    
    int tempN = 1;
    while (tempN < initialValues.length) {
      tempN *= 2;
    }
    n = tempN;
    lazies = createTArray(2 * n -1);
    values = createSArray(2 * n -1);
    for (int i = 0; i < initialValues.length; i++) {
      values[i + n - 1] = initialValues[i];
    }
    for (int i = initialValues.length; i < n; i++) {
      values[i + n - 1] = this.operator.getUnitValue();
    }
    for (int i = n - 2; i >= 0; i--) {
      values[i] = this.operator.mergeValue(values[2 * i + 1], values[2 * i + 2]);
    }
  }

  @SuppressWarnings("unchecked")
  private LazyType[] createTArray(int size) {
    return (LazyType[])(new Object[size]);    
  }
  
  @SuppressWarnings("unchecked")
  private ValueType[] createSArray(int size) {
    return (ValueType[])(new Object[size]);    
  }
  
  private void eval(int k, int l, int r) {
    if (lazies[k] == null) {
      return;
    }
    values[k] = operator.apply(values[k], operator.multiplyLazyValue(lazies[k], r - l));
    if (r - l > 1) {
      lazies[2 * k + 1] = operator.mergeLazyValue(lazies[2 * k + 1], lazies[k]);
      lazies[2 * k + 2] = operator.mergeLazyValue(lazies[2 * k + 2], operator.applyOffset(lazies[k], (r - l) / 2));
    }
    lazies[k] = null;
  }
  
  private void update(int a, int b, LazyType lazy, int k, int l, int r) {
    eval(k, l, r);
    if (b <= l || r <= a) {
      return;
    }
    if (a <= l && r <= b) {
      lazies[k] = operator.mergeLazyValue(lazies[k], lazy);
      eval(k, l, r);
    } else {
      update(a, b, lazy, 2 * k + 1, l, (l + r) / 2);
      update(a, b, operator.applyOffset(lazy, (r - l) / 2), 2 * k + 2, (l + r) / 2, r);
      values[k] = operator.mergeValue(values[2 * k + 1], values[2 * k + 2]);
    }
  }
  
  public void update(int a, int b, LazyType lazy) {
    update(a, b, lazy, 0, 0, n);
  }
  
  public void update(int a, LazyType lazy) {
    update(a, a + 1, lazy);
  }
  
  private ValueType getRange(int a, int b, int k, int l, int r) {
    if (b <= l || r <= a) {
      return operator.getUnitValue();
    }
    eval(k, l, r);
    if (a <= l && r <= b) {
      return values[k];
    }
    ValueType vl = getRange(a, b, 2 * k + 1, l, (l + r) / 2);
    ValueType vr = getRange(a, b, 2 * k + 2, (l + r) / 2, r);
    return operator.mergeValue(vl, vr);
  }
  
  public ValueType getRange(int a, int b) {
    return getRange(a, b, 0, 0, n);
  }
  
  public ValueType get(int a) {
    return getRange(a, a + 1);
  }

  private void dump(Object[] objects) {
    int sum = 0;
    for (int i = 0; sum + (1 << i) <= 2 * n - 1; sum += 1 << i, i++) {
      for (int j = sum; j < sum + (1 << i); j++) {
        System.err.print(objects[j]);
        System.err.print(" ");
      }
      System.err.println();
    }
    System.err.println();
  }
  
  public void dump() {
    System.err.println("values: ");
    dump(values);
    System.err.println("lazies: ");
    dump(lazies);
  }
}

class ContestWriter extends PrintWriter {
  ContestWriter(PrintStream printStream) {
    super(printStream);
  }

  public void printList(List<? extends Object> list) {
    for (Object object : list) {
      println(object);
    }
  }

  public void printListOneLine(List<? extends Object> list) {
    List<String> stringList = new ArrayList<>();
    for (Object object : list) {
      stringList.add(object.toString());
    }
    println(String.join(" ", stringList));
  }
}

class LazySegmentTree<F> {
  public interface Operator<F> {
    long e();
    F id();
    long mapping(F lazyValue, long value, int count);
    F composition(F newLazy, F oldLazy);
    long op(long smallIndexValue, long largeIndexValue);
  }

  private final Operator<F> operator;
  private final int n, log, size;
  private final long[] d;
  private final F[] lz;

  private static int ceil_pow2(int n) {
    int x = 0;
    while ((1 << x) < n) {
      x++;
    }
    return x;
  }

  public LazySegmentTree(Operator<F> operator, long[] v) {
    this.operator = operator;
    this.n = v.length;
    this.log = ceil_pow2(n);
    this.size = 1 << log;
    this.d = new long[2 * size];
    Arrays.fill(this.d, this.operator.e());
    this.lz = (F[])new Object[size];
    Arrays.fill(this.lz, this.operator.id());
    for (int i = 0; i < n; i++) {
      this.d[size + i] = v[i];
    }
    for (int i = size - 1; i >= 1; i--) {
      update(i);
    }
  }

  public void set(int p, long x) {
//    assert(0 <= p && p < n);
    p += size;
    for (int i = log; i >= 1; i--) {
      push(p >> i);
    }
    d[p] = x;
    for (int i = 1; i <= log; i++) {
      update(p >> i);
    }
  }

  public long get(int p) {
//    assert(0 <= p && p < n);
    p += size;
    for (int i = log; i >= 1; i--) {
      push(p >> i);
    }
    return d[p];
  }

  public long prod(int l, int r) {
//    assert(0 <= l && l <= r && r <= n);
    if (l == r) {
      operator.e();
    }
    l += size;
    r += size;
    for (int i = log; i >= 1; i--) {
      if (((l >> i) << i) != l) {
        push(l >> i);
      }
      if (((r >> i) << i) != r) {
        push(r >> i);
      }
    }
    long lValue = operator.e();
    long rValue = operator.e();
    while (l < r) {
      if ((l & 1) == 1) {
        lValue = operator.op(lValue, d[l]);
        l++;
      }
      if ((r & 1) == 1) {
        r--;
        rValue = operator.op(d[r], rValue);
      }
      l >>= 1;
      r >>= 1;
    }
    return operator.op(lValue, rValue);
  }

  public long allProd() {
    return d[1];
  }

  public void apply(int p, F f) {
//    assert(0 <= p && p < n);
    p += size;
    for (int i = log; i >= 1; i--) {
      push(p >> i);
    }
    d[p] = operator.mapping(f, d[p], 1);
    for (int i = 1; i <= log; i++) {
      update(p >> i);
    }
  }

  public void apply(int l, int r, F f) {
//    assert(0 <= l && l <= r && r <= n);
    if (l == r) {
      return;
    }

    l += size;
    r += size;

    for (int i = log; i >= 1; i--) {
      if (((l >> i) << i) != l) {
        push(l >> i);
      }
      if (((r >> i) << i) != r) {
        push((r - 1) >> i);
      }
    }

    {
      int l2 = l;
      int r2 = r;
      while (l < r) {
        if ((l & 1) == 1) {
          allApply(l, f);
          l++;
        }
        if ((r & 1) == 1) {
          r--;
          allApply(r, f);
        }
        l >>= 1;
        r >>= 1;
      }
      l = l2;
      r = r2;
    }

    for (int i = 1; i <= log; i++) {
      if (((l >> i) << i) != l) {
        update(l >> i);
      }
      if (((r >> i) << i) != r) {
        update((r - 1) >> i);
      }
    }
  }

  private void update(int k) {
    d[k] = this.operator.op(d[2 * k], d[2 * k + 1]);
  }

  private void allApply(int k, F f) {
    int count = 1 << (Integer.numberOfLeadingZeros(k) - 31 + log);
//    System.err.printf("k: %d, count: %d\n", k, count);
    d[k] = this.operator.mapping(f, d[k], count);
    if (k < size) {
      lz[k] = this.operator.composition(f, lz[k]);
    }
  }

  private void push(int k) {
    allApply(2 * k, lz[k]);
    allApply(2 * k + 1, lz[k]);
    lz[k] = this.operator.id();
  }
}

class ContestReader {
  private static final int BUFFER_SIZE = 1024;
  
  private final InputStream stream;
  private final byte[] buffer;
  private int pointer;
  private int bufferLength;
  
  ContestReader(InputStream stream) {
    this.stream = stream;
    this.buffer = new byte[BUFFER_SIZE];
    this.pointer = 0;
    this.bufferLength = 0;
  }
  
  private boolean hasNextByte() {
    if (pointer < bufferLength) {
      return true;
    }
    
    pointer = 0;
    try {
      bufferLength = stream.read(buffer);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return bufferLength > 0;
  }
  
  private int readByte() {
    if (hasNextByte()) {
      return buffer[pointer++];
    } else {
      return -1;
    }
  }
  
  private static boolean isPrintableChar(int c) {
    return 33 <= c && c <= 126;
  }
  
  public boolean hasNext() {
    while (hasNextByte() && !isPrintableChar(buffer[pointer])) {
      pointer++;
    }
    return hasNextByte();
  }
  
  public String next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    StringBuilder sb = new StringBuilder();
    while(true) {
      int b = readByte();
      if (!isPrintableChar(b)) {
        break;
      }
      sb.appendCodePoint(b);
    }
    return sb.toString();
  }
  
  public String nextLine() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    StringBuilder sb = new StringBuilder();
    while(true) {
      int b = readByte();
      if (!isPrintableChar(b) && b != 0x20) {
        break;
      }
      sb.appendCodePoint(b);
    }
    return sb.toString();
  }
  
  public char nextChar() {
    return next().charAt(0);
  }
  
  public int nextInt() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    
    int n = 0;
    boolean minus = false;
    
    {
      int b = readByte();
      if (b == '-') {
        minus = true;
      } else if ('0' <= b && b <= '9') {
        n = b - '0';
      } else {
        throw new NumberFormatException();
      }
    }
    
    while(true){
      int b = readByte();
      if ('0' <= b && b <= '9') {
        n *= 10;
        n += b - '0';
      } else if (b == -1 || !isPrintableChar(b)) {
        return minus ? -n : n;
      } else {
        throw new NumberFormatException();
      }
    }
  }
  
  public long nextLong() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    
    long n = 0;
    boolean minus = false;
    
    {
      int b = readByte();
      if (b == '-') {
        minus = true;
      } else if ('0' <= b && b <= '9') {
        n = b - '0';
      } else {
        throw new NumberFormatException();
      }
    }
    
    while(true){
      int b = readByte();
      if ('0' <= b && b <= '9') {
        n *= 10;
        n += b - '0';
      } else if (b == -1 || !isPrintableChar(b)) {
        return minus ? -n : n;
      } else {
        throw new NumberFormatException();
      }
    }
  }
  
  public double nextDouble() {
    return Double.parseDouble(next());
  }
  
  public String[] next(int n) {
    String[] array = new String[n];
    for (int i = 0; i < n; i++) {
      array[i] = next();
    }
    return array;
  }
  
  public String[] nextLine(int n) {
    String[] array = new String[n];
    for (int i = 0; i < n; i++) {
      array[i] = nextLine();
    }
    return array;
  }
  
  public char[] nextChar(int n) {
    char[] array = new char[n];
    for (int i = 0; i < n; i++) {
      array[i] = nextChar();
    }
    return array;
  }
  
  public int[] nextInt(int n) {
    int[] array = new int[n];
    for (int i = 0; i < n; i++) {
      array[i] = nextInt();
    }
    return array;
  }
  
  public long[] nextLong(int n) {
    long[] array = new long[n];
    for (int i = 0; i < n; i++) {
      array[i] = nextLong();
    }
    return array;
  }
  
  public double[] nextDouble(int n) {
    double[] array = new double[n];
    for (int i = 0; i < n; i++) {
      array[i] = nextDouble();
    }
    return array;
  }
  
  public char[] nextCharArray() {
    return next().toCharArray();
  }
  
  public String[][] next(int n, int m) {
    String[][] matrix = new String[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrix[i][j] = next();
      }
    }
    return matrix;
  }
  
  public int[][] nextInt(int n, int m) {
    int[][] matrix = new int[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrix[i][j] = nextInt();
      }
    }
    return matrix;
  }
  
  public char[][] nextChar(int n, int m) {
    char[][] matrix = new char[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrix[i][j] = nextChar();
      }
    }
    return matrix;
  }
  
  public long[][] nextLong(int n, int m) {
    long[][] matrix = new long[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrix[i][j] = nextLong();
      }
    }
    return matrix;
  }
  
  public double[][] nextDouble(int n, int m) {
    double[][] matrix = new double[n][m];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        matrix[i][j] = nextDouble();
      }
    }
    return matrix;
  }
  
  public char[][] nextCharArray(int n) {
    char[][] matrix = new char[n][];
    for (int i = 0; i < n; i++) {
      matrix[i] = next().toCharArray();
    }
    return matrix;
  }
}

class MyAssert {
  public static void myAssert(boolean flag, String message) {
    if (!flag) {
      throw new RuntimeException(message);
    }
  }
 
  public static void myAssert(boolean flag) {
    myAssert(flag, "");
  }
}
