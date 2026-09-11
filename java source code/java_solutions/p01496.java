import java.util.*;

class Main{

    int h,w;
    char[][] sheet;
    ArrayList<Dice> list;
    ArrayList<Character> color;
    int[] colors;

    int[] dx = {0, 1, 0, -1};
    int[] dy = {-1, 0, 1, 0};

    int[][] nei = {{1, 3, 2, 4},
                   {5, 3, 0, 4},
                   {0, 3, 5, 4},
                   {1, 5, 2, 0},
                   {1, 0, 2, 5},
                   {1, 4, 2, 3}};

    class Dice{
        char[] dice = new char[6]; 
        Dice(char f, char t, char b, char r, char l, char u){
            dice[0] = f;
            dice[1] = t;
            dice[2] = b;
            dice[3] = r;
            dice[4] = l;
            dice[5] = u;
        }
    }

    Dice rotateRight(Dice d){
        return new Dice(d.dice[0], d.dice[4], d.dice[3], d.dice[1], d.dice[2], d.dice[5]);
    }

    Dice rotateLeft(Dice d){
        return new Dice(d.dice[0], d.dice[3], d.dice[4], d.dice[2], d.dice[1], d.dice[5]);
    }

    Dice rotateDown(Dice d){
        return new Dice(d.dice[1], d.dice[5], d.dice[0], d.dice[3], d.dice[4], d.dice[2]);
    }

    Dice rotateUp(Dice d){
        return new Dice(d.dice[2], d.dice[0], d.dice[5], d.dice[3], d.dice[4], d.dice[1]);
    }
    
    boolean check(Dice d){
        if(d.dice[1]!='#' && d.dice[3]!='#') return true;
        if(d.dice[3]!='#' && d.dice[2]!='#') return true;
        if(d.dice[2]!='#' && d.dice[4]!='#') return true;
        if(d.dice[4]!='#' && d.dice[1]!='#') return true;
        return false;
    }

    Dice rotate(int k, Dice d){
        if(k==0) return d;
        if(k==1) return rotateDown(d);
        if(k==2) return rotateUp(d);
        if(k==3) return rotateDown(rotateLeft(d));
        if(k==4) return rotateDown(rotateRight(d));
        if(k==5) return rotateDown(rotateDown(d));
        return d;
    }

    void solve(){
        Scanner sc = new Scanner(System.in);

        h = sc.nextInt();
        w = sc.nextInt();
        sheet = new char[h][w];
        color = new ArrayList<Character>();
        for(int i=0; i<h; i++){
            String s = sc.next();
            for(int j=0; j<w; j++){
                sheet[i][j] = s.charAt(j);
                if(sheet[i][j]!='#' && sheet[i][j]!='.' && !color.contains(sheet[i][j])) 
                    color.add(sheet[i][j]);
            }
        }

        list = new ArrayList<Dice>();
        colors = new int[color.size()];

        for(int i=0; i<h; i++){
            for(int j=0; j<w; j++){
                if(sheet[i][j]!='.'){
                    bfs(i, j);
                }
            }
        }

        ArrayList<Dice> list2 = new ArrayList<Dice>();

        for(int i=0; i<colors.length; i++){
            if(colors[i]<4) continue;
            ArrayList<Dice> same = new ArrayList<Dice>();
            for(int j=0; j<list.size(); j++){
                Dice dd  = list.get(j);
                for(int k=0; k<6; k++){
                    if(dd.dice[k]==color.get(i)){
                        Dice newDice = rotate(k, dd); //kを前面に
                        same.add(newDice);
                        break;
                    }
                }
            }
            if(same.size()<4) continue;

            for(int j=0; j<same.size(); j++){
                Dice d1 = same.get(j);
                if(!check(d1)) continue;
                while(d1.dice[1]=='#' || d1.dice[4]=='#') d1 = rotateRight(d1);
                for(int k=0; k<same.size(); k++){
                    Dice d2 = same.get(k);
                    if(!check(d2)) continue;
                    while(d2.dice[1]=='#' || d2.dice[3]=='#') d2 = rotateRight(d2); 
                    for(int l=0; l<same.size(); l++){
                        Dice d3 = same.get(l);
                        if(!check(d3)) continue;
                        while(d3.dice[4]=='#' || d3.dice[2]=='#') d3 = rotateRight(d3);
                        for(int m=0; m<same.size(); m++){
                            Dice d4 = same.get(m);
                            if(!check(d4)) continue;
                            while(d4.dice[3]=='#' || d4.dice[2]=='#') d4 = rotateRight(d4);

                            if(d1.dice[1]==d2.dice[1] && d2.dice[3]==d4.dice[3] &&
                               d3.dice[2]==d4.dice[2] && d1.dice[4]==d3.dice[4] && 
                               d1.dice[1]!=d3.dice[2] && d1.dice[4]!=d2.dice[3]){
                                list2.add(new Dice((char)color.get(i), d1.dice[1], d3.dice[2], d2.dice[3], d1.dice[4], '.'));
                            }
                        }
                    }
                }
            }
        }

        String ans = "No";
        for(int i=0; i<list2.size(); i++){
            Dice d1 = list2.get(i);
            for(int j=i+1; j<list2.size(); j++){
                Dice d2 = list2.get(j);
                for(int k=0; k<4; k++){
                    d2 = rotateRight(d2);
                    if(d1.dice[0]!=d2.dice[0] && d1.dice[1]==d2.dice[1] && 
                       d1.dice[2]==d2.dice[2] && d1.dice[3]==d2.dice[4] && d1.dice[4]==d2.dice[3]){
                        ans = "Yes";
                    }
                }
            }
        }

        System.out.println(ans);
    }

    int[][] abs = {{2,1,0,3},{3,2,1,0},{0,3,2,1},{1,0,3,2}};

    void bfs(int y, int x){
        //y, x, pos, prior
        Queue<int[]> q = new LinkedList<int[]>();
        q.add(new int[]{y, x, 0, 0});
        boolean[][] v = new boolean[h][w];
        v[y][x] = true;

        char[] c = new char[6];
        HashSet<Character> set = new HashSet<Character>();

        int black = 0;
        int cnt = 0;
        while(q.size()>0){
            int[] yx = q.poll();
            int yy = yx[0], xx = yx[1], pos = yx[2], prior = yx[3];
          
            c[pos] = sheet[yy][xx];
            set.add(c[pos]);
            if(sheet[yy][xx]=='#') black++;
            cnt++;
            sheet[yy][xx] = '.';  

            for(int i=0; i<4; i++){
                int ny = yy + dy[(i+prior)%4], nx = xx + dx[(i+prior)%4];
                if(ny<0 || ny>=h || nx<0 || nx>=w || v[ny][nx] || sheet[ny][nx]=='.') continue;
                for(int j=0; j<4; j++){
                    if(nei[nei[pos][i]][j]==pos){
                        v[ny][nx] = true;
                        q.add(new int[]{ny, nx, nei[pos][i], (abs[i][j]+prior)%4});
                    }
                }
            }
        }



        if(cnt==6 && black==3 && set.size()==4){
            list.add(new Dice(c[0], c[1], c[2], c[3], c[4], c[5]));
            //System.out.println(c[0]+" "+c[1]+" "+c[2]+" "+c[3]+" "+c[4]+" "+c[5]);
            for(int i=0; i<6; i++){
                if(c[i]=='#') continue;
                colors[color.indexOf(c[i])]++;
            }
        }
    }

    public static void main(String[] args){
        new Main().solve();
    }
}
