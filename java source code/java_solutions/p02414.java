import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {
		//入力
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st=new StringTokenizer(br.readLine());
        int n=Integer.parseInt(st.nextToken());
        int m=Integer.parseInt(st.nextToken());
        int l=Integer.parseInt(st.nextToken());
        long[][] Aary=new long[n][m];
        long[][] Bary=new long[m][l];
        for(int j=0;j<n;j++){
            StringTokenizer sa=new StringTokenizer(br.readLine());
            for(int i=0;i<m;i++){
                Aary[j][i]=Long.parseLong(sa.nextToken());
            }
        }
        for(int j=0;j<m;j++){
            StringTokenizer sb=new StringTokenizer(br.readLine());
            for(int i=0;i<l;i++){
                Bary[j][i]=Long.parseLong(sb.nextToken());
            }
        }

        //配列の作成と加工
        long[][] Cary = new long[n][l];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                long sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += Aary[i][k] * Bary[k][j];
                }
                Cary[i][j] = sum;
            }
        }

        //出力
        for(int j=0;j<n;j++){
            for(int i=0;i<l;i++){
                System.out.print(Cary[j][i]);
                if(i==l-1) {
                	continue;
                }
                System.out.print(" ");
            }
            System.out.println();
        }
	}

}
