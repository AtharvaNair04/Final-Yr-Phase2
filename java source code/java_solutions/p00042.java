public class
Main
{
  private int[ ][ ] opt_p;
  private int[ ][ ] opt_w;
  private int[ ] p;
  private int[ ] w;
  private int n;
  private int c;
  
  private void
  dp (
    )
  {
    int i, j;

    opt_p = new int[ n + 1 ][ c + 1 ];
    opt_w = new int[ n + 1 ][ c + 1 ];
    
    /* (1) 基底 */
    for ( i = 0; i <= c; ++i )
    {
      opt_p[ 0 ][ i ] = 0;
      opt_w[ 0 ][ i ] = 0;
    }
    
    /* (2) 再帰逆行 */
    for ( i = 1; i <= n; ++i )
    for ( j = 0; j <= c; ++j )
    {
      opt_p[ i ][ j ] = opt_p[ i - 1 ][ j ];
      opt_w[ i ][ j ] = opt_w[ i - 1 ][ j ];
      if ( w[ i - 1 ] <= j )
      {
        final int tmp_p = opt_p[ i - 1 ][ j - w[ i - 1 ] ] + p[ i - 1 ];
        final int tmp_w = opt_w[ i - 1 ][ j - w[ i - 1 ] ] + w[ i - 1 ];
        if ( tmp_p > opt_p[ i ][ j ]
          || ( tmp_p == opt_p[ i ][ j ] && tmp_w < opt_w[ i ][ j ] ) )
        {
          opt_p[ i ][ j ] = tmp_p;
          opt_w[ i ][ j ] = tmp_w;
        }
      }
    }
  }
  
  public void
  run (
    )
  {
    java.util.Scanner sc = new java.util.Scanner ( System.in );
    int no = 0;
    int i;
    
    for ( ; ; )
    {
      c = sc.nextInt ( );
      if ( c == 0 ) break ;
      n = sc.nextInt ( );
      w = new int[ n ];
      p = new int[ n ];
      for ( i = 0; i < n; ++i )
      {
        String[ ] s = sc.next ( ).split ( "," );
        p[ i ] = Integer.parseInt ( s[ 0 ] );
        w[ i ] = Integer.parseInt ( s[ 1 ] );
      }
      
      dp ( );
      System.out.println ( "Case " + ( ++no ) + ":" );
      System.out.println ( opt_p[ n ][ c ] );
      System.out.println ( opt_w[ n ][ c ] );
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
