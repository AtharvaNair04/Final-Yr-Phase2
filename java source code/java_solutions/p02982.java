import java.util.*;

public class Main {
 	public static void main(String[] args){
    	Scanner s = new Scanner(System.in);
      	int n = s.nextInt();
      	int m = s.nextInt();
      	int[][] arr = new int[n][m];
      	for(int i = 0; i < n; i++) {
         	for(int j = 0; j < m; j++) {
             	arr[i][j] = s.nextInt();
            }
        }
      
      	int count = 0;
      	for(int i = 0; i < n; i++ ) {
         	for(int j = i + 1; j < n; j++) {
              	int sum = 0;
            	for(int k = 0; k < m; k++ ){
                 	 sum += Math.pow(arr[j][k] - arr[i][k], 2);
                }
              	int sqrt = (int)(Math.sqrt(sum));
              	if(sqrt * sqrt == sum) {
                	count++;
                }
            }
        }
      System.out.println(count);
    }
}
