import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    ArrayList<Airplane> airplanes = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      int x = sc.nextInt();
      int y = sc.nextInt();
      char u = sc.next().charAt(0);
      airplanes.add(new Airplane(x, y, u));
    }
    int mintime = Integer.MAX_VALUE;

    //X座標でソートして、上下にぶつかる飛行機をさがす
    Collections.sort(airplanes, (a1, a2) -> {
      if (a1.x != a2.x) {
        return Integer.compare(a1.x, a2.x);
      }
      return Integer.compare(a1.y, a2.y);
    });
    for(int i = 0; i < n - 1; i++){
      Airplane cur = airplanes.get(i);
      Airplane next = airplanes.get(i + 1);
      if(cur.x == next.x && cur.direction == 'U' && next.direction == 'D'){
        mintime = Math.min(mintime, (next.y - cur.y) * 5);
      }
    }

    //Y座標でソートして、左右にぶつかる飛行機をさがす
    Collections.sort(airplanes, (a1, a2) -> {
      if (a1.y != a2.y) {
        return Integer.compare(a1.y, a2.y);
      }
      return Integer.compare(a1.x, a2.x);
    });
    for(int i = 0; i < n - 1; i++){
      Airplane cur = airplanes.get(i);
      Airplane next = airplanes.get(i + 1);
      if(cur.y == next.y && cur.direction == 'R' && next.direction == 'L'){
        mintime = Math.min(mintime, (next.x - cur.x) * 5);
      }
    }

    //X＋Yの合計でソートして、90度突入でぶつかる飛行機をさがす
    Collections.sort(airplanes, (a1, a2) -> {
      if (a1.x + a1.y != a2.x + a2.y) {
        return Integer.compare(a1.x + a1.y, a2.x + a2.y);
      }
      return Integer.compare(a1.y, a2.y);
    });
    Airplane tempU = null;
    Airplane tempL = null;
    for(int i = 0; i < n; i++){
      Airplane cur = airplanes.get(i);
      if(cur.direction == 'U'){
        tempU = cur;
      } else if(cur.direction == 'L'){
        tempL = cur;
      } else if(cur.direction == 'D') {
        if(tempL != null && tempL.x + tempL.y == cur.x + cur.y){
          mintime = Math.min(mintime, (cur.y - tempL.y) * 10);
        }
      } else if(cur.direction == 'R') {
        if(tempU != null && tempU.x + tempU.y == cur.x + cur.y){
          mintime = Math.min(mintime, (cur.y - tempU.y) * 10);
        }
      }
    }

    //X-Yの合計でソートして、90度突入でぶつかる飛行機をさがす
    Collections.sort(airplanes, (a1, a2) -> {
      if (a1.x - a1.y != a2.x - a2.y) {
        return Integer.compare(a1.x - a1.y, a2.x - a2.y);
      }
      return Integer.compare(a1.x, a2.x);
    });
    Airplane tempR = null;
    tempU = null;
    for(int i = 0; i < n; i++){
      Airplane cur = airplanes.get(i);
      if(cur.direction == 'U'){
        tempU = cur;
      } else if(cur.direction == 'R'){
        tempR = cur;
      } else if(cur.direction == 'D') {
        if(tempR != null && tempR.x - tempR.y == cur.x - cur.y){
          mintime = Math.min(mintime, (cur.x - tempR.x) * 10);
        }
      } else if(cur.direction == 'L') {
        if(tempU != null && tempU.x - tempU.y == cur.x - cur.y){
          mintime = Math.min(mintime, (cur.x - tempU.x) * 10);
        }
      }
    }

    System.out.println(mintime == Integer.MAX_VALUE ? "SAFE" : mintime);
  }

  static class Airplane {
    public Airplane(int x, int y, char direction) {
      this.x = x;
      this.y = y;
      this.direction = direction;
    }

    int x, y;
    char direction;
  }
}
