import java.util.Scanner;
public class Main{
	Scanner sc = new Scanner(System.in);
	int pm,pe,pj;
	double avg1,avg2;
	int nin;
	public void sei(){
		nin = sc.nextInt();
		while(!(nin==0)){
		for(int i = 1;i<=nin;i++){
			pm = sc.nextInt();
			pe = sc.nextInt();
			pj = sc.nextInt();
			if((pm==100)||(pe==100)||(pj==100)){
				System.out.println("A");
			}else{
				avg1 = (pm +pe) / 2;
				if(avg1>=90){
					System.out.println("A");
					}else{
						avg2 = (pm + pe + pj) / 3;
						if(avg2>=80){
							System.out.println("A");
						}else{
							if(avg2>=70){
								System.out.println("B");
							}else{
								if(avg2>=50){
									if((pm>=80)||(pe>=80)){
										System.out.println("B");
									}else{
										System.out.println("C");
									}
								}else{
									System.out.println("C");
								}
							}
						}

				}

			}


		}
		nin = sc.nextInt();
		}
	}
	public static void main(String [] args){
		Main a= new Main();
		a.sei();
	}

}
