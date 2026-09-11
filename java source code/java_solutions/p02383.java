import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner stdIn = new Scanner(System.in);
    
    int top = stdIn.nextInt();
    int back = stdIn.nextInt();
    int right = stdIn.nextInt();
    int left = stdIn.nextInt();
    int front = stdIn.nextInt();
    int bottom = stdIn.nextInt();

    String str = stdIn.next();
    char[] c = str.toCharArray();

    Dice dice = new Dice(top, back, right, left, front, bottom);

    for (int i = 0; i < c.length; i++) {
      dice.roll(c[i]);
    }

    System.out.println(dice.getTop());
  }
}

class Dice {
  int top;
  int back;
  int right;
  int left;
  int front;
  int bottom;

  Dice (int top, int back, int right, int left, int front, int bottom) {
    this.top = top;
    this.back = back;
    this.right = right;
    this.left = left;
    this.front = front;
    this.bottom = bottom;
  }

  public int getTop() { return this.top; }

  public void roll(char c) {
    if (c == 'E') {
      int tmp = top;
      top = left;
      left = bottom;
      bottom = right;
      right = tmp;
    } else if (c == 'N') {
      int tmp = top;
      top = back;
      back = bottom;
      bottom = front;
      front = tmp;
    } else if (c == 'S') {
      int tmp = top;
      top = front;
      front = bottom;
      bottom = back;
      back = tmp;
    } else if (c == 'W') {
      int tmp = top;
      top = right;
      right = bottom;
      bottom = left;
      left = tmp;
    } else {
      System.out.println("roll error");
    }
  }
}
