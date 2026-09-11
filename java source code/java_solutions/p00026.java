import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
 
public class Main {
    public static void main(String args[]){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String line;
            int[][] map = new int[10][10];
             
            while((line=br.readLine())!=null){
                if(line.equals(""))break;
                String[] splited = line.split(",");
                int x = Integer.parseInt(splited[0]);
                int y = Integer.parseInt(splited[1]);
                int size = Integer.parseInt(splited[2]);
                map[x][y]++;
                if(x!=0)map[x-1][y]++;if(x!=9)map[x+1][y]++;
                if(y!=0)map[x][y-1]++;if(y!=9)map[x][y+1]++;
                if(size>1){
                    if(x!=0&&y!=0)map[x-1][y-1]++;if(x!=9&&y!=0)map[x+1][y-1]++;
                    if(x!=9&&y!=9)map[x+1][y+1]++;if(x!=0&&y!=9)map[x-1][y+1]++;
                }
                if(size==3){
                    if(x>=2)map[x-2][y]++;if(x<=7)map[x+2][y]++;
                    if(y>=2)map[x][y-2]++;if(y<=7)map[x][y+2]++;
                }
            }
            int max=0, tile=0;
            for(int i=0; i<100;i++){
                int den = map[i%10][i/10];
                if(den>max)max=den; if(den>0)tile++;
            }
            System.out.println(100-tile);
            System.out.println(max);
        }catch(Exception e){e.printStackTrace();}
    }
}
