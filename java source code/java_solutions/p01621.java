import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {
	static int S, N, T;
	static String WEEKDAY, TIME;
	static int P, M;
	
	static int[] timelist = {0, 6*60, 18*60};
	
	static private void solve()
	{
		// Weekdayの数字化
				int Weekday = 0;
				     if(WEEKDAY.equals("All")) Weekday = -1;
				else if(WEEKDAY.equals("Sun")) Weekday = 0;
				else if(WEEKDAY.equals("Mon")) Weekday = 1;
				else if(WEEKDAY.equals("Tue")) Weekday = 2;
				else if(WEEKDAY.equals("Wed")) Weekday = 3;
				else if(WEEKDAY.equals("Thu")) Weekday = 4;
				else if(WEEKDAY.equals("Fri")) Weekday = 5;
				else if(WEEKDAY.equals("Sat")) Weekday = 6;
		
		// OK時間表作り
		int[] goodtable = new int[24*60*7];
		for (int i = 0; i < 24*60*7; i++) {
			goodtable[i] = 0;
		}
		
		for (int w = 0; w < 7; w++) {
			if(Weekday != -1 && Weekday != w) continue;
			for (int t = 0; t < 24*60; t++) {
				if(( TIME.equals("All")   ||
					      ( TIME.equals("Day")   && (6*60 <= t && t < 18*60) ) ||
					      ( TIME.equals("Night") && (18*60 <= t || t < 6*60) )))
					      {
								goodtable[w*24*60 + t] = 1;
					      }
			}
		}
		
		for (int time = 1; time < 24*60*7; time++) {
			goodtable[time] += goodtable[time-1];
		}
		
		double maxans = 0;
		for (int time = 0; time < 24*60*7; time++) {
			double ans = 0;
			double tmp = 1;
			for (int m = 0; m < M; m++) {
				int count = 0;
				
				if(time + S >= 24*60*7)
				{
					count += goodtable[24*60*7-1] - goodtable[time-1];
					count += goodtable[time+S-24*60*7];
				}
				else {
					if(time == 0)
						count += goodtable[time+S];
					else {
						count += goodtable[time+S] - goodtable[time-1];
					}
				}
				
				if(count == S+1)
				{
					for (int j = 0; j < N; j++) {
						ans += tmp / (double)P;
						tmp *= (double)(P-1) / (double)P;
					}
				}
				time += T;
				if(time >= 24*60*7) time -= 24*60*7;
			}
			
			maxans = Math.max(maxans, ans);
		}
		
//		for (int w = 0; w < 7; w++) {
//			int week = w;
//			//for (int t = 0; t < 3; t++) {
//				//int time = timelist[t];
//			for (int time = 0; time < 24*60; time++) {
//				
//				
//				// ステージ数分回す
//				double ans = 0;
//				double tmp = 1;
//				for (int m = 0; m < M; m++) {
//					boolean ok = true;
//					for (int i = 0; i < T; i++) {
//						if(0 <= i && i <= S)
//						{
//							if(Weekday == -1 || week == Weekday)
//							{
//								if ( !( TIME.equals("All")   ||
//								      ( TIME.equals("Day")   && (6*60 <= time && time < 18*60) ) ||
//								      ( TIME.equals("Night") && (18*60 <= time || time < 6*60) )    ) ) 
//								{
//									ok = false;
//								}
//							}
//							else {
//								ok = false;
//							}
//						}
//						
//						if(i == S)
//						{
//							if (ok) {
//								for (int j = 0; j < N; j++) {
//									ans += tmp / (double)P;
//									tmp *= (double)(P-1) / (double)P;
//								}
//							}
//						}
//						
//						time++;
//						if(time == 24*60)
//						{
//							time = 0;
//							week++;
//							if(week == 7) week = 0;
//						}
//					}
//				}
//				
//				maxans = Math.max(maxans, ans);
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		
		if(maxans > 1.0) maxans = 1.0;
		
		System.out.printf("%.010f\n", maxans);
	}
	
	static public void main(String[] args)
	{
		Scanner sca = new Scanner(System.in);
		
		while(true)
		{
			
			S = sca.nextInt();
			N = sca.nextInt();
			T = sca.nextInt();
			WEEKDAY = sca.next();
			TIME = sca.next();
			P = sca.nextInt();
			M = sca.nextInt();
			
			if(S == 0) break;
			
			solve();
		}
	}
}
