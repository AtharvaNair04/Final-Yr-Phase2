import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

import javax.print.attribute.standard.MediaSize.Other;
import javax.xml.bind.ParseConversionEvent;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		final int n = sc.nextInt();
		
		int number = -1;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < n; i++){
			boolean flag = true;
			boolean all_make = true;
			
			char[] input = sc.next().toCharArray();
			
			//System.out.println(Arrays.toString(input));
			
			for(int j = 0; j < n; j++){
				if(input[j] == 'x'){
					flag = false;
				}else if(input[j] == 'o'){
					all_make = false;
				}
			}
			
			if(flag && number == -1){
				number = i;
			}
			
			if(!all_make){
				list.add(i);
			}
			
		}
		
		for(int i = 0; i < 1000; i++){
			System.out.println(number == -1 ? list.get((int) (XRandom.XorShift(0, list.size()))) + 1 : number + 1);
			System.out.flush();
			
			final int ai = sc.nextInt();
		}
	}
	
	
	public static class XRandom {
		static long y = System.currentTimeMillis();
		static long z = System.currentTimeMillis();
		static long x = System.currentTimeMillis();
		static long w = System.currentTimeMillis();

		static int XorShift(int min, int max) {
			int t;
			t = (int) (x ^ (x << 11));
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >> 19)) ^ (t ^ (t >> 8));
			return (int) ((min + w) % max);
		}
	}
}
