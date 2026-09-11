public class
Main
{
  public void
  run (
    java.io.InputStream in,
    java.io.PrintStream out
    )
  {
    java.util.Scanner sc = new java.util.Scanner ( in );
    UnionFind lis;
    int n;
    int i;

    lis = new UnionFind ( sc.nextInt ( ) );
    n = sc.nextInt ( );
    for ( i = 0; i < n; ++i )
    {
      int q, x, y;

      q = sc.nextInt ( );
      x = sc.nextInt ( );
      y = sc.nextInt ( );
      if ( q == 0 )
        lis.combine ( x, y );
      else
        out.println ( lis.combinedp ( x, y ) ? 1 : 0 );
    }

    sc.close ( );
  }

  public static void
  main (
    String[ ] args
    )
  {
    ( new Main ( ) ).run ( System.in, System.out );
  }

  /** ????????¢?????????????????? */
  private static class
  UnionFind
  {
    /** Union Find Node ??????????´? */
    private int[ ] h;
    /** Union Find (Sub-)Tree ???????´???° */
    private int[ ] w;

    public
    UnionFind (
      /** 0..(size-1) ?????§?????´??°??????????????±??? */
      int size
      )
    {
      h = new int[ size ];
      w = new int[ size ];
      java.util.Arrays.fill ( h, -1 );
      java.util.Arrays.fill ( w,  1 );
    }

    /** p ??¨q ???????????¨?????? */
    public void
    combine (
      int p,
      int q
      )
    {
      p = root ( p );
      q = root ( q );
      if ( p == q ) return ;
      if ( w[ q ] < w[ p ] )
      {
        final int t = p;
        p = q;
        q = t;
      }
      h[ p ] = q;
      w[ q ] += w[ p ];
    }

    /** p ??¨q ???????????§????????????????????? */
    public boolean
    combinedp (
      int p,
      int q
      )
    {
      return ( root ( p ) == root ( q ) );
    }

    /** ??£??¨???????????? */
    private int
    root (
      int p
      )
    {
      if ( h[ p ] == -1 )
        return ( p );
      h[ p ] = root ( h[ p ] );

      return ( h[ p ] );
    }
  }
}
