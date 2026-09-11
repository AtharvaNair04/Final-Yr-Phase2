import java.util.Scanner;

public class Main{
	public static void main(String[] args) {
		// TODO ?????????????????????????????????????????????
		new Main().start();
	}
	void start(){
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		String[] ss = s.split(" ");
		Stack stack = new Stack();
		for(int i = 0; i < ss.length; i++){
			if(ss[i].equals("+")){
				int a = stack.pop();
				int b = stack.pop();
				stack.push(a + b);
			}else if(ss[i].equals("-")){
				int a = stack.pop();
				int b = stack.pop();
				stack.push(b - a);
			}else if(ss[i].equals("*")){
				int a = stack.pop();
				int b = stack.pop();
				stack.push(a * b);
			}else{
				stack.push(Integer.parseInt(ss[i]));
			}
		}
		System.out.println(stack.pop());
		in.close();
	}
}

class Stack {
	int[] data;
	int top = 0;
	Stack(){
		data = new int[101];
		top = 0;
	}
	void push(int x){
		data[top++] = x;
	}
	int pop(){
		return data[--top];
	}
	boolean isEmpty(){
		return top == 0;
	}
	boolean isFull(){
		return top == data.length;
	}
}
