import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    Robot r;
    public int w, h, t;
    String com;

    void run(){
	while(sc.hasNext()){
	    w = sc.nextInt();
	    h = sc.nextInt();
	    if(w==0 && h==0) break;

	    r = new Robot();
	    com = sc.next();
	    while(!com.equals("STOP")){

		if(com.equals("FORWARD"))
		    r.forward(sc.nextInt());
      
		else if(com.equals("BACKWARD"))
		    r.forward((sc.nextInt())*(-1));

		else if(com.equals("RIGHT"))
		    r.turnR();

		else if(com.equals("LEFT"))
		    r.turnL();

		com = sc.next();
	    }
	    System.out.println(r.getX()+" "+r.getY());
	}
    }

    class Robot{
	int x, y;
	int d; // 0=f=(y+), 1=r=(x+), 2=b=(y-), 3=l=(x-)
	Robot(){
	    x = 1;
	    y = 1;
	    d = 0;
	}

	void forward(int g){
	    if(d==0) y = y+g>h ? h : y+g<1 ? 1 : y+g;  
	    if(d==1) x = x+g>w ? w : x+g<1 ? 1 : x+g;  
	    if(d==2) y = y-g>h ? h : y-g<1 ? 1 : y-g;	  
	    if(d==3) x = x-g>w ? w : x-g<1 ? 1 : x-g;  
	}

	int getX(){ return x; }
	int getY(){ return y; }
	
	void turnR(){ d = (d+1)%4; }
	void turnL(){ d = (d+4-1)%4; }
    }
    
}
