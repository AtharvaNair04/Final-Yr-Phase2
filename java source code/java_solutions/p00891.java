import java.util.Scanner;

public class Main{

	final int mod1 = 1000000007;
	final int mod2 = 999999997;
	final int base1 = 999999997;
	final int base2 = 999999991;
	int[] hs1;
	int[] hs2;
	int[] bpow1;
	int[] bpow2;
	int W, H, p;
	boolean[][] board;
	boolean[][][] pattern;
	int[] table = new int[0x100];

	void run(){
		Scanner in = new Scanner(System.in);
		for(int c='A'; c<='Z'; c++) table[c] = c - 'A';
		for(int c='a'; c<='z'; c++) table[c] = c - 'a' + 26;
		for(int c='0'; c<='9'; c++) table[c] = c - '0' + 52;
		table['+'] = 62;
		table['/'] = 63;
		bpow1 = new int[10000];
		bpow1[0] = 1;
		for(int i=1; i<10000; i++){
			bpow1[i] = (int)(((long)bpow1[i-1]*base1)%mod1);
		}
		bpow2 = new int[10000];
		bpow2[0] = 1;
		for(int i=1; i<10000; i++){
			bpow2[i] = (int)(((long)bpow2[i-1]*base2)%mod2);
		}
		for(;;){
			W = in.nextInt();
			H = in.nextInt();
			p = in.nextInt();
			if(W==0 && H==0 && p==0) return ;
			board = new boolean[H][W];
			pattern = new boolean[8][p][p];
			for(int i=0; i<H; i++){
				String line = in.next();
				int[] tmp = new int[(W+5)/6];
				for(int j=0; j<(W+5)/6; j++){
					tmp[j] = table[line.charAt(j)];
				}
				for(int j=0; j<W; j++){
					board[i][j] = ((tmp[j/6]>>(5-j%6))&1)==1;
				}
			}
			for(int i=0; i<p; i++){
				String line = in.next();
				int[] tmp = new int[(p+5)/6];
				for(int j=0; j<(p+5)/6; j++){
					tmp[j] = table[line.charAt(j)];
				}
				for(int j=0; j<p; j++){
					pattern[0][i][j] = ((tmp[j/6]>>(5-j%6))&1)==1;
				}
			}
			for(int k=1; k<4; k++){
				for(int i=0; i<p; i++){
					for(int j=0; j<p; j++){
						pattern[k][i][j] = pattern[k-1][p-1-j][i];
					}
				}
			}
			for(int k=0; k<4; k++){
				for(int i=0; i<p; i++){
					for(int j=0; j<p; j++){
						pattern[k+4][i][j] = pattern[k][i][p-1-j];
					}
				}
			}
			System.out.println(solve());
		}
	}

	int hash1(boolean[][] brd, int lower){
		int ret = 0;
		for(int i=0; i<p; i++){
			for(int j=0; j<p; j++)if(brd[i+lower][j]){
				ret = (ret + bpow1[p*p-1-(i*p+j)])%mod1;
			}
		}
		return ret;
	}

	int hash2(boolean[][] brd, int lower){
		int ret = 0;
		for(int i=0; i<p; i++){
			for(int j=0; j<p; j++)if(brd[i+lower][j]){
				ret = (ret + bpow2[p*p-1-(i*p+j)])%mod2;
			}
		}
		return ret;
	}

	int solve(){
		if(p>W || p>H) return 0;
		int ans = 0;
		hs1 = new int[8];
		for(int i=0; i<8; i++){
			hs1[i] = hash1(pattern[i], 0);
		}
		hs2 = new int[8];
		for(int i=0; i<8; i++){
			hs2[i] = hash2(pattern[i], 0);
		}
		for(int i=0; i<H-p+1; i++){
			int h1 = hash1(board, i);
			int h2 = hash2(board, i);
			for(int k=0; k<8; k++)if(hs1[k] == h1 && hs2[k] == h2){
				ans++;
				break;
			}
			for(int j=0; j<W-p; j++){
				for(int k=0; k<p; k++)if(board[i+k][j]){
					h1 = (h1 - bpow1[p*p-1-k*p])%mod1;
					h2 = (h2 - bpow2[p*p-1-k*p])%mod2;
				}
				h1 = (int)((long)h1*base1%mod1);
				h2 = (int)((long)h2*base2%mod2);
				for(int k=0; k<p; k++)if(board[i+k][j+p]){
					h1 = (h1 + bpow1[(p-1-k)*p])%mod1;
					h2 = (h2 + bpow2[(p-1-k)*p])%mod2;
				}
				h1 = (h1 + mod1)%mod1;
				h2 = (h2 + mod2)%mod2;
				for(int k=0; k<8; k++)if(hs1[k] == h1 && hs2[k] == h2){
					ans++;
					break;
				}
			}
		}
		return ans;
	}

	public static void main(String args[]){
		new Main().run();
	}
}
