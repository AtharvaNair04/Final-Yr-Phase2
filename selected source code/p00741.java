import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int w = kbd.nextInt();
	    int h = kbd.nextInt();
	    if(w!=0 && h!=0){
		solve(w, h);
	    }
	}
    }

    static void solve(int w, int h){
	int[][] field = new int[w][h];
	int i, j;
	for(i=0; i<h; i++){
	    for(j=0; j<w; j++){
		field[j][i] = kbd.nextInt();  // 0:海  1:陸
		//System.out.print(field[j][i]);
	    }
	    //System.out.println();
	}
	
	Stack[] s = new Stack[2];
	s[0] = new Stack(1000000);
	s[1] = new Stack(1000000);
	int count = 0;
	for(i=0; i<w; i++){
	    for(j=0; j<h; j++){
		int x = i;
		int y = j;
		if(field[x][y]==1) {
		    count++;
		    //System.out.println(x+" "+y);
		    search(s, field, x, y, w, h);
		    while(! (s[0].isEmpty() && s[1].isEmpty())){
			//System.out.println("＠"+x+" "+y);
			x = (Integer)s[0].pop();
			y = (Integer)s[1].pop();
			if(field[x][y]==1){
			    field[x][y] = -1; //通過済
			    search(s, field, x, y, w, h);
			}
		    }
		}
	    }
	}
	System.out.println(count);
    }

    static void search(Stack[] s, int[][]field, int x, int y, int w, int h){
	if(x+1<w && (field[x+1][y]==1)){      //右
	    if(!isExist(s, x+1, y)){
		s[0].push(x+1);
		s[1].push(y);
	    }
	}
	if(x-1>=0 && field[x-1][y]==1){   //左
	    if(!isExist(s, x-1, y)){
		s[0].push(x-1);
		s[1].push(y);
	    }
	}
	if(y+1<h && field[x][y+1]==1){    //上
	    if(!isExist(s, x, y+1)){
		s[0].push(x);
		s[1].push(y+1);
	    }
	}
	if(y-1>=0 && field[x][y-1]==1){    //下
	    if(!isExist(s, x, y-1)){
		s[0].push(x);
		s[1].push(y-1);
	    }
	}
	if(x+1<w && y+1<h && field[x+1][y+1]==1) { //右上
	    if(!isExist(s, x+1, y+1)){
		s[0].push(x+1);
		s[1].push(y+1);
	    }
	}
	if(x+1<w && y-1>=0 && field[x+1][y-1]==1) { //右下
	    if(!isExist(s, x+1, y-1)){
		s[0].push(x+1);
		s[1].push(y-1);
	    }
	}
	if(x-1>=0 && y+1<h && field[x-1][y+1]==1){  //左上
	    if(!isExist(s, x-1, y+1)){
		s[0].push(x-1);
		s[1].push(y+1);
	    }
	}
	if(x-1>=0 && y-1>=0 && field[x-1][y-1]==1){ //左下
	    if(!isExist(s, x-1, y-1)){
		s[0].push(x-1);
		s[1].push(y-1);
	    }
	}
    }

    static boolean isExist(Stack[] s, int x, int y){
	if(s[0].aru(x)!=-1 && s[0].aru(x) == s[1].aru(y)) return true;
	return false;
    }
}

class Stack{
    Object[] stack;
    int sp;
    public Stack(int n){
	stack = new Object[n];
	sp = 0;
    }
    public void push(Object x){
	stack[sp++] = x;
    }
    public Object pop(){
	return stack[--sp];
    }
    public boolean isEmpty(){
	return sp == 0;
    }
    public int aru(Object x){
	int i;
	for(i=0; stack[i]!=null; i++){
	    if(stack[i]==x) return i;
	}
	return -1;
    }
}
