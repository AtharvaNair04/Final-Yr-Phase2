import java.util.*;

public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args) {
	
	int n = kbd.nextInt();       //データセット数
	while(n>0) {
	    solve();
	    n--;
	}
    }

    static void solve() {
	int y = kbd.nextInt();   //誕生年
	int m = kbd.nextInt();   //誕生月
	int d = kbd.nextInt();   //誕生日
	int answer;
	answer = calc(y, m, d);
	System.out.println(answer);
    }

    static int calc(int y, int m, int d) {
	int total = 0;  //ミレニアムまでの日数
	int three = 0;  //3の倍数の年数
	int notT = 0;   //3の倍数でない年数
	int D;          //誕生年1月1日から誕生日までの日数
	for(int i=y; i<1000; i++) {
	    if(i%3 == 0) three++;
	    else         notT++;
	}
	total = notT*195 + three*200;   //仮total(誕生年1月1日〜ミレニアム)
	D = days(y, m, d);
	total = total - D + 1;          //誕生日も含めた正式なtotal
	return total;
    }

    static int days(int y, int m, int d) {
	int D = 0;
	if(y%3 == 0)
	    for(int j=1; j<m; j++) D += 20;
	else {
	    for(int j=1; j<m; j++) {
		if(j%2 == 0) D += 19;
		else         D += 20;
	    }
	}
	D += d;
	return D;
    }
}
