import java.util.Scanner;

//Hyper Rock-Scissors-Paper
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		String[] s = {"Rock", "Fire", "Scissors", "Snake", "Human", "Tree", "Wolf", "Sponge", "Paper", "Air", "Water", "Dragon", "Devil", "Lightning", "Gun"};
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			boolean[] u = new boolean[15];
			for(int i=0;i<n;i++){
				String t = sc.next();
				for(int j=0;j<15;j++)if(s[j].equals(t))u[j]=true;
			}
			boolean[] win = new boolean[15], lose = new boolean[15];
			for(int i=0;i<15;i++){
				if(!u[i])continue;
				for(int j=i+1;j<=i+7;j++){
					int k = j%15;
					if(!u[k])continue;
					lose[k] = true;
					win[i] = true;
				}
			}
			String res = "Draw";
			for(int i=0;i<15;i++)if(u[i]&&!lose[i]&&win[i])res=s[i];
			System.out.println(res);
		}
	}
	public static void main(String[] args) {
		new Main().run();
	}
}
