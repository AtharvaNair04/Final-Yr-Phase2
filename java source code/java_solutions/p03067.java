import java.util.*;
import java.lang.*;
import java.io.*;

public class Main{
public static void main(String[] args){
Scanner s = new Scanner(System.in);
  int a = s.nextInt();
  int b = s.nextInt();
  int c = s.nextInt();

  
  
  if((a>c && b<c) ||(b>c && a<c)){
  System.out.print("Yes");
  }
  else{
  System.out.print("No");
  }
}
}
