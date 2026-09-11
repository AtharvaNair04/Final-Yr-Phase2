import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  private static final int A_CODE_POINT = "a".codePointAt(0);
  private static final int Z_CODE_POINT = "z".codePointAt(0);

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    String s = sc.next();
    String t = sc.next();
    Indexes[] sCharIndexes = new Indexes[Z_CODE_POINT + 1];
    for (int i = A_CODE_POINT; i <= Z_CODE_POINT; i++) {
      sCharIndexes[i] = new Indexes();
    }
    for (int i = 0; i < s.length(); i++) {
      int c = s.codePointAt(i);
      sCharIndexes[c].add(i);
    }
    CharIndexMap map = new CharIndexMap(sCharIndexes);
    boolean allContains = true;
    for (int i = 0; i < t.length(); i++) {
      allContains = allContains && map.hasCodePoint(t.codePointAt(i));
    }
    if (!allContains) {
      pw.println(-1);
      return;
    }
    long resetCount = 0;
    for (int i = 0; i < t.length(); i++) {
      int codePoint = t.codePointAt(i);
      if (!map.hasNext(codePoint)) {
        map.resetIndex();
        resetCount++;
      }
      map.next(codePoint);
    }
    pw.println((long) s.length() * resetCount + map.lastIndex + 1);
  }

  static class Indexes {

    List<Integer> indexes = new ArrayList<>();

    void add(Integer i) {
      indexes.add(i);
    }

    boolean hasIndexes() {
      return indexes.size() > 0;
    }

    int size() {
      return indexes.size();
    }

    int getIndex(int n) {
      return indexes.get(n);
    }
  }

  static class CharIndexMap {

    Indexes[] sCharIndexes;
    int[] arrayIndex;
    int lastIndex = -1;

    public CharIndexMap(Indexes[] sCharIndexes) {
      this.sCharIndexes = sCharIndexes;
      this.arrayIndex = new int[sCharIndexes.length];
      for (int i = 0; i < arrayIndex.length; i++) {
        arrayIndex[i] = 0;
      }
    }

    boolean hasCodePoint(int codePoint) {
      return sCharIndexes[codePoint].hasIndexes();
    }

    void resetIndex() {
      for (int i = 0; i < arrayIndex.length; i++) {
        arrayIndex[i] = 0;
      }
    }

    boolean hasNext(int codePoint) {
      if (!(arrayIndex[codePoint] < sCharIndexes[codePoint].size())) {
        return false;
      }
      for (int i = arrayIndex[codePoint]; i < sCharIndexes[codePoint].size(); i++) {
        if (sCharIndexes[codePoint].getIndex(i) > lastIndex) {
          arrayIndex[codePoint] = i;
          return true;
        }
      }
      return false;
    }

    void next(int codePoint) {
      int i = arrayIndex[codePoint];
      lastIndex = sCharIndexes[codePoint].getIndex(i);
      arrayIndex[codePoint] = i + 1;
    }
  }
}
