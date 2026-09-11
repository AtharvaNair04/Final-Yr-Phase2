import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//The Morning after Halloween
public class Main{

	int w, h, N;
	char[][] map;
	int[][] d = {{-1,0},{0,1},{1,0},{0,-1},{0,0}};
	byte INF = Byte.MAX_VALUE, MIN = Byte.MIN_VALUE;
	byte[][][] dist;
	int[] si, sj, gi, gj;
	
	int one(){
		for(int i=0;i<w*h;i++)dist[0][0][i]=INF;
		dist[0][0][si[0]*w+sj[0]] = MIN;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(si[0]*w+sj[0]);
		while(!q.isEmpty()){
			int V = q.poll();
			int i = V/w, j = V%w;
			if(i==gi[0]&&j==gj[0])return (int)dist[0][0][V] + 128;
			for(int k=0;k<4;k++){
				int ni = i+d[k][0], nj = j+d[k][1];
				if(map[ni][nj]==' '&&dist[0][0][ni*w+nj]==INF){
					dist[0][0][ni*w+nj] = (byte) (dist[0][0][V]+1);
					q.add(ni*w+nj);
				}
			}
		}
		return -1;
	}
	int two(){
		int M = w*h;
		for(int i=0;i<M;i++)for(int j=0;j<M;j++)dist[i][j][0]=INF;
		dist[si[0]*w+sj[0]][si[1]*w+sj[1]][0] = MIN;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add((si[0]*w+sj[0])*M+si[1]*w+sj[1]);
		while(!q.isEmpty()){
			int V = q.poll(), VA = V/M, VB = V%M;
			int ai = VA/w, aj = VA%w, bi = VB/w, bj = VB%w;
			if(ai==gi[0]&&aj==gj[0]&&bi==gi[1]&&bj==gj[1])return (int)dist[VA][VB][0] + 128;
			for(int k0=0;k0<5;k0++){
				int Ai = ai+d[k0][0], Aj = aj+d[k0][1];
				if(map[Ai][Aj]=='#')continue;
				for(int k1=0;k1<5;k1++){
					int Bi = bi+d[k1][0], Bj = bj+d[k1][1];
					if(map[Bi][Bj]=='#')continue;
					if(Ai==Bi&&Aj==Bj)continue;
					if(Ai==bi&&Aj==bj&&Bi==ai&&Bj==aj)continue;
					int PA = Ai*w+Aj, PB = Bi*w+Bj;
					if(dist[PA][PB][0]!=INF)continue;
					dist[PA][PB][0] = (byte) (dist[VA][VB][0]+1);
					q.add(PA*M+PB);
				}
			}
		}
		return -1;
	}
	int three(){
		int FIX = 0;
		int M = w*h, MM = w*h*w*h, SA = si[0]*w+sj[0], SB = si[1]*w+sj[1], SC = si[2]*w+sj[2];
		for(int i=0;i<M;i++)for(int j=0;j<M;j++)for(int k=0;k<M;k++)dist[i][j][k]=INF;
		dist[SA][SB][SC] = MIN;
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(SA*MM+SB*M+SC);
		while(!q.isEmpty()){
			int V = q.poll(), VA = V/MM, VB = (V%MM)/M, VC = V%M;
			int ai = VA/w, aj = VA%w, bi = VB/w, bj = VB%w, ci = VC/w, cj = VC%w;
			if(dist[VA][VB][VC]==INF-1){
				FIX = dist[VA][VB][VC] + 128;
				for(int i=0;i<M;i++)for(int j=0;j<M;j++)for(int k=0;k<M;k++)if(dist[i][j][k]!=INF)dist[i][j][k]=MIN;
			}
			if(ai==gi[0]&&aj==gj[0]&&bi==gi[1]&&bj==gj[1]&&ci==gi[2]&&cj==gj[2])return (int)dist[VA][VB][VC] + 128 + FIX;
			for(int k0=0;k0<5;k0++){
				int Ai = ai+d[k0][0], Aj = aj+d[k0][1], PA = Ai*w+Aj;
				if(map[Ai][Aj]=='#')continue;
				for(int k1=0;k1<5;k1++){
					int Bi = bi+d[k1][0], Bj = bj+d[k1][1], PB = Bi*w+Bj;
					if(map[Bi][Bj]=='#')continue;
					if(PA==PB)continue;
					if(PA==VB&&PB==VA)continue;
					for(int k2=0;k2<5;k2++){
						int Ci = ci+d[k2][0], Cj = cj+d[k2][1], PC = Ci*w+Cj;
						if(map[Ci][Cj]=='#')continue;
						if(PA==PC||PB==PC)continue;
						if(PA==VC&&PC==VA || PB==VC&&PC==VB)continue;
						if(dist[PA][PB][PC]!=INF)continue;
						dist[PA][PB][PC] = (byte) (dist[VA][VB][VC]+1);
						q.add(PA*MM+PB*M+PC);
					}
				}
			}
		}
		return -1;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		dist = new byte[256][256][256];
		si = new int[3]; sj = new int[3]; gi = new int[3]; gj = new int[3];
		for(;;){
			w = sc.nextInt(); h = sc.nextInt(); N = sc.nextInt();
			if((w|h|N)==0)break;
			sc.nextLine();
			map = new char[h][];
			for(int i=0;i<h;i++){
				map[i] = sc.nextLine().toCharArray();
				for(int j=0;j<w;j++){
					if(Character.isLowerCase(map[i][j])){
						si[map[i][j]-'a'] = i; sj[map[i][j]-'a'] = j; map[i][j] = ' ';
					}
					else if(Character.isUpperCase(map[i][j])){
						gi[map[i][j]-'A'] = i; gj[map[i][j]-'A'] = j; map[i][j] = ' ';
					}
				}
			}
			System.out.println(N==1?one():N==2?two():three());
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
