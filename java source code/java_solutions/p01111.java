import java.util.Scanner;

public class Main {

	static int BinarySearch(long low, long high, long b) {
		// low <= ? < high
		long mid = 0;
		while(low < high) {
			mid = (low + high)/2;
			long ans = mid * mid + mid;
			if(ans == b)	return (int)mid;
			else if(ans < b) low = mid+1;
			else	high = mid;
		}
		return (int)mid;
	}

    public static void main(String[] args) throws Exception {
    	try(Scanner sc = new Scanner(System.in)) {
    	/*
    	 1. (aを最小値1として)lengthのmaxを二分探索で探してaを後から求める 
    	 2. lengthを0から線形探索してa<=0になる一歩手前を求める。
    	 ** lengthを最大値なにかで仮定できないから,aのminを二分探索で探すことはできない
    	 
    	 時間がかかる最初に考えたもの:
    	 	aを1から代入していってlengthを二分探索で求めていた。
    	 	
    	 */

//			1.
    		while(true) {
    			long b = sc.nextLong();
    			if(b == 0) break;
    			b *= 2;
    			int max = BinarySearch(1, (long)Math.sqrt(b)+2, b);
    			int size = 1;
    			int a = 0;
    			for(size = max; size > 0; size--) {
    				if(b%size != 0)	continue;
    				else if((b/size - size + 1)% 2 != 0)	continue;
    				a = (int)(b/size - size + 1)/2;
    				break;
    			}
    			System.out.println(a+" "+size);
    		}
//    		2.
//    		while(true) {
//    			int b = sc.nextInt();
//    			if(b == 0) break;
//    			long length = 0;
//    			int a = 0;
//    			for(int size=0; size<b; size++) { //size大きいほど、aが小さい
//    				int x = 2*b/(size+1) - (size+1) + 1;
//    				if(2*b%(size+1) != 0)	continue;
//    				else if(x % 2 != 0)	continue;
//    				else if(x <= 0)	break;
//    				a = x/2;
//    				length = size+1;
//    			}
//    			System.out.println(a + " " + length);
//    		}

    	}
    }
}
