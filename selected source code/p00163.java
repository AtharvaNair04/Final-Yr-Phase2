import java.util.Scanner;


public class Main {
	
	public static int[][] money = new int[][]
			{
				{	0,	300,	500,	600,	700,	1350,	1650},
				{	0,	0,		350,	450,	600,	1150,	1500},
				{	0,	0,		0,		250,	400,	1000,	1350},
				{	0,	0,		0,		0,		250,	850,	1300},
				{	0,	0,		0,		0,		0,		600,	1150},
				{	0,	0,		0,		0,		0,		0,		 500},
			};
	
	public static int[][] dist = new int[][]
			{
				{	0,	6,	13,	18,	23,	43,	58},
				{	0,	0,	7,	12,	17,	37,	52},
				{	0,	0,	0,	5,	10,	30,	45},
				{	0,	0,	0,	0,	5,	25,	40},
				{	0,	0,	0,	0,	0,	20,	35},
				{	0,	0,	0,	0,	0,	0,	15},
			};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			final int start = sc.nextInt() - 1;
			
			if(start == -1){
				break;
			}
			
			final int start_h = sc.nextInt();
			final int start_m = sc.nextInt();
			final int start_t = start_h * 60 + start_m;
			
			final int end = sc.nextInt() - 1;
			final int end_h = sc.nextInt();
			final int end_m = sc.nextInt();
			final int end_t = end_h * 60 + end_m;
			
			int cost = 0;
			if(!(start_t < (17 * 60 + 30) && end_t > (19 * 60 + 30))){
				if(dist[start][end] <= 40){
					cost = (int) (Math.ceil((money[start][end] / (2.0 * 50)))) * 50;
				}else{
					cost = money[start][end];
				}
			}else{
				cost = money[start][end];
			}
			
			System.out.println(cost);
		}
		
	}

}
