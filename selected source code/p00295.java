import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Main {
	public static void main(String args[])throws IOException{
		BufferedReader br=
				new BufferedReader(new InputStreamReader(System.in));
		int cNum=Integer.parseInt(br.readLine());
		for(int i=0;i<cNum;i++){
			String buf=br.readLine();
			String sbuf[]=buf.split(" ");
			int cubes[]=new int[30];
			for(int j=0;j<30;j++) cubes[j]=Integer.parseInt(sbuf[j]);
			System.out.println(searchAnswer(cubes));
		}
	}
	static int searchAnswer(int cubes[]){
		if(isClear(cubes)) return 0;

		Set<String> check=new HashSet<String>();
		check.add(Arrays.toString(cubes));

		int originalCubes[]=new int[30];
		for(int i=0;i<30;i++) originalCubes[i]=cubes[i];

		int dequeueSize=4;
		Queue<Integer> paths = new ArrayDeque<Integer>();
		for(int i=0;i<dequeueSize;i++) paths.add(i+1);

		for(int i=1,digit=10;i<9;i++,digit*=10){
			int newDequeueSize=0;
			for(int j=0;j<dequeueSize;j++){
				int opes=paths.poll();
				int back=0;
				//System.out.println("?????????"+opes);
				for(int ope=opes%10,opes2=opes;opes2>0;opes2/=10,ope=opes2%10){
					operate(ope,cubes);
					back=back*10+ope;
				}

				//for(int count=0;count<cubes.length;count++)
				//	System.out.print(cubes[count]);
				//System.out.println();

				if(isClear(cubes)) return i;
				if(!check.contains(Arrays.toString(cubes))){
					//System.out.println("??????");
					for(int k=1;k<=4;k++){
						if(back%10==k) continue;
					//	System.out.println(opes+k*digit);
						paths.add(opes+k*digit);
					}
					//System.out.println("?????????");
					newDequeueSize+=3;
					check.add(Arrays.toString(cubes));
					//System.out.println(Arrays.toString(cubes));
				}
				for(int k=0;k<30;k++) cubes[k]=originalCubes[k];
			}
			dequeueSize=newDequeueSize;
		}
		return -1;
	}
	static boolean isClear(int cubes[]){
		int cmp=cubes[0];
		for(int i=1;i<9;i++)
			if(cmp!=cubes[i]) return false;
		if(cubes[9]!=cubes[10]||cubes[10]!=cubes[11]) return false;
		if(cubes[12]!=cubes[13]||cubes[13]!=cubes[14]) return false;
		if(cubes[15]!=cubes[16]||cubes[16]!=cubes[17]) return false;
		if(cubes[18]!=cubes[19]||cubes[19]!=cubes[20]) return false;
		return true;
	}
	static void operate(int pat,int cubes[]){
		int replace[][][]={
				{{0,27},{1,28},{2,29},{14,15},{20,18}},
				{{2,21},{5,24},{8,27},{18,11},{12,14}},
				{{6,21},{7,22},{8,23},{9,11},{12,17}},
				{{0,23},{3,26},{6,29},{15,17},{9,20}},
		};
		for(int i=0;i<replace[pat-1].length;i++){
			int select[][]=replace[pat-1];
			int tmp=cubes[select[i][0]];
			cubes[select[i][0]]=cubes[select[i][1]];
			cubes[select[i][1]]=tmp;
		}
	}
}
