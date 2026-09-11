import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		int n = sc.nextInt();
		sc.nextLine();
		for(int i=0; i<n;i++){
			String order = sc.nextLine();
			String message = sc.nextLine();
			calc(order, message);
		}
	}
	public void calc(String order, String message){
		if(order.equals("")) System.out.println(message);
		else{
			String person = order.substring(order.length() - 1);
			if(person.equals("J")){
				String me = message.substring(message.length()-1) + message.substring(0, message.length() - 1);
				message = me;
			}
			else if(person.equals("C")){
				String me = message.substring(1) + message.substring(0, 1);
				message = me;
			}
			else if(person.equals("E")){
				int half = message.length() / 2;
				String me = message.substring(message.length()-half) 
						+ message.substring(half, message.length()-half) 
						+ message.substring(0, half);
				message = me;
			}
			else if(person.equals("A")){
				String me = reverse(message, "");
				message = me;
			}
			else if(person.equals("P")){
				String me = addNum(message, -1);
				message = me;
			}
			else if(person.equals("M")){
				String me = addNum(message, 1);
				message = me;
			}
			calc(order.substring(0, order.length()-1), message);
		}
	}
	public String reverse(String s, String ans){
		if(s.equals("")) return ans;
		else{
			ans = ans + s.substring(s.length()-1);
			return reverse(s.substring(0, s.length()-1), ans);
		}
	}
	public String addNum(String message, int n){
		for(int i = 0; i < message.length(); i++){
			String c = message.substring(i, i+1);
			if(c.equals("0")) message = message.substring(0,i) + addN(0,n) + message.substring(i+1);
			else if(c.equals("1")) message = message.substring(0,i) + addN(1,n) + message.substring(i+1);
			else if(c.equals("2")) message = message.substring(0,i) + addN(2,n) + message.substring(i+1);
			else if(c.equals("3")) message = message.substring(0,i) + addN(3,n) + message.substring(i+1);
			else if(c.equals("4")) message = message.substring(0,i) + addN(4,n) + message.substring(i+1);
			else if(c.equals("5")) message = message.substring(0,i) + addN(5,n) + message.substring(i+1);
			else if(c.equals("6")) message = message.substring(0,i) + addN(6,n) + message.substring(i+1);
			else if(c.equals("7")) message = message.substring(0,i) + addN(7,n) + message.substring(i+1);
			else if(c.equals("8")) message = message.substring(0,i) + addN(8,n) + message.substring(i+1);
			else if(c.equals("9")) message = message.substring(0,i) + addN(9,n) + message.substring(i+1);
		}
		return message;
	}
	public String addN(int m, int n){
		int ans = m + n;
		if(ans == 10) ans = 0;
		if(ans == -1) ans = 9;
		return Integer.toString(ans);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
