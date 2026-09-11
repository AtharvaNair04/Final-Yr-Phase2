import java.util.*;
import java.math.*;
import java.awt.geom.*;
import java.io.*;
     
     
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int d = sc.nextInt();
        int x = sc.nextInt();
        PriorityQueue<DataA> queueA = new PriorityQueue<DataA>();
        PriorityQueue<DataB> queueB = new PriorityQueue<DataB>();
        for(int i = 0; i < x; i++) {
        	DataA xx = new DataA(sc.next().charAt(0),sc.nextInt());
        	if(xx.a == 'D')
            queueA.add(xx);
        }
        int y = sc.nextInt();
        for(int i = 0; i < y; i++) {
            String in = sc.next();
            DataB xx = new DataB(in.charAt(0),in.charAt(1),sc.nextInt());
            if(xx.b == 'D' && xx.c == 'D')
            queueB.add(xx);
        }
        int score = 0;
        while(!queueA.isEmpty() && !queueB.isEmpty() && d >= 1) {
            if(d != 1) {
            	DataA tmp = queueA.poll();
            	//System.out.println(tmp.p);
            	if((queueA.size() == 0 && queueB.peek().q >= tmp.p) || (!queueA.isEmpty() && queueB.peek().q >= queueA.peek().p + tmp.p)) {
	            	d -= 2; 
	            	//System.out.println(queueB.peek().q + " " + d );
	                score += queueB.poll().q;
	                queueA.add(tmp);
	                continue;
            	}
            	queueA.add(tmp);
            }

                d -= 1;
                //System.out.println(queueA.peek().p + " " + d);
                score += queueA.poll().p;
                continue;
        }
        if(queueA.isEmpty() && !queueB.isEmpty()) {
        	while(!queueB.isEmpty() && d >= 2) {
        		d -= 2;
        		//System.out.println(queueB.peek().q + " " + d);
        		score += queueB.poll().q;
        	}
        }
        else if(!queueA.isEmpty() && queueB.isEmpty()) {
        	while(!queueA.isEmpty() && d >= 1) {
        		d -= 1;
        		//System.out.println(queueA.peek().p + " " + d);
        		score += queueA.poll().p;
        		
        	}
        }
        
        System.out.println(score);

         
    }
    static class DataB implements Comparable<DataB> {
        char b;
        char c;
        int  q;
        @Override
        public int compareTo(DataB o) {
            return o.q - this.q;
        }
        DataB(char b, char c, int q) {
            this.b = b;
            this.c = c;
            this.q = q;
        }
         
    }
    static class DataA implements Comparable<DataA> {
        char a;
        int p;
        @Override
        public int compareTo(DataA o) {
            return o.p - this.p;
        }
        DataA(char a, int b) {
            this.a = a;
            this.p = b;
        }
         
         
    }
     
}
