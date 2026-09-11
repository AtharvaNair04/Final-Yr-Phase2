public class
Main
{
  public void
  run (
    )
  {
    java.util.Scanner sc = new java.util.Scanner ( System.in );
    int[ ] d;
    int n;
    int i;
    
    n = sc.nextInt ( );
    d = new int[ n ];
    for ( i = 0; i < n; ++i )
      d[ i ] = sc.nextInt ( );
    java.util.Arrays.sort ( d );
    
    for ( i = 0; i < n; ++i )
    {
      System.out.print ( d[ i ] );
      if ( i == n - 1 )
        System.out.println ( );
      else
        System.out.print ( " " );
    }
    
    return ;
  }

  public static void
  main (
    String[ ] args
    )
  {
    ( new Main ( ) ).run ( );
    
    return ;
  }
}
