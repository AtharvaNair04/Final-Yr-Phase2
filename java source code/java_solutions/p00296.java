import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		int n = stdIn.nextInt();
		int m = stdIn.nextInt();
		int q = stdIn.nextInt();
		LinkedList list = new LinkedList(n);
		for(int i = 0; i < m; i++) {
			int a = stdIn.nextInt();
			if(a%2 == 0) {
				for(int j = 0; j < a; j++) {
					
					list.now = list.now.next;
				}
			}
			else {
				for(int j = 0; j < a; j++) {
					
					list.now = list.now.prev;
				}
			}
			
			list.remove();
			
		}
		for(int i = 0; i < q; i++) {
			int qx = stdIn.nextInt();
			if(list.isntalive[qx]) {
				System.out.println("0");
			}
			else {
				System.out.println("1");
			}
		}
	}
	
	
}
class LinkedList {
	boolean[] isntalive;
	Data now;
	
	LinkedList(int n) {
		isntalive = new boolean[n];
		now = new Data();
		Data tmp = now;
		now.id = 0;
		now.next = new Data();
		now.next.id = 1;
		now.next.next = now;
		now.next.prev = now;
		now.prev = now.next;
		now = now.next;
		for(int i = 2; i < n; i++) {
			add();
		}
		now = tmp;
	}
	
	void add() {
		Data tmp = now.next;
		now.next = new Data();
		
		now.next.id = now.id+1;
		now.next.next = tmp;
		now.next.next.prev = now.next;
		now.next.prev = now;
		
		now = now.next;
	}
	void remove() {
		isntalive[now.id] = true;
		now.prev.next = now.next;
		now.next.prev = now.prev;
		now = now.next;
	}
}

class Data {
	Data prev;
	Data next;
	int id;
	
}
