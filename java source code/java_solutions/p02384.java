import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] num = new int[6];

        for(int i = 0; i < num.length; i++) {
        	num[i] = sc.nextInt();
        }
        Dice dice = new Dice(num);

        int count = sc.nextInt();
        for(int i = 0; i < count; i++) {
        	int top = sc.nextInt();
            int front = sc.nextInt();
            dice.setTop(top);
            dice.setFront(front);
            System.out.println(dice.getRight());
        }
    }
}

class Dice {
	private int top;
    private int front;
    private int right;
    private int left;
    private int back;
    private int bottom;

    Dice(int[] num) {
    	top = num[0];
    	front = num[1];
    	right = num[2];
    	left = num[3];
    	back = num[4];
    	bottom = num[5];
    }

    void change(char dir) {
    	switch(dir) {
    	case 'N' : moveN(); break;
    	case 'S' : moveS(); break;
    	case 'E' : moveE(); break;
    	case 'W' : moveW(); break;
    	}
    }

    void setTop(int top) {
    	if(top != this.top) {
    		if(top == this.front) {
    			moveN();
    		} else if(top == this.right) {
    			moveW();
    		} else if(top == this.left) {
    			moveE();
    		} else if(top == this.back) {
    			moveS();
    		} else if(top == this.bottom) {
    			moveS();
    			moveS();
    		}
    	}
    }

    void setFront(int front) {
    	while(true) {
    		if(front == this.front) {
    			break;
    		}
    		moveClockwise();
    	}
    }

    void moveN() {
    	int temp = top;
    	top = front;
    	front = bottom;
    	bottom = back;
    	back = temp;
    }
    void moveS() {
    	int temp = top;
    	top = back;
    	back = bottom;
    	bottom = front;
    	front = temp;
    }
    void moveE(){
        int temp = top;
        top = left;
        left = bottom;
        bottom = right;
        right = temp;
    }
    void moveW(){
        int temp = top;
        top = right;
        right = bottom;
        bottom = left;
        left = temp;
    }
    void moveClockwise() {
    	int temp = front;
    	front = right;
    	right = back;
    	back = left;
    	left = temp;
    }

    int getTop() {
    	return top;
    }
    int getRight() {
    	return right;
    }
}
