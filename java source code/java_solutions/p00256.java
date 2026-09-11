import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {
	
	public static final long ONE_YEAR = 365;
	public static final long FOUR_YEAR = ONE_YEAR * 4 + 1;
	public static final long HUNDRED_YEAR = FOUR_YEAR * 25 - 1;
	public static final long FOUR_HUNDRED_YEAR = HUNDRED_YEAR * 4 + 1;
	

	public static String trans_Gre(long sum){
		 long year = (sum + 305) / 146097 * 400
				  + (sum + 305) % 146097 / 36524 * 100
				  + (sum + 305) % 146097 % 36524 / 1461 * 4
				  + (sum + 305) % 146097 % 36524 % 1461 / 365;
		 long days = sum - (365 * (year - 1) + year / 4 - year / 100 + year / 400 + 31 + 28 + 1);
		 if(days == -1){
			 year--;
			 days = 365;
		 }
		 
		 long month;
		 for(month = 3; month < 15; month++){
			 if(306 * (month+1) / 10 - 122 <= days && 306 * (month + 2) / 10 - 122 > days){
				 break;
			 }
		 }
		 
		 days -= 306 * (month + 1) / 10 - 122 - 1;
		 if(month > 12){
			 year++;
			 month -= 12;
		 }
		 
		 return year + "." + month + "." + days;
	}
	
	public static String trans_Maya(long sum){
		sum %= CYCLE;
		
		final long ki = sum % 20;
		sum /= 20;
		final long w = sum % 18;
		sum /= 18;
		final long t = sum % 20;
		sum /= 20;
		final long ka = sum % 20;
		sum /= 20;
		final long b = sum;
		
		return (b + "." + ka + "." + t + "." + w + "." + ki);
	}
	
	public static long Fairfileld(long year, long month, long day){
		if(month <= 2){
			year--;
			month += 12;
		}
		
		return (365 * year) + (year / 4) - (year / 100) + (year / 400) + ((306 * (month + 1)) / 10) + day - 428;
	}
	
	public static long Maya(long b, long ka, long t, long w, long ki){
		return (((((((b * 20) + ka) * 20) + t) * 18) + w) * 20) +  ki;
	}
	
	public static final long CYCLE = Maya(13, 0, 0, 0, 0);
	
	public static void test_maya(){
		for(int ki = 0; ki < 20; ki++){
			for(int w = 0; w < 18; w++){
				for(int t = 0; t < 20; t++){
					for(int ka = 0; ka < 20; ka++){
						for(int b = 0; b < 13; b++){
							String one = b + "." + ka + "." + t + "." + w + "." + ki;
							String two = trans_Maya(Maya(b, ka, t, w, ki));
							
							if(!one.equals(two)){
								System.out.println(one + " " + two);
							}
							
						}
					}
				}
			}
		}
	}
	
	public static void test_gre(){
		for(int year = 1; year <= 10000000; year++){
			boolean leap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
			
			if(year % 100 == 0){
				System.out.println(year);
			}
			
			for(int month = 1; month <= 12; month++){
				int day_max = 31;
				if(leap && month == 2){
					day_max = 29;
				}else if(month == 2){
					day_max = 28;
				}else if(month == 4 || month == 6 || month == 9 || month == 11){
					day_max = 30;
				}
				
				for(int day = 1; day <= day_max; day++){
					String one = year + "." + month + "." + day;
					String two = trans_Gre(Fairfileld(year, month, day));
					
					//System.out.println(Fairfileld(year, month, day));
					
					if(!one.equals(two)){
						System.out.println(one + " " + two);
						
						try {
							System.in.read();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/**/
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		//test_maya();
		//test_gre();
		//System.out.println("end");
		while(true){
			final String input = sc.next();
			
			if("#".equals(input)){
				break;
			}
			//System.out.println(input);
			String[] inputs = input.split("\\.");
			
			//boolean s_to_m = false;
			long sum = 0;
			if(inputs.length == 3){
				final int year = Integer.parseInt(inputs[0]);
				final int month = Integer.parseInt(inputs[1]);
				final int day = Integer.parseInt(inputs[2]);
				
				sum = Fairfileld(year, month, day) - Fairfileld(2012, 12, 21);
				
				System.out.println(trans_Maya(sum));
			}else{
				
				final long b = Long.parseLong(inputs[0]);
				final long ka = Long.parseLong(inputs[1]);
				final long t = Long.parseLong(inputs[2]);
				final long w = Long.parseLong(inputs[3]);
				final long ki = Long.parseLong(inputs[4]);
				
				sum = Maya(b, ka, t, w, ki) + Fairfileld(2012, 12, 21);
				
				System.out.println(trans_Gre(sum));
			}
			
		}
		
		sc.close();
	}
}
