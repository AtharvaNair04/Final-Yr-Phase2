import java.util.*;
class Engine implements Comparable<Engine>
{
  int X,Y;
  double atan2;
  Engine(int X,int Y)
  {
    this.X =X;
    this.Y=Y;
    atan2 = Math.atan2(Y,X);
  }
  public int compareTo(Engine x)
  {
    return Double.compare(this.atan2,x.atan2);
  }
}
class Main
{
  public static void main(String Args[])
  {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    List <Engine> engines = new ArrayList<>(n);
    for(int i = 0;i< n;i++)
    {
      int x = in.nextInt();
      int y = in.nextInt();
      if(x==0&&y==0)
        continue;
      engines.add(new Engine(x,y));
    }
    Collections.sort(engines);
    int m = engines.size();
    double max = 0.0;
    for(int i = 0;i< m;i++)
    {
      double y = 0.0;
      double x = 0.0;
      for(int j = 0;j<m;j++)
      {
        int index = (i+j)%m;
        Engine engine  = engines.get(index);
        y += engine.Y;
        x += engine.X;
        max = Math.max(max , Math.hypot(x,y));
      }
    }
    System.out.printf("%.20f",max);
  }
}
