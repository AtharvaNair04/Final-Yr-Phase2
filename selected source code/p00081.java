import java.util.Scanner;

class Main{

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		String str="";
		String[] dset;
		double[] moji=new double[6];
		double[] kotae=new double[2];

		while(sc.hasNext()) {
			str=sc.next();
			dset=str.split(",");
			for(int i=0; i<6; i++) {
				moji[i]=Double.parseDouble(dset[i]);
			}
			kotae=kotae(moji);
			System.out.println(kotae[0]+" "+kotae[1]);
			moji=new double[6];
			kotae=new double[2];
		}
	}

	public static double[] kotae(double[] a) {
		double[] ansary=new double[2];
		ansary[0]=0;
		ansary[1]=0;
		double difx=0;
		double dify=0;
		double ktm1=0;

		double[] cyokusen = new double[3];		// katamuki suihei_ka_suicyoku_ka  y_spn
												//y jiku ni heikou -1  x jikuni heikou 1
		double[] suisen=new double[3];		//	suise wo hiku

		difx=a[2]-a[0];
		dify=a[3]-a[1];

		if(Math.abs(dify)==0) {
			cyokusen[0]=0;
			cyokusen[1]=4;
			cyokusen[2]=a[3];		// y seppen

			suisen[0]=0;
			suisen[1]=-4;
			suisen[2]=a[4];		// y jiku heikou nara   x seppen

			ansary[0]=a[4];
			ansary[1]=cyokusen[2]*2-a[5];

			return ansary;


		}
		else if(Math.abs(difx)==0) {
			cyokusen[0]=0;
			cyokusen[1]=-4;
			cyokusen[2]=a[2];		// x seppen

			suisen[0]=0;
			suisen[1]=4;
			suisen[2]=a[5];				//垂線のあたりが微妙

			ansary[0]=cyokusen[2]*2-a[4];
			ansary[1]=suisen[2];

			return ansary;
		}
		else {
			cyokusen[0]=dify/difx;
			cyokusen[1]=0;
			cyokusen[2]=a[3]-cyokusen[0]*a[2];

			ktm1=-1/cyokusen[0];

			suisen[0]=ktm1;
			suisen[2]=a[5]-suisen[0]*a[4];

			ansary[0]=-1*(cyokusen[2]-suisen[2])/(cyokusen[0]-suisen[0]);
			ansary[1]=ansary[0]*cyokusen[0]+cyokusen[2];		//交点

			//System.out.println(ansary[0]+","+ansary[1]);
			ansary[1]=ansary[1]*2-a[5];
			ansary[0]=ansary[0]*2-a[4];

		}
		return ansary;
	}
}
