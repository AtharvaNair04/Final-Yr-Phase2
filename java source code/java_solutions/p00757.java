import java.io.*;
import java.util.*;

public class Main {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));    
	private static StringTokenizer st;

    public static void main ( String [] args ) throws IOException
    {

	int h,w,s;

	while(true){
	
		st = new StringTokenizer(br.readLine());
		h = Integer.parseInt(st.nextToken());
		w = Integer.parseInt(st.nextToken());
		s = Integer.parseInt(st.nextToken());
		if( h == 0 && w == 0 && s == 0)	break;
	    new Main().cal(h,w,s);
	}
	
    }


int min;
int[][] area;
int[][][][] sumArea;
int[][] basicSum;
int[][][][] grouparray;
int[][][][] minDep;

    private void cal(int h, int w, int s) throws IOException 
    {
	int areaSum = 0;
	area = new int[h][w];
	grouparray = new int[h][w][h][w];
	minDep =new int[h][w][h][w];
	for(int i=0; i < h; i++){
		st = new StringTokenizer(br.readLine());
		for(int j=0; j < w; j++){
			area[i][j] = Integer.parseInt(st.nextToken());
			areaSum += area[i][j];
		}
	}
	calBasicSum(h,w);
	calSum(h,w);

	//cal max
	min = areaSum - s;
	int[] out = grouping(0,0,h-1,w-1);

	System.out.println(out[0]+" "+(out[1]-min));

    }


    private int[] grouping(int hs, int ws, int hl, int wl){
	int[] ret = new int[2];
	if( grouparray[hs][ws][hl][wl] != -1 ){
		ret[0] = grouparray[hs][ws][hl][wl];
		ret[1] = minDep[hs][ws][hl][wl];
		return ret;
	}
	grouparray[hs][ws][hl][wl] = 0;
	ret[0] = 1;
	ret[1] = sumArea[hs][ws][hl][wl];
	int[] tmp = new int[2];
	int grptmp;
	int tempDep;

	//&#44032;&#47196;&#47196; &#51900;&#44060;&#44592;
	for(int i = hs; i < hl; i++){
		if(sumArea[hs][ws][i][wl] < min){
			continue;
		}
		if(sumArea[i+1][ws][hl][wl] < min){
			break;
		}
		tmp = grouping(hs, ws, i, wl);
		grptmp = tmp[0];
		tempDep = tmp[1];

		tmp = grouping(i+1, ws, hl, wl);
		grptmp += tmp[0];
		if( tempDep < tmp[1] ){ tmp[1] = tempDep; }

		if( grptmp > ret[0] ){ ret[0] = grptmp; ret[1] = tmp[1]; }
		if( grptmp == ret[0] && tmp[1] > ret[1] ){ ret[1] = tmp[1]; }
	}
	//&#49464;&#47196;&#47196; &#51900;&#44060;&#44592;
	for(int i = ws; i < wl; i++){
		if(sumArea[hs][ws][hl][i] < min){
			continue;
		}
		if(sumArea[hs][i+1][hl][wl] < min){
			break;
		}
		tmp = grouping(hs, ws, hl, i);
		grptmp = tmp[0];
		tempDep = tmp[1];

		tmp = grouping(hs, i+1, hl, wl);
		grptmp += tmp[0];

		if( tempDep < tmp[1] ){ tmp[1] = tempDep; }

		if( grptmp > ret[0] ){ ret[0] = grptmp; ret[1] = tmp[1]; }
		if( grptmp == ret[0] && tmp[1] > ret[1] ){ ret[1] = tmp[1]; }

	}

	grouparray[hs][ws][hl][wl] = ret[0];
	minDep[hs][ws][hl][wl] = ret[1];
	return ret;
	
    }


   private void calBasicSum(int h, int w){
	basicSum = new int[h][w];
	basicSum[0][0] = area[0][0];
	for(int j = 1; j < w; j++){
		basicSum[0][j] = basicSum[0][j-1] + area[0][j];
	}
	for(int i = 1; i < h; i++){
		basicSum[i][0] = basicSum[i-1][0] + area[i][0];
	}
	for(int i = 1; i < h; i++){
		for(int j=1; j < w; j++){
			basicSum[i][j] = basicSum[i][j-1] + basicSum[i-1][j] + area[i][j] - basicSum[i-1][j-1];
		}
	}
   }

   private void calSum(int h, int w ){
	sumArea = new int[h][w][h][w];

	for(int i = 0; i < h; i++){
	for(int j = 0; j < w; j++){
		for(int l = i; l < h; l++){
		for(int k = j; k < w; k++){
			grouparray[i][j][l][k] = -1;
			if( i == 0 && j ==0 ){
				sumArea[0][0][l][k] = basicSum[l][k];
			}
			else if( i == 0 ){
				sumArea[0][j][l][k] = basicSum[l][k] - basicSum[l][j-1];
			}
			else if( j == 0 ){
				sumArea[i][0][l][k] = basicSum[l][k] - basicSum[i-1][k];
			}
			else{
				sumArea[i][j][l][k] = basicSum[l][k] - basicSum[l][j-1] - basicSum[i-1][k] + basicSum[i-1][j-1];
			}
		}
		}
	}
	}
   }


}
