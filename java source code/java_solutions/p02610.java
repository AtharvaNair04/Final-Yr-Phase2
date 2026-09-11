import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int T = sc.nextInt();
    for (int i = 0; i < T; i++) {
      int N = sc.nextInt();
      long answer = 0;
      TreeSet<Camel> setA = new TreeSet<>();
      TreeSet<Camel> setB = new TreeSet<>();
      TreeSet<Camel> setC = new TreeSet<>();
      ArrayList<Camel> listA = new ArrayList<>();
      ArrayList<Camel> listB = new ArrayList<>();
      for(int j = 1; j <= N; j++){
        int k = sc.nextInt();
        int l = sc.nextInt();
        int r = sc.nextInt();
        Camel c = new Camel(j, k, l, r);
        if(l > r){
          listA.add(c);
        } else {
          listB.add(c);
        }
      }
      listA.sort(Comparator.comparingInt(Camel::getK));
      listB.sort(Comparator.comparingInt(Camel::getK).reversed());

      for(Camel c : listA){
        if(!setA.isEmpty() && setA.size() >= c.getK()){
          if(setA.first().getGap() < c.getGap()){
            setC.add(setA.pollFirst());
            setA.add(c);
          } else {
            setC.add(c);
          }
        } else {
          setA.add(c);
        }
      }

      for(Camel c : listB){
        if(N == c.getK()){
          setC.add(c);
        } else if(!setB.isEmpty() && setB.size() >= N - c.getK()){
          if(setB.first().getGap() < c.getGap()){
            setC.add(setB.pollFirst());
            setB.add(c);
          } else {
            setC.add(c);
          }
        } else {
          setB.add(c);
        }
      }
      for(Camel c : setA){
        answer += c.l;
      }
      for(Camel c : setB){
        answer += c.r;
      }
      for(Camel c : setC){
        answer += Math.min(c.l, c.r);
      }
      System.out.println(answer);
    }
  }

  static class Camel implements Comparable<Camel> {

    int number;
    int k, l, r;

    public int getK() {return k;}
    public int getGap() { return Math.abs(l - r);}
    public Camel(int number, int k, int l, int r) {
      this.number = number;
      this.k = k;
      this.l = l;
      this.r = r;
    }

    @Override
    public int compareTo(Camel o) {
      int ret = Integer.compare(getGap(), o.getGap());
      if(ret == 0){
        return Integer.compare(number, o.number);
      }
      return ret;
    }
  }
}
