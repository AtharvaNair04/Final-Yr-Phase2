import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


class Main{

	public static void main(String[] a){
		int sankaku=0;
		int eikaku=0;
		int donkaku=0;
		int cyokkaku=0;
		int maximum,middle,minimal;
		int[] hen=new int[3];

	    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String str="";
		try{
			while(true) {
			str=br.readLine();
			if(str==null) {
				System.out.println(sankaku+" "+cyokkaku+" "+eikaku+" "+donkaku);
			}
			StringTokenizer st=new StringTokenizer(str," ");

			for(int i=0; i<3; i++) {
				hen[i]=Integer.parseInt(st.nextToken());
			}

			maximum=Math.max(hen[0],Math.max(hen[1],hen[2]));
			if(hen[0]==maximum) hen[0]=0;
			else if(hen[1]==maximum) hen[1]=0;
			else if(hen[2]==maximum) hen[2]=0;
			middle=Math.max(hen[0],Math.max(hen[1],hen[2]));
			if(hen[0]==middle) hen[0]=0;
			else if(hen[1]==middle) hen[1]=0;
			else if(hen[2]==middle) hen[2]=0;
			minimal=Math.max(hen[0],Math.max(hen[1],hen[2]));

			if(maximum>=middle+minimal || minimal<=maximum-middle) {
				System.out.println(sankaku+" "+cyokkaku+" "+eikaku+" "+donkaku);
				System.exit(0);
				}
			else if(maximum*maximum==(minimal*minimal+middle*middle)) {
				sankaku++;
				cyokkaku++;
			}
			else if(maximum*maximum>minimal*minimal+middle*middle) {
				sankaku++;
				donkaku++;
			}
			else if(maximum*maximum<minimal*minimal+middle*middle) {
				sankaku++;
				eikaku++;
			}
		}

		}catch(Exception e){
			System.exit(0);
		}
	}
}
