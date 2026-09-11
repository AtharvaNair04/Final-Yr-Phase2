import java.util.*;
import java.io.*;
import static java.lang.System.*;
import static java.lang.Math.*;

public class Main {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(true)
    {
      int x = sc.nextInt();
      int y = sc.nextInt();

      if(x == 0 && y == 0) break;

      for(int i=0; i<x; i++){
        for(int j=0; j<y; j++){
          if(i % 2 == 0){
            out.printf(j%2 == 0 ? "#" : ".");
          }else{
            out.printf(j%2 == 0 ? "." : "#");
          }
        }
        out.println();
      }

      out.println();
    }
  }
}
