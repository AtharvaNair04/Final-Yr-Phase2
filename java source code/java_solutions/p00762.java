import java.util.Scanner;

public class Main {
	public static void main(String args[]){	
		Scanner scan = new Scanner(System.in);
		
		int n;
		int[][][] ans;
		int t, f;
		
		int posX, posY, minX, maxX, minY, maxY, move, max;
		int[] xi = {1, 2, 3, 4, 5, 6};
		
		while(true) {
			n = scan.nextInt();
			if(n == 0)break;

			minX = 10;
			maxX = 10;
			minY = 10;
			maxY = 10;
			
			int[] out = new int[6];//?§£???????????¨
			
			ans = new int [2][21][21];
			//???0 : ????????? 1: ??????, x??§?¨?, y??§?¨?
			
			for(int i = 0; i < n; i++) {
				posX = 10;
				posY = 10;
				
				t = scan.nextInt();
				f = scan.nextInt();
				
				//????????????????????¨?????¢???????????????
				
				if(7 - t == xi[0]) {
					xi = Dice.north(xi);
					xi = Dice.north(xi);
					
				} else if(xi[0] != t) {
					xi = Dice.east(xi);
					
					while(true) {
						xi = Dice.north(xi);
						
						if(xi[0] == t) break;
					}
				}
				
				while(xi[1] != f) {
					xi = Dice.left(xi);
				}
				
				//???????????§????????????
				
				//???????????????????????????????????°?????????????¨????
				if(ans[0][10][10] == 0) {
					ans[0][10][10]++;
					ans[1][10][10] = t;
					
				} else {					
					while(true) {
						move = 0;
						max = 4;
						
						//?§?????????????????????????????????\????§??????§?????????????????????????????§??????????????????????§????
						//???????§??????§?????????
						if(xi[1] >= max && ans[0][posX][posY] > ans[0][posX][posY + 1]) {
							max = xi[1];
							move = 1;
						}
						
						//???????§??????§?????????
						if(xi[2] >= max && ans[0][posX][posY] > ans[0][posX + 1][posY]) {
							max = xi[2];
							move = 2;
						}
						
						//???????§??????§?????????
						if(xi[3] >= max && ans[0][posX][posY] > ans[0][posX - 1][posY]) {
							max = xi[3];
							move = 3;
						}
						
						//???????§??????§?????????
						if(xi[4] >= max && ans[0][posX][posY] > ans[0][posX][posY - 1]) {
							max = xi[4];
							move = 4;
						}
						
						//?§??????§??????????????°????????????????¨????????????????
						if(move == 0) {
							ans[0][posX][posY]++;
							ans[1][posX][posY] = xi[0];
							
							if(posY > maxY) {
								maxY = posY;
								
							} else if(posY < minY) {
								minY = posY;
							}
							
							if(posX > maxX) {
								maxX = posX;
								
							} else if(posX < minX) {
								minX = posX;
							}

							break;
						}
						
						//????????????????§???????1:???, 2:???, 3:???, 4:??????
						switch(move) {
							case 1:
								posY++;
								xi = Dice.south(xi);
								break;

							case 2:
								posX++;
								xi = Dice.east(xi);
								break;
																
							case 3:
								posX--;
								xi = Dice.west(xi);
								break;
								
							case 4:
								posY--;
								xi = Dice.north(xi);
								break;
						}
						//?§???????????????§
					}
				}
			}
			
			for(int i = minY; i <= maxY; i++) {
				for(int j = minX; j <= maxX; j++) {
					if(ans[1][j][i] > 0) {
						out[ans[1][j][i] - 1]++;
					}
				}
			}
			
			for(int i = 0; i < 6; i++) {
				System.out.print(out[i]);
				
				if(i != 5) {
					System.out.print(" ");
					
				} else {
					System.out.println();
				}
			}
		}
		
		scan.close();
	}
}

class Dice {
	
	
/*               North
 *               _____
 *              /[0]_/|
 * West   [3]-> |[1] ||<-[2]   East
 *              |____|/
 *      
 *               South
 *               
 *    top   -> [0]
 *    front -> [1]
 */
	

    public static int[] north(int arrayDice[]){
        int swap = arrayDice[0];
        arrayDice[0] = arrayDice[1];
        arrayDice[1] = arrayDice[5];
        arrayDice[5] = arrayDice[4];
        arrayDice[4] = swap;  
        
        return arrayDice;
    }
     
    public static int[] south(int arrayDice[]){
        int swap = arrayDice[0];
        arrayDice[0] = arrayDice[4];
        arrayDice[4] = arrayDice[5];
        arrayDice[5] = arrayDice[1];
        arrayDice[1] = swap;
        
        return arrayDice;
    }
     
    public static int[] west(int arrayDice[]){
        int swap = arrayDice[0];
        arrayDice[0] = arrayDice[2];
        arrayDice[2] = arrayDice[5];
        arrayDice[5] = arrayDice[3];
        arrayDice[3] = swap;
        
        return arrayDice;
    }
     
    public static int[] east(int arrayDice[]){
        int swap = arrayDice[0];
        arrayDice[0] = arrayDice[3];
        arrayDice[3] = arrayDice[5];
        arrayDice[5] = arrayDice[2];
        arrayDice[2] = swap;
        
        return arrayDice;
    }
     
    public static int[] right(int arrayDice[]){
        int swap = arrayDice[1];
        arrayDice[1] = arrayDice[3];
        arrayDice[3] = arrayDice[4];
        arrayDice[4] = arrayDice[2];
        arrayDice[2] = swap;
        
        return arrayDice;
    }
     
    public static int[] left(int arrayDice[]){
        int swap = arrayDice[1];
        arrayDice[1] = arrayDice[2];
        arrayDice[2] = arrayDice[4];
        arrayDice[4] = arrayDice[3];
        arrayDice[3] = swap;
        
        return arrayDice;
    }
    
    //5020?
    public static int addValue(int sumValue, int arrayNumber, int arrayDice[]){
    	
    	int value = sumValue + arrayDice[arrayNumber];
    	
    	return value;
    }
    
    
}
