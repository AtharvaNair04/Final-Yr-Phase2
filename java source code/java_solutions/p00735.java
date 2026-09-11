import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static Scanner stdIn= new Scanner(System.in);
	static ArrayList<Integer> smPrime = new ArrayList<Integer>();

	public static void main(String args[]){
		while(true){
			int input = stdIn.nextInt();
			if(input==1)
				break;
			func(input);
		}
	}
	static public void func(int in){
		ArrayList<Integer> ans = new ArrayList<Integer>();
		if(isPrime(in)){
			ans.add(in);
		}
		else{
			int tmp = in;
			if(in % 7 == 1){
				tmp=in-2;
				if(in % tmp == 0 ){
					if(isPrime(in/tmp)){
						ans.add(in/tmp);
					}
				}
			}
			while(tmp>6){
				tmp-=5;
				if(in % tmp == 0 ){
					if(isPrime(in/tmp)){
						ans.add(in/tmp);
					}
				}
				tmp-=2;
				if(in % tmp == 0 ){
					if(isPrime(in/tmp)){
						ans.add(in/tmp);
					}
				}
			}
		}
		System.out.print(in+":");
		for(int i=0;i<ans.size();i++){
			System.out.print(" "+ans.get(i));
		}
		System.out.println();
	}
	private static boolean isPrime(int i) {
		for(int j=0;j<smPrime.size();j++){
			if(i==smPrime.get(j)){
//				System.out.println("222");

				return true;
			}
		}
		int tmp=i;
		if(i % 7 == 1){
			tmp=i-2;
			if(i % tmp == 0 ){
	//			System.out.println("001");

				return false;
				}
		}
		while(tmp>6){
			tmp-=5;
			if(i % tmp == 0 )
			{
		//		System.out.println("002");

				return false;
				}
			tmp-=2;
			if(i % tmp == 0 )
			{
			//	System.out.println("003");

				return false;
				}

		}
		smPrime.add(i);
		//System.out.println("!!!!");
		return true;
	}
}
