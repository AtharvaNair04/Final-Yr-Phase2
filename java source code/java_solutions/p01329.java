import java.util.*;

class State{
  char x, y;
  short cost;
  String path;
  char[] match;

  State(char x, char y, short cost, String path, char[] match){
    this.x = x;
    this.y = y;
    this.cost = cost;
    this.path = path;
    this.match = match;
  }

  public boolean equals(Object o){
    State st = (State)o;
    if(x != st.x || y != st.y){
      return false;
    }
    for(int i = 0; i < match.length; i++){
      if(match[i] != st.match[i]){
        return false;
      }
    }
    return true;
  }

  public int hashCode(){
    int res = x * 841841 + y * 841;
    for(int i = 0, p = 1; i < match.length; i++, p *= 10){
      res += (int)(match[i]) * p;
    }
    return res;
  }
}

public class Main{
  static int h, w, n;
  static char sx, sy, gx, gy;
  static char t[][];
  static String[] p;

  static char[] ds = {'U', 'R', 'D', 'L'};
  static int dx[] = {0, 1, 0, -1};
  static int dy[] = {-1, 0, 1, 0};

  static int bfs(){
    LinkedList<State> open = new LinkedList<State>();
    HashSet<State> closed = new HashSet<State>();

    State st = new State(sx, sy, (short)0, "", new char[n]);
    open.add(st);
    closed.add(st);

    while(!open.isEmpty()){
      st = open.poll();
      //System.out.println(st.path);

      if(st.x == gx && st.y == gy){
        return st.cost;
      }

      for(int i = 0; i < 4; i++){
        char nx = (char)(st.x + dx[i]);
        char ny = (char)(st.y + dy[i]);

        if(0 > nx || 0 > ny || nx >= w || ny >= h || t[ny][nx] == '#'){
          continue;
        }

        String npath = st.path + ds[i];

        if(npath.length() > 10){
          npath = npath.substring(1);
        }

        boolean flg = true;
        char[] nmatch = new char[n];

        for(int j = 0; j < n; j++){
          nmatch[j] = (char)0;

          for(int len = Math.min(p[j].length(), npath.length()); len > 0; len--){
            boolean matchFlg = true;

            for(int k = 0; k < len; k++){
              if(npath.charAt(npath.length() - len + k) != p[j].charAt(k)){
                matchFlg = false;
                break;
              }
            }

            if(matchFlg){
              nmatch[j] = (char)len;
              break;
            }
          }

          if(nmatch[j] == p[j].length()){
            flg = false;
            break;
          }
        }

        if(!flg){
          continue;
        }

        State nst = new State(nx, ny, (short)(st.cost + 1), npath, nmatch);

        if(!closed.contains(nst)){
          closed.add(nst);
          open.add(nst);
        }
      }
    }

    return -1;
  }

  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    while(sc.hasNextInt()){
      h = sc.nextInt();
      w = sc.nextInt();
      if(h == 0 && w == 0) break;
      t = new char[h][w];

      for(int i = 0; i < h; i++){
        t[i] = sc.next().toCharArray();

        for(int j = 0; j < w; j++){
          if(t[i][j] == 'S'){
            sx = (char)j;
            sy = (char)i;
          }
          else if(t[i][j] == 'G'){
            gx = (char)j;
            gy = (char)i;
          }
        }
      }

      n = sc.nextInt();
      p = new String[n];

      for(int i = 0; i < n; i++){
        p[i] = sc.next();
      }

      System.out.println(bfs());
    }
  }
}
