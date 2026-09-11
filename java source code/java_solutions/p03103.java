import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int n = scanner.nextInt();
    int m = scanner.nextInt();
    Data[] datas = new Data[n];
    for (int i = 0; i < n; i++) {
      long a = scanner.nextLong();
      long b = scanner.nextLong();
      datas[i] = new Data(a, b);
    }
    Arrays.sort(datas, new Comparator<Data>() {
      @Override
      public int compare(Data d1, Data d2) {
        return (int) (d1.getPrice() - d2.getPrice());
      }
    });

    long sum = 0;
    long money = 0;

    for (int i = 0; i < n; i++) {
      if (sum + datas[i].getStock() >= m) {
        money += datas[i].getPrice() * (m - sum);
        break;
      } else {
        sum += datas[i].getStock();
        money += datas[i].getPrice() * datas[i].getStock();
      }
    }
    System.out.println(money);
  }

}

class Data {
  private long a;
  private long b;

  public Data(long a, long b) {
    this.a = a;
    this.b = b;
  }

  public long getPrice() {
    return a;
  }

  public long getStock() {
    return b;
  }
}
