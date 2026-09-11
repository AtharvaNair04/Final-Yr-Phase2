import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        int i,j,dice,dice1[]=new int[6],dice2[]=new int[6],f=0,swap;
        for(i=0;i<6;i++) {
        	dice1[i]=scan.nextInt();
        }
        for(i=0;i<6;i++) {
        	dice2[i]=scan.nextInt();
        }
        for(i=0;i<=6;i++) {
        	if(dice1[0]==dice2[0] && dice1[5]==dice2[5]) {
    			for(j=0;j<4;j++) {
    				if(dice1[1]==dice2[1] && dice1[2]==dice2[2] && dice1[3]==dice2[3] && dice1[4]==dice2[4]) {
    					f=1;
    					break;
    				}
    				swap=dice2[1];
            		dice2[1]=dice2[2];
            		dice2[2]=dice2[4];
            		dice2[4]=dice2[3];
            		dice2[3]=swap;
    			}
    		}
        	if(i%2==0) {
        		swap=dice2[0];
        		dice2[0]=dice2[1];
        		dice2[1]=dice2[5];
        		dice2[5]=dice2[4];
        		dice2[4]=swap;
        	}

        	else {
        		swap=dice2[0];
        		dice2[0]=dice2[3];
        		dice2[3]=dice2[5];
        		dice2[5]=dice2[2];
        		dice2[2]=swap;
        	}
        	if(f==1) {
        		break;
        	}
        }
        if(f==1) {
        	System.out.println("Yes");
        }
        else {
        	System.out.println("No");
        }
    }
}
