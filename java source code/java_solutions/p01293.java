import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			String trump = sc.next();
			
			if(trump.equals("#")) break;
			else calc(trump);
		}
	}
	public void calc(String trump){
		String[][] cards = new String[4][13];
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 13; j++){
				cards[i][j] = sc.next();
			}
		}
		
		int led = 0;
		int nsPoint = 0;
		int ewPoint = 0;
		for(int i = 0; i < 13; i++){
			String[] turn = new String[]{cards[0][i], cards[1][i], cards[2][i], cards[3][i]};			
			String ledSuit = turn[led].substring(1, 2);
			
			int winner = win(turn, ledSuit, trump);
			
			if(winner == 0 || winner == 2) nsPoint++;
			else ewPoint++;
			
			led = winner;
		}
		
		if(nsPoint > ewPoint){
			int point = nsPoint - 6;
			System.out.println("NS " + point);
		}
		else{
			int point = ewPoint - 6;
			System.out.println("EW " + point);
		}
	}

	public int win(String[] turn, String ledSuit, String trump){
		int winner = 0;
		int winNum = getNum(turn[0].substring(0, 1));
		String winSuit = turn[0].substring(1,2);
		
		for(int i = 1; i < 4; i++){
			int turnNum = getNum(turn[i].substring(0, 1));
			String turnSuit = turn[i].substring(1, 2);
			
			if(turnSuit.equals(trump)){
				if(!winSuit.equals(trump)){
					winner = i;
					winNum = turnNum;
					winSuit = turnSuit;
				}
				else if(turnNum > winNum){
					winner = i;
					winNum = turnNum;
					winSuit = turnSuit;
				}
			}
			else if(turnSuit.equals(ledSuit)){
				if(!winSuit.equals(trump)){
					if(!winSuit.equals(ledSuit)){
						winner = i;
						winNum = turnNum;
						winSuit = turnSuit;
					}
					else if(turnNum > winNum){
						winner = i;
						winNum = turnNum;
						winSuit = turnSuit;
					}
				}
			}
		}
		return winner;
	}
	
	public int getNum(String card){
		if(card.equals("T")) return 10;
		else if(card.equals("J")) return 11;
		else if(card.equals("Q")) return 12;
		else if(card.equals("K")) return 13;
		else if(card.equals("A")) return 14;
		else return Integer.valueOf(card);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
