import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	static double[] shou ={0.0, 0.5,0.25,0.125,0.0625};
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		label:while(true){
			double d = cin.nextDouble();
			if(d < 0.0){
				break;
			}
			int dd = (int)d;
			String seisuubu = Integer.toBinaryString(dd);
			if(seisuubu.length() > 8){
				System.out.println("NA");
				continue;
			}
			d = d - (double)dd;
			//System.out.println(d + " " + dd);
			for(int i = seisuubu.length();i<8;i++){
				seisuubu = "0"+seisuubu;
			}
			if(d==0.0){
				System.out.println(seisuubu + "."  + "0000");
				continue;
			}
			for(int i = 0;i<2;i++){
				for(int j = 0;j<2;j++){
					for(int k = 0;k<2;k++){
						for(int h = 0;h<2;h++){
							double value = 0.0;
							if(i==1){
								value+=shou[1];
							}
							if(j==1){
								value+=shou[2];
							}
							if(k==1){
								value+=shou[3];
							}
							if(h==1){
								value+=shou[4];
							}
							if(value==d){
								System.out.print(seisuubu + ".");
								System.out.println(i+""+j+""+k+""+h);
								continue label;
							}
						}
					}
				}
			}
			System.out.println("NA");
		}

	}

}
