import java.util.Scanner;
 
public class Main{
    public static void main(String[] args){
        Scanner stdIn = new Scanner(System.in);
         
        int N = stdIn.nextInt();
        String[][] result = new String[N][];
        for(int i = 0; i < N; i++){
            int H = stdIn.nextInt();
            int W = stdIn.nextInt();
            String[] str = new String[H];
            for(int j = 0; j < H; j++){
                str[j] = stdIn.next();
            }
            char[][] map = new char[H][W];
            for(int j = 0; j < H; j++){
                map[j] = str[j].toCharArray();
            }
             
            int[] now = new int[2];
            for(int j = 0; j < H; j++){
                for(int k = 0; k < W; k++){
                    if( map[j][k] == '<' || map[j][k] == '>' || map[j][k] == '^' || map[j][k] == 'v' ){
                        now[0] = k;
                        now[1] = j;
                        break;
                    }
                }
            }
            int com = stdIn.nextInt();
            String s = stdIn.next();
            char[] c = s.toCharArray();
            for(int j = 0; j < com; j++){
                if( c[j] == 'U' ){
                    if( now[1] > 0 && map[now[1]-1][now[0]] == '.' ){
                        map[now[1]-1][now[0]] = '^';
                        map[now[1]][now[0]] = '.';
                        now[1]--;
                    } else {
                        map[now[1]][now[0]] = '^';
                    }
                } else if( c[j] == 'D' ){
                    if( now[1] < map.length-1 && map[now[1]+1][now[0]] == '.' ){
                        map[now[1]+1][now[0]] = 'v';
                        map[now[1]][now[0]] = '.';
                        now[1]++;
                    } else {
                        map[now[1]][now[0]] = 'v';
                    }
                } else if( c[j] == 'L' ){
                    if( now[0] > 0 && map[now[1]][now[0]-1] == '.' ){
                        map[now[1]][now[0]-1] = '<';
                        map[now[1]][now[0]] = '.';
                        now[0]--;
                    } else {
                        map[now[1]][now[0]] = '<';
                    }
                } else if( c[j] == 'R' ){
                    if( now[0] < map[now[1]].length-1 && map[now[1]][now[0]+1] == '.' ){
                        map[now[1]][now[0]+1] = '>';
                        map[now[1]][now[0]] = '.';
                        now[0]++;
                    } else {
                        map[now[1]][now[0]] = '>';
                    }
                } else if( c[j] == 'S' ){
                    if( map[now[1]][now[0]] == '>' ){
                        for(int k = now[0]+1; k < map[now[1]].length; k++){
                            if( map[now[1]][k] == '*' ){
                                map[now[1]][k] = '.';
                                break;
                            } else if( map[now[1]][k] == '#' ){
                                break;
                            }
                        }
                    } else if( map[now[1]][now[0]] == '<' ){
                        for(int k = now[0]-1; k >= 0; k--){
                            if( map[now[1]][k] == '*' ){
                                map[now[1]][k] = '.';
                                break;
                            } else if( map[now[1]][k] == '#' ){
                                break;
                            }
                        }   
                    } else if( map[now[1]][now[0]] == 'v' ){
                        for(int k = now[1]+1; k < map.length; k++){
                            if( map[k][now[0]] == '*' ){
                                map[k][now[0]] = '.';
                                break;
                            } else if( map[k][now[0]] == '#' ){
                                break;
                            }
                        }
                    } else if( map[now[1]][now[0]] == '^' ){
                        for(int k = now[1]-1; k >= 0; k--){
                            if( map[k][now[0]] == '*' ){
                                map[k][now[0]] = '.';
                                break;
                            } else if( map[k][now[0]] == '#' ){
                                break;
                            }
                        }
                    }
                }
            }
            result[i] = new String[H];
            for(int j = 0; j < H; j++){
                result[i][j] = new String(map[j]);
            }
        }
         
        for(int i = 0; i < N-1; i++){
            for(int j = 0; j < result[i].length; j++){
                System.out.println(result[i][j]);
            }
            System.out.println();
        }
        for(int i = 0; i < result[N-1].length; i++){
            System.out.println(result[N-1][i]);
        }
    }
}
