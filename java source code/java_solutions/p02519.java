import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.math.BigInteger;
import java.io.*;

class Stack{
    int s[]=new int[20010];
    int mins[]=new int[20010];
    int maxs[]=new int[20010];
    int p=0,maxp=0,minp=0;
    public int size(){return p;}
    public boolean empty(){return p==0;}
    public void push(int v){
	s[p++]=v;
	if (minp == 0 || mins[minp-1] > v)mins[minp++]=v;
	if (maxp == 0 || maxs[maxp-1] < v)maxs[maxp++]=v;
    }
    public int pop(){
	int ret=s[--p];
	if (mins[minp-1] == ret)minp--;
	if (maxs[maxp-1] == ret)maxp--;
	return ret;
    }
    public int getIthMin(int i){
	return mins[minp-i];
    }
    public int getIthMax(int i){return maxs[maxp-i];}
}

class Deque{
    int d[]=new int[20010];
    int mind[]=new int[20010];
    int maxd[]=new int[20010];
    int b=0,e=0,maxb=0,maxe=0,minb=0,mine=0;
    public int size(){return e-b;}
    public boolean empty(){return b==e;}
    public void push(int v){
	d[e++]=v;
	while(maxb < maxe && maxd[maxe-1] < v)maxe--;
	maxd[maxe++]=v;
	while(minb < mine && mind[mine-1] > v)mine--;
	mind[mine++]=v;
    }
    public int pop(){
	int ret=d[b++];
	if (mind[minb] == ret)minb++;
	if (maxd[maxb] == ret)maxb++;
	return ret;
    }
    public int getIthMin(int i){return mind[minb+i-1];}
    public int getIthMax(int i){return maxd[maxb+i-1];}
}

class Fimo{
    Stack S;
    Deque D;
    Fimo(){
	S=new Stack();
	D=new Deque();
    }
    public void push(int v){
	if (S.empty() && D.empty()){
	    S.push(v);
	}else if (S.size() > D.size()){
	    D.push(v);
	}else if (S.size() == D.size()){
	    int tmp=D.pop();
	    S.push(tmp);
	    D.push(v);
	}
    }
    public int getMin(int t,int i){
	if (t == 0)return S.getIthMin(i);
	else return D.getIthMin(i);
    }
    public int getMax(int t,int i){
	if (t == 0)return S.getIthMax(i);
	else return D.getIthMax(i);
    }
    public int pop(){
	int ret=-1;
	if (D.empty()){
	    ret=S.pop();
	}else if (S.size() > D.size()){
	    ret=S.pop();
	}else if (S.size() == D.size()){
	    int tmp=D.pop();
	    ret=S.pop();
	    S.push(tmp);
	}
	return ret;
    }
}

class Main{
    public static void main(String []args){
	Scanner in=new Scanner(System.in);
	//Scanner in=new Scanner();
	//System.setOut(new PrintStream(new BufferedOutputStream(System.out)));
	int q;
	while(true){
	    q=in.nextInt();
	    if (q == 0)break;
	    Fimo a=new Fimo();
	    for(int i=0;i<q;i++){
		int val,tmp;
		tmp=in.nextInt();
		switch(tmp){
		case 0:
		    val=in.nextInt();
		    //System.out.print(0+" "+val+"\n");
		    a.push(val);
		    break;
		case 1:
		    System.out.print(a.pop()+"\n");
		    break;
		case 2:
		    System.out.print(a.getMin(0,1)+"\n");
		    break;
		case 3:
		    System.out.print(a.getMin(1,1)+"\n");
		    break;
		case 4:
		    val=in.nextInt();
		    System.out.print(a.getMin(0,val)+"\n");
		    break;
		case 5:
		    val=in.nextInt();
		    System.out.print(a.getMin(1,val)+"\n");
		    break;
		case 6:
		    System.out.print(a.getMax(0,1)+"\n");
		    break;
		case 7:
		    System.out.print(a.getMax(1,1)+"\n");
		    break;
		case 8:
		    val=in.nextInt();
		    System.out.print(a.getMax(0,val)+"\n");
		    break;
		case 9:
		    val=in.nextInt();
		    System.out.print(a.getMax(1,val)+"\n");
		    break;
		default:
		    break;
		}
	    }
	    System.out.print("end\n");
	}
    }
}
    
/*
class Scanner {
    int nextInt() {
        try {
            int c = System.in.read();
            if (c == -1)
                return c;
            while (c != '-' && (c < '0' || '9' < c)) {
                c = System.in.read();
                if (c == -1)
                    return c;
            }
            if (c == '-')
                return -nextInt();
            int res = 0;
            do {
                res *= 10;
                res += c - '0';
                c = System.in.read();
            } while ('0' <= c && c <= '9');
            return res;
        } catch (Exception e) {
            return -1;
        }
    }
 
    long nextLong() {
        try {
            int c = System.in.read();
            if(c==-1)return -1;
            while (c != '-' && (c < '0' || '9' < c)){
                c = System.in.read();
                if(c==-1)return -1;
            }
            if (c == '-')
                return -nextLong();
            long res = 0;
            do {
                res *= 10;
                res += c - '0';
                c = System.in.read();
            } while ('0' <= c && c <= '9');
            return res;
        } catch (Exception e) {
            return -1;
        }
    }
 
    double nextDouble() {
        return Double.parseDouble(next());
    }
 
    String next() {
        try {
            StringBuilder res = new StringBuilder("");
            int c = System.in.read();
            while (Character.isWhitespace(c))
                c = System.in.read();
            do {
                res.append((char) c);
            } while (!Character.isWhitespace(c = System.in.read()));
            return res.toString();
        } catch (Exception e) {
            return null;
        }
    }
 
    String nextLine() {
        try {
            StringBuilder res = new StringBuilder("");
            int c = System.in.read();
            while (c == '\r' || c == '\n')
                c = System.in.read();
            do {
                res.append((char) c);
                c = System.in.read();
            } while (c != '\r' && c != '\n');
            return res.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
*/
