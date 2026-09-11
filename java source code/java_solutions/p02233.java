public class Main
{
  int[ ] f;

  public int
  fibonacci (
    int n
    )
  {
    if ( n == 0 ) return ( 1 );
    if ( n == 1 ) return ( 1 );
    if ( f[ n ] == -1 )
    {
      f[ n ] = fibonacci ( n - 1 ) + fibonacci ( n - 2 );
    }

    return ( f[ n ] );
  }

  public void
  run (
    )
  {
    java.util.Scanner sc = new java.util.Scanner ( System.in );
    int n;
    int i;

    n = sc.nextInt ( );
    f = new int[ n + 1 ];
    for ( i = 0; i < f.length; ++i )
      f[ i ] = -1;

    System.out.println ( fibonacci ( n ) );

    return ;
  }

  public static void main ( String[ ] args )
  {
    ( new Main ( ) ).run ( );

    return ;
  }

}
