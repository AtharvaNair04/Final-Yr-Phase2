import java.util.Scanner;

class Main{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		String str="";
		String[] stck=new String[3];
		int[] matome=new int[3];
		int c_ks=0;
		int h_ks=0;

		while(sc.hasNext()) {
		//for(int aa=0; aa<4; aa++){
			str=sc.next();
			stck=str.split(",");
			for(int i=0; i<3; i++) {
				matome[i]=Integer.parseInt(stck[i]);
			}

			if(matome[2]*matome[2]==matome[1]*matome[1]+matome[0]*matome[0]) {
				c_ks++;
			}
			else if(matome[0]==matome[1]) {
				h_ks++;
			}

			//System.out.println(a+" "+b+" "+c);

			/*if(a*a==b*b+c*c) {
				if(c_ks==0) {
					cyouhou_kake[0]=a*c*b;
					cyouhou_tasu[0]=a+c+b;
					cyouhou_kosu[0]=1;
					c_ks++;
					add=true;
				}
				else {
					for(int i=0; i<c_ks; i++) {
						if(a*b*c==cyouhou_kake[i] && a+b+c==cyouhou_tasu[i]) {
							cyouhou_kosu[i]++;
							add=true;
						}
					}
					if(add==false) {
						cyouhou_kake[c_ks]=a*c*b;
						cyouhou_tasu[c_ks]=a+b+c;
						cyouhou_kosu[c_ks]=1;
						c_ks++;
						add=true;
					}
				}
			}*/

			/*else if(a*a!=b*b+c*c && (a==b || a==c || b==c)) {
				if(h_ks==0) {
					heikshn_kake[0]=a*c*b;
					heikshn_tasu[0]=a+c+b;
					heikshn_kosu[0]=1;
					h_ks++;
					add=true;
				}
				else {
					for(int i=0; i<h_ks; i++) {
						if(a*b*c==heikshn_kake[i] && a+b+c==heikshn_tasu[i]) {
							heikshn_kosu[i]++;
							add=true;
						}
					}
					if(add==false) {
						heikshn_kake[h_ks]=a*c*b;
						heikshn_tasu[h_ks]=a+b+c;
						heikshn_kosu[h_ks]=1;
						h_ks++;
						add=true;
					}
				}
			}
			else {
				h_ks++;
			}*/
		}
		System.out.println(c_ks);
		System.out.println(h_ks);
		sc.close();
	}
}
