import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;


 
public class Main {
  public static void main(String[] args) {
    InputReader sc = new InputReader(System.in);
 
    String[] inpstr = new String[8];
    for(int i = 0; i < 8; i++){
      inpstr[i] = sc.nextStr();
    }

    char[][] inp = new char[8][8];
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        inp[i][j] = inpstr[i].charAt(j);
      }
    }

    char[][] ans = new char[8][8];

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        ans[i][j] = inp[7-j][i];
      }
    }
    System.out.println("90");
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        System.out.print(ans[i][j]);
      }
      System.out.println("");
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        inp[i][j] = ans[i][j];
      }
    }

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        ans[i][j] = inp[7-j][i];
      }
    }
    System.out.println("180");
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        System.out.print(ans[i][j]);
      }
      System.out.println("");
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        inp[i][j] = ans[i][j];
      }
    }

    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        ans[i][j] = inp[7-j][i];
      }
    }
    System.out.println("270");
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        System.out.print(ans[i][j]);
      }
      System.out.println("");
    }
    for(int i = 0; i < 8; i++){
      for(int j = 0; j < 8; j++){
        inp[i][j] = ans[i][j];
      }
    }


  }
 
  
 
  static class InputReader {
      private InputStream stream;
      private byte[] buf = new byte[1024];
      private int curChar;
      private int numChars;
      private SpaceCharFilter filter;

      public InputReader(InputStream stream) {
          this.stream = stream;
      }
 
      public int next() {
          if (numChars == -1)
              throw new InputMismatchException();
          if (curChar >= numChars) {
              curChar = 0;
              try {
                  numChars = stream.read(buf);
              } catch (IOException e) {
                  throw new InputMismatchException();
              }
              if (numChars <= 0)
                  return -1;
          }
          return buf[curChar++];
      }
 
      public String nextStr() {
        int c = next();
        while(isSpaceChar(c)){c = next();}
        StringBuffer str = new StringBuffer();
        do{
          str.append((char)c);
          c = next();
        }while(!isSpaceChar(c));
        return str.toString();
      }

      public int nextInt() {
          int c = next();
          while (isSpaceChar(c))
              c = next();
          int sgn = 1;
          if (c == '-') {
              sgn = -1;
              c = next();
          }
          int res = 0;
          do {
              if (c < '0' || c > '9')
                  throw new InputMismatchException();
              res *= 10;
              res += c - '0';
              c = next();
          } while (!isSpaceChar(c));
          return res * sgn;
      }
 
      public boolean isSpaceChar(int c) {
          if (filter != null)
              return filter.isSpaceChar(c);
          return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
      }
 
      public interface SpaceCharFilter {
          public boolean isSpaceChar(int ch);
      }
  }
}
