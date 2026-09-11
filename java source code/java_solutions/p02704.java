import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
	static int rowBitAnd(long mat[][] , int row){
		int ret = 1;
		for(int c = 0 ; c < mat[row].length ; ++c){
			if(mat[row][c] == -1){
				continue;
			}
			ret &= mat[row][c];
		}
		return ret;
	}

	static int rowBitOr(long mat[][] , int row){
		int ret = 0;
		for(int c = 0 ; c < mat[row].length ; ++c){
			if(mat[row][c] == -1){
				continue;
			}
			ret |= mat[row][c];
		}
		return ret;
	}

	static int colBitAnd(long mat[][] , int col){
		int ret = 1;
		for(int r = 0 ; r < mat.length ; ++r){
			if(mat[r][col] == -1){
				continue;
			}
			ret &= mat[r][col];
		}
		return ret;
	}

	static int colBitOr(long mat[][] , int col){
		int ret = 0;
		for(int r = 0 ; r < mat.length ; ++r){
			if(mat[r][col] == -1){
				continue;
			}
			ret |= mat[r][col];
		}
		return ret;
	}
	static int get(Set<Integer> set){
		for(int v : set)return v;
		return -1;
	}
	static long[][] constructBit(int S[] , int[] T , int U[] , int V[]){
		int N = S.length;
		long result[][] = new long[N][N];
		Set<Integer> undefRow = new HashSet<Integer>();
		Set<Integer> undefCol = new HashSet<Integer>();
		for(int i = 0 ; i < N ; ++i){
			Arrays.fill(result[i], -1);
			undefRow.add(i);
			undefCol.add(i);
		}
		for(int i = 0 ; i < N ; ++i){
			if(S[i] == 0 && U[i] == 1){
				undefRow.remove(i);
				Arrays.fill(result[i] , 1);
			}
			if(S[i] == 1 && U[i] == 0){
				undefRow.remove(i);
				Arrays.fill(result[i] , 0);
			}
		}
		for(int i = 0 ; i < N ; ++i){
			if(T[i] == 0 && V[i] == 1){
				undefCol.remove(i);
				for(int j = 0 ; j < N ; ++j){
					if(result[j][i] == 0){
						return null;
					}
					result[j][i] = 1;
				}
			}
			if(T[i] == 1 && V[i] == 0){
				undefCol.remove(i);
				for(int j = 0 ; j < N ; ++j){
					if(result[j][i] == 1){
						return null;
					}
					result[j][i] = 0;
				}			
			}			
		}
		if(undefCol.size() > 1 && undefRow.size() > 1){
			List<Integer> rows = new ArrayList<Integer>(undefRow);
			List<Integer> cols = new ArrayList<Integer>(undefCol);
			for(int i = 0 ; i < rows.size() ; ++i){
				for(int j = 0 ; j < cols.size() ; ++j){
					int r = rows.get(i);
					int c = cols.get(j);
					result[r][c] = (i + j) % 2 == 0 ? 1 : 0;
				}
			}
			return result;
		}else if(undefCol.size() <= 1){
			if(undefCol.isEmpty()){
				for(int r : undefRow){
					if(S[r] == 0 && rowBitAnd(result, r) == 1)return null;
					if(S[r] == 1 && rowBitOr(result, r ) == 0)return null;
				}
				return result;
			}else{ // undefCol.size() == 1
				int c = get(undefCol);
				for(int r : undefRow){
					if(S[r] == 0){
						if(rowBitAnd(result, r) == 1)result[r][c] = 0;
						else result[r][c] = T[c] == 1 ? 1 : 0;
					}else{
						if(rowBitOr(result, r) == 0)result[r][c] = 1;
						else result[r][c] = T[c] == 1 ? 1 : 0;
					}
				}
				if(T[c] == 0 && colBitAnd(result, c) == 1) return null;
				if(T[c] == 1 && colBitOr(result, c) == 0)return null; 
				return result;
			}
		}else{ // undefRow.size() <= 1
			if(undefRow.isEmpty()){
				for(int c : undefCol){
					if(T[c] == 0 && colBitAnd(result, c) == 1)return null;
					if(T[c] == 1 && colBitOr(result, c) == 0)return null;					
				}
				return result;
			}else{
				int r = get(undefRow);
				for(int c : undefCol){
					if(T[c] == 0){
						if(colBitAnd(result, c) == 1)result[r][c] = 0;
						else result[r][c] = S[r] == 1 ? 1 : 0;
					}else{
						if(colBitOr(result, c) == 0)result[r][c] = 1;
						else result[r][c] = S[r] == 1 ? 1 : 0;						
					}
				}
				if(S[r] == 0 && rowBitAnd(result, r) == 1)return null;
				if(S[r] == 1 && rowBitOr(result, r) == 0)return null;
				return result;
			}
		}
	}
	static boolean construct(int S[] , int T[] , BigInteger U[] , BigInteger V[]){
		int N = S.length;
		long result[][] = new long[N][N];
		int bmax = 60;
		for(int i = 0 ; i < bmax ; ++i){
			int Ubit[] = new int[N];
			int Vbit[] = new int[N];
			for(int j = 0 ; j < U.length ; ++j){
				if(U[j].testBit(i)){
					Ubit[j] = 1;
				}
				if(V[j].testBit(i)){
					Vbit[j] = 1;
				}
			}			
			long A[][] = constructBit(S, T , Ubit , Vbit);
			if(A == null){
				return false;
			}
			long bit = 1L << (long)i;
			for(int j = 0 ; j < N ; ++j){
				for(int k = 0 ; k < N ; ++k){
					if(A[j][k] == 1){
						result[j][k] |= bit;
					}
				}
			}
		}
		BigInteger res2[][] = new BigInteger[N][N];
		for(int i = 0 ; i < res2.length ; ++i){
			for(int j = 0 ; j < N ; ++j){
				res2[i][j] = BigInteger.valueOf(result[i][j]);
			}
		}
		for(int i = bmax ; i < 64 ; ++i){
			int Ubit[] = new int[N];
			int Vbit[] = new int[N];
			for(int j = 0 ; j < U.length ; ++j){
				if(U[j].testBit(i)){
					Ubit[j] = 1;
				}
				if(V[j].testBit(i)){
					Vbit[j] = 1;
				}
			}			
			long A[][] = constructBit(S, T , Ubit , Vbit);
			if(A == null){
				return false;
			}
			for(int j = 0 ; j < N ; ++j){
				for(int k = 0 ; k < N ; ++k){
					if(A[j][k] == 1){
						res2[j][k] = res2[j][k].setBit(i);
					}
				}
			}

		}
		StringBuilder ret = new StringBuilder();
		for(int i = 0 ; i < N ; ++i){
			StringBuilder line = new StringBuilder();
			for(int j = 0 ; j < N ; ++j){
				if(j > 0){
					line.append(' ');
				}
				line.append(res2[i][j]);
			}
			ret.append(line);
			ret.append('\n');
//			System.out.println(line);
		}
		System.out.println(ret);
		return true;
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int S[] = new int[N];
		int T[] = new int[N];
		BigInteger U[] = new BigInteger[N];
		BigInteger V[] = new BigInteger[N];
		for(int i = 0 ; i < N ; ++i){
			S[i] = sc.nextInt();
		}
		for(int i = 0 ; i < N ; ++i){
			T[i] = sc.nextInt();
		}
		for(int i = 0 ; i < N ; ++i){
			U[i] = sc.nextBigInteger();
		}
		for(int i = 0 ; i < N ; ++i){
			V[i] = sc.nextBigInteger();
		}
		/*
		Random rand = new Random();
		for(int rep = 0 ; rep < 100 ; ++rep){
			for(int i = 0 ; i < N ; ++i){
				S[i] = 1;//rand.nextInt(2);
				T[i] = 1;//rand.nextInt(2);
				long l = rand.nextInt(1 << 30);
				l += ((long)(rand.nextInt(1 << 30)) ) << 31;
				U[i] = BigInteger.valueOf(l);
				l = rand.nextInt(1 << 30);
				l += ((long)(rand.nextInt(1 << 30)) ) << 31;
				V[i] = BigInteger.valueOf(l);
			}
			long ts = System.currentTimeMillis();
			construct(S, T, U, V);
			long te = System.currentTimeMillis();
			System.out.println(te - ts);
		}
		*/
		if(!construct(S, T , U , V)){
			System.out.println(-1);
		}
	}
}
