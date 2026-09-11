import java.util.Scanner;

class Main {
	 public static void main(String[] args){
	        Scanner scan = new Scanner(System.in);
	        
	        int val[] = {1, 5, 4, 3, 2,6};
	    	
	    	int n;
	    	String dir;
	    	
	    	while (true) {
		    	Dice dice = new Dice(val);
	    		
	    		n = scan.nextInt();
	    		if (n == 0) break;
	    		
	    		for (int i = 0; i < n; i++) {
	    			dir = scan.next();
	    			if (dir.equals("north")) dice.noth();
	    			else if (dir.equals("east")) dice.east();
	    			else if (dir.equals("west")) dice.west();
	    			else if (dir.equals("south")) dice.south();
	    		}
	    	System.out.println(dice.getDice(0));
	    	}
	    }
}

class Dice {
    private int[] dice = new int[6];

    Dice() {
    	
    };

    Dice(int[] val) {
    	for (int i = 0; i < this.dice.length; i++) {
			this.dice[i] = val[i];
		}
    }

    void setDice(int i, int val) {
    	this.dice[i] = val;
    }

    int getDice(int i) {
    	return this.dice[i];
    }

    int[] getDice() {
    	return this.dice;
    }

	int[] noth() {
		swap(0, 1, 5, 4);
		return this.dice;
	}

	int[] east() {
		swap(0, 3, 5, 2);
		return this.dice;
	}

	int[] west() {
		swap(0, 2, 5, 3);
		return this.dice;
	}

	int[] south() {
		swap(0, 4, 5, 1);
		return this.dice;
	}

	int[] right() {
		swap(1, 2, 4, 3);
		return this.dice;
	}

	int[] left() {
		swap(1, 3, 4, 2);
		return this.dice;
	}

	void swap(int n1, int n2, int n3, int n4) {
		int swap = this.dice[n1];
		this.dice[n1] = this.dice[n2];
		this.dice[n2] = this.dice[n3];
		this.dice[n3] = this.dice[n4];
		this.dice[n4] = swap;
	}

	void dicePrint() {
		int i;
		for (i = 0; i < this.dice.length - 1; i++) {
			System.out.print(this.dice[i] + ", ");
		}
	}
}
