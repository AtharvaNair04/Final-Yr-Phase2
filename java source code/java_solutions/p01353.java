import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TreeSet;
 
public class Main {
	
	public static class Monster implements Comparable<Monster>{
		long atk;
		long live;
		
		private Monster(long atk, long live) {
			super();
			this.atk = atk;
			this.live = live;
		}

		@Override
		public int compareTo(Monster arg0) {
			long own = this.atk * arg0.live;
			long oth = arg0.atk * this.live;
			
			if(own < oth){
				return 1;
			}else if(own > oth){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in);
		
		final int n = sc.nextInt();
		
		final long hp = sc.nextLong();
		final long atk = sc.nextLong();
		final long def = sc.nextLong();
		final long spd = sc.nextLong();
		
		ArrayList<Monster> monsters = new ArrayList<Main.Monster>();
		
		boolean tumi = false;
		long current_hp = hp;
		for(int i = 0; i < n; i++){
			final long h = sc.nextLong();
			final long a = sc.nextLong();
			final long d = sc.nextLong();
			final long s = sc.nextLong();
			
			final long monster_atk = Math.max(a - def, 0);
			final long usagi_atk = Math.max(atk - d, 0);
			
			if(spd < s){
				current_hp -= monster_atk;
			}
			
			if(usagi_atk == 0 && !(monster_atk == 0)){
				tumi = true;
				continue;
			}else if(monster_atk == 0){
				continue;
			}
			
			long live = h / usagi_atk;
			if(h % usagi_atk != 0){
				live++;
			}
			
			monsters.add(new Monster(monster_atk, live));
		}
		
		if(tumi || current_hp <= 0){
			System.out.println(-1);
			return;
		}
		
		Collections.sort(monsters);
		
		long turn = 0;
		for(Monster monster : monsters){
			turn += monster.live;
			
			current_hp -= (turn - 1) * (monster.atk);
			if(current_hp <= 0){
				break;
			}
		}
		
		System.out.println((current_hp <= 0) ? -1 : (hp - current_hp));
		
	}
	
	public static class Scanner {
		
		private BufferedReader br;
		private StringTokenizer tok;
		
		public Scanner(InputStream is) throws IOException{
			br = new BufferedReader(new InputStreamReader(is));
			getLine();
		}
		
		private void getLine() throws IOException{
			while(tok == null || !tok.hasMoreTokens()){
				tok = new StringTokenizer(br.readLine());
			}
		}
		
		private boolean hasNext(){
			return tok.hasMoreTokens();
		}
		
		public String next() throws IOException{
			if(hasNext()){
				return tok.nextToken();
			}else{
				getLine();
				return tok.nextToken();
			}
		}
		
		public int nextInt() throws IOException{
			if(hasNext()){
				return Integer.parseInt(tok.nextToken());
			}else{
				getLine();
				return Integer.parseInt(tok.nextToken());
			}
		}
		
		public long nextLong() throws IOException{
			if(hasNext()){
				return Long.parseLong(tok.nextToken());
			}else{
				getLine();
				return Long.parseLong(tok.nextToken());
			}
		}
		
		public double nextDouble() throws IOException{
			if(hasNext()){
				return Double.parseDouble(tok.nextToken());
			}else{
				getLine();
				return Double.parseDouble(tok.nextToken());
			}
		}
	}
}
