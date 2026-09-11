import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    resolve(System.in, System.out);
  }

  static void resolve(InputStream is, PrintStream pw) {
    Scanner sc = new Scanner(is);
    int n = Integer.parseInt(sc.next());
    int x = Integer.parseInt(sc.next());
    int y = Integer.parseInt(sc.next());
    Map<Integer, Node> nodes = new HashMap<>();
    for (int i = 1; i <= n; i++) {
      Node node = new Node(i, n);
      nodes.put(i, node);
    }
    for (Node base : nodes.values()) {
      for (Node target : nodes.values()) {
        base.calculate(target, x, y);
      }
    }

    Map<Integer, Long> answer = nodes.values().stream()
      .flatMapToInt(v -> Arrays.stream(v.idToDistanceArray))
      .mapToObj(Integer::valueOf)
      .collect(Collectors.groupingBy(
        i -> i,
        Collectors.counting()
      ));
    for (int i = 1; i < n; i++) {
      pw.println(answer.getOrDefault(i, 0L) / 2);
    }
  }

  static class Node {

    private int id;

    private int[] idToDistanceArray;

    public Node(int id, int numOfNode) {
      this.id = id;
      this.idToDistanceArray = new int[numOfNode];
    }

    void calculate(Node target, int x, int y) {
      int targetId = target.id;
      idToDistanceArray[targetId - 1] = Math.min(Math.min(
        Math.abs(id - target.id),
        Math.abs(x - id) + 1 + Math.abs(y - targetId)),
        Math.abs(y - id) + 1 + Math.abs(x - targetId)
      );
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Node node = (Node) o;
      return id == node.id;
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }

    @Override
    public String toString() {
      return "Node{" +
        "id=" + id +
        '}';
    }
  }
}
