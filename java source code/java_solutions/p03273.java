import java.util.*;
class Main
{
    public static void main (String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int h = sc.nextInt(), w = sc.nextInt();

        int[][] a = new int[h][w];
        for(int i = 0; i < h; i++){
            String s = sc.next();
            for(int j = 0; j < w; j++){
                if(s.charAt(j) == '.') a[i][j] = 0;
                else a[i][j] = 1;
            }
        }
        for(int i = 0;  i < h; i++){
            int tmp = 0;
            for(int j = 0; j < w; j++){
                tmp += a[i][j];
            }
            if(tmp == 0){
                for(int k = i; k < h-1; k++){
                    for(int j = 0; j < w; j++){
                        a[k][j] = a[k+1][j];
                    }
                }
                h--;
                i--;
            }
        }
        for(int j = 0; j < w; j++){
            int tmp = 0;
            for(int i = 0; i < h; i++){
                tmp += a[i][j];
            }
            if(tmp == 0){
                for(int k = j; k < w-1; k++){
                    for(int i = 0; i < h; i++){
                        a[i][k] = a[i][k+1];
                    }
                }
                w--;
                j--;
            }
        }
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                if(a[i][j] == 0)
                    System.out.print(".");
                else
                    System.out.print("#");
            }
            System.out.print("\n");
        }
    }
 }
