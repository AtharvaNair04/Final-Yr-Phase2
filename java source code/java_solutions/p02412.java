import java.util.*;
import java.io.*;
import static java.lang.System.*;

public class Main {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(true)
    {
      int n = sc.nextInt();
      int x = sc.nextInt();
      if(n == 0 && x == 0) break;
      int count=0;
      for(int i=1; i<=n; i++)
      {
        for(int j=i+1; j<=n; j++)
        {
          for(int k=j+1; k<=n; k++)
          {
            if(i+j+k == x) count++;
          }
        }
      }
      out.println(count);
    }
  }
}
