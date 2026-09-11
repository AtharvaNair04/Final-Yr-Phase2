//import java.io.File;
//import java.util.Scanner;
import java.util.Stack;

class Main{
  public static void main(String[] args) throws Exception{
    //Scanner scan = new Scanner(new File("in.txt"));
    //Scanner scan = new Scanner(System.in);
    MyScanner scan = new MyScanner();

    int input1 = scan.nextInt();
    Stack<Integer> st = new Stack<>();
    int stc = 0;
    int answer = 0;

    for(int incount = 0; incount < input1; incount++) {
      int input2 = scan.nextInt();
      boolean flag = true;

      while(!st.isEmpty()) {
        if(st.peek() > input2) {
          st.pop();
          answer++;
          stc--;
        }else break;
      }
      if(!st.isEmpty()) {
        if(st.peek() < input2) {
          st.push(input2);
          stc++;
        }
      }
      if(st.isEmpty()) {
        stc++;
        st.push(input2);
      }

      /*
      for(int j = 0; j < ite; j++) {
        System.out.print(arr[j]+",");
      }
      System.out.println(" answer -> "+answer);
      */
    }
    System.out.println(answer+stc);
  }
}

class MyScanner{
  StringBuilder sb;
  MyScanner() {sb = new StringBuilder();}

  int nextInt() throws Exception{
    sb.delete(0,2147483647);
    while(true) {
      int c = System.in.read();
      //System.out.println("c:"+c);
      if(c == ' ' || c == '\n' || c == '\r' || c < 16) {
        //System.out.println("return");
        return Integer.parseInt(sb.toString());
      }
      sb.appendCodePoint(c);
      //System.out.println(sb.toString());
    }
  }
}
