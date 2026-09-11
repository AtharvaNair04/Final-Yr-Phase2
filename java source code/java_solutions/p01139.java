import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

public class Main{
	static int[][] map;
	static int[][] used;
	static int w, h;
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		while(true){
			w = in.nextInt();
			h = in.nextInt();
			if(w == 0) break;
			map = new int[h][w];
			used = new int[h][w];
			checked = new int[h][w];
			for(int i=0; i<h; i++){
				char[] s = in.next().toCharArray();
				for(int j=0; j<w; j++){
					map[i][j] = s[j]=='.'?0:s[j]=='W'?1:2;
				}
			}
			turn = 1;
			for(int i=0; i<h; i++){
				for(int j=0; j<w; j++){
					if(map[i][j] == 0) continue;
					int color = map[i][j];
					for(int k=0; k<4; k++){
						int nx = j+dx[k];
						int ny = i+dy[k];
						if(out(nx, ny) || map[ny][nx] != 0) continue;
						if(checked[ny][nx] != 0) continue;
						if(dfs(nx, ny, color)){
							check(nx, ny, color);
						}
						turn++;
					}
				}
			}
			int white = 0;
			int black = 0;
			for(int i=0; i<h; i++){
				for(int j=0; j<w; j++){
					if(used[i][j] == 1) white++;
					else if(used[i][j] == 2) black++;
				}
			}
			System.out.println(black+" "+white);
		}
	}
	static int turn;
	static String dump(int[][] map){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<h; i++){
			for(int j=0; j<w; j++){
				sb.append(map[i][j]+" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	static void check(int x, int y, int color){
		if(used[y][x] != 0) return;
		used[y][x] = color;
		for(int i=0; i<4; i++){
			int nx = x+dx[i];
			int ny = y+dy[i];
			if(out(nx, ny) || map[ny][nx] == color) continue;
			check(nx, ny, color);
		}
	}
	
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static int[][] checked;
	static boolean dfs(int x, int y, int color){
		if(checked[y][x] == turn) return true;
		else if(checked[y][x] != 0) return false;
		checked[y][x] = turn;
		for(int i=0; i<4; i++){
			int nx = x+dx[i];
			int ny = y+dy[i];
			if(out(nx, ny) || map[ny][nx] == color){
				continue;
			}
			if(map[ny][nx] == 0){
				if(!dfs(nx, ny, color)) return false;
				continue;
			}
			return false;
		}
		return true;
	}
	
	static boolean out(int x, int y){
		return x<0||x>=w||y<0||y>=h;
	}
}
