import java.io.*;
import java.util.*;

class Range {
  final int l, r;

  Range(int l, int r) {
    this.l = l;
    this.r = r;
  }
}

class Solver {
  private static final int ALPHABET_SIZE = 26;

  private static final UpdateRangeGetRangeSegmentTree.Operator<Integer, Integer> OPERATOR = new UpdateRangeGetRangeSegmentTree.Operator<>() {

    @Override
    public Integer multiplyLazyValue(Integer lazy, int count) {
      return lazy;
    }

    @Override
    public Integer applyOffset(Integer lazy, int offset) {
      return lazy;
    }

    @Override
    public Integer mergeLazyValue(Integer oldLazy, Integer newLazy) {
      if (oldLazy == null) {
        return newLazy;
      }
      if (newLazy == null) {
        return oldLazy;
      }
      return (newLazy + oldLazy) % ALPHABET_SIZE;
    }

    @Override
    public Integer apply(Integer value, Integer lazy) {
      return (value + lazy) % ALPHABET_SIZE;
    }

    @Override
    public Integer mergeValue(Integer smallIndexValue, Integer largeIndexValue) {
      return (smallIndexValue + largeIndexValue) % ALPHABET_SIZE;
    }

    @Override
    public Integer getUnitValue() {
      return Integer.valueOf(0);
    }

  };

  final char[] cs;
  final int n;
  final int pairCount;
  final int[][] lrs;

//  List<List<Range>> rangeMatrix;

  List<Set<Integer>> leftSetList;
  List<Set<Integer>> rightSetList;

  Solver(char[] cs, int pairCount, int[][] lrs) {
    this.cs = cs;
    this.n = cs.length;
    this.pairCount = pairCount;
    this.lrs = lrs;
  }

  int getMinAndUpdate(List<Set<Integer>> setList, int index) {
    List<Integer> list = new ArrayList<>();
    list.addAll(setList.get(index));
    Collections.sort(list);
    for (int i = 1; i < list.size(); i++) {
      if (list.get(i - 1) < list.get(i)) {
        setList.get(list.get(i - 1) + 1).add(list.get(i));
      }
    }
    setList.get(index).clear();
    return list.get(0);
  }

  // n = 4
  // 0, 1, 2, 3
  // 3, 2, 1, 0
  //
  // n = 5
  // 0, 1, 2, 3, 4
  // 4, 3, 2, 1, 0
  void addRange(int l, int r) {
//    System.err.printf("addRange: 1, l: %d, r: %d\n", l, r);
    if (l + r == n - 1) {
//      System.err.printf("addRange: 2, l: %d, r: %d\n", l, r);
      return;
    }
    if (r < n / 2) {
//      System.err.printf("addRange: 3, l: %d, r: %d\n", l, r);
      leftSetList.get(l).add(r);
      return;
    }
    if (l > n / 2) {
//      System.err.printf("addRange: 4, l: %d, r: %d\n", l, r);
      rightSetList.get(n - 1 - r).add(n - 1 - l);
      return;
    }
//    System.err.printf("addRange: 5, l: %d, r: %d\n", l, r);
    int rFromRight = n - 1 - r;
    if (l < rFromRight) {
//      System.err.printf("addRange: 6, l: %d, r: %d\n", l, r);
      rightSetList.get(l).add(n - 1 - (r + 1));
    } else if (rFromRight < l) {
//      System.err.printf("addRange: 7, l: %d, r: %d\n", l, r);
      leftSetList.get(rFromRight).add(l - 1);
    }
  }

  public boolean solve() {
    leftSetList = new ArrayList<>();
    rightSetList = new ArrayList<>();
    for (int i = 0; i < n / 2; i++) {
      leftSetList.add(new HashSet<>());
      rightSetList.add(new HashSet<>());
    }

    for (int[] lr : lrs) {
      int l = lr[0] - 1;
      int r = lr[1] - 1;
//      System.err.printf("%d %d\n", l, r);
      addRange(l, r);
    }

    Integer[] initialValues = new Integer[n];
    for (int i = 0; i < n; i++) {
      initialValues[i] = cs[i] - 'a';
    }
    UpdateRangeGetRangeSegmentTree<Integer, Integer> segmentTree = new UpdateRangeGetRangeSegmentTree<>(OPERATOR, initialValues);
    for (int i = 0; i < n / 2; i++) {
      int leftValue = segmentTree.get(i);
      int rightValue = segmentTree.get(n - 1 - i);
      if (!leftSetList.get(i).isEmpty() && !rightSetList.get(i).isEmpty()) {
        int leftRight = getMinAndUpdate(leftSetList, i);
        int rightLeft = getMinAndUpdate(rightSetList, i);
        if (leftValue != rightValue) {
          segmentTree.update(i, leftRight + 1, (rightValue - leftValue + ALPHABET_SIZE) % ALPHABET_SIZE);
        }
        addRange(leftRight + 1, n - rightLeft - 2);
      } else if (!leftSetList.get(i).isEmpty()) {
        int leftRight = getMinAndUpdate(leftSetList, i);
        if (leftValue != rightValue) {
          segmentTree.update(i, leftRight + 1, (rightValue - leftValue + ALPHABET_SIZE) % ALPHABET_SIZE);
        }
      } else if (!rightSetList.get(i).isEmpty()) {
        int rightLeft = getMinAndUpdate(rightSetList, i);
        if (leftValue != rightValue) {
          segmentTree.update(n - 1 - rightLeft, n - i, (leftValue - rightValue + ALPHABET_SIZE) % ALPHABET_SIZE);
        }
      } else if (leftValue != rightValue) {
        return false;
      }
    }

    return true;
  }
}

public class Main {
  private static void execute(ContestReader reader, ContestWriter out) {
    char[] cs = reader.nextCharArray();
    int n = reader.nextInt();
    int[][] lrs = reader.nextInt(n, 2);
    out.println(new Solver(cs, n, lrs).solve() ? "YES" : "NO");
  }
  
  public static void main(String[] args) {
    ContestReader reader = new ContestReader(System.in);
    ContestWriter out = new ContestWriter(System.out);
    execute(reader, out);
    out.flush();
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
