import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	String strArr[] = br.readLine().split(" ");
        	int[] a=new int[12];

        	for(int j=0; j<12; j++){
        		a[j]=Integer.parseInt(strArr[j]);
        	}

        	for(int i=0;i<12-1;i++){
        	      for(int j=12-1;j>i;j--){

        		if(a[j]<a[j-1]){
        		  int t=a[j];
        		  a[j]=a[j-1];
        		  a[j-1]=t;
        		}
        	  }
        	}

        	if(a[0] == a[1] && a[2] == a[1] && a[3] == a[2] && a[4]==a[5] && a[5]==a[6] && a[6] == a[7] && a[8] == a[9] && a[9] == a[10] && a[10] == a[11]){
        		System.out.println("yes");
        	}
        	else{
        		System.out.println("no");
        	}
    }
}
