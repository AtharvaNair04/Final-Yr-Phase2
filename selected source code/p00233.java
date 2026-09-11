import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;
import java.math.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			long ss = sc.nextLong();
			if(ss == 0) break;
			boolean minus = false;
			if(ss < 0) minus = true;
			char[] in = String.valueOf(ss).toCharArray();
			int[] ar0 = new int[64];
			int[] ar1 = new int[64];
			for(int i = 0; i < in.length; i++) {
				if(in[in.length - i - 1] == '-') continue;
				ar0[i] = in[in.length -1 - i] - '0';
			}
			if(!minus)
			for(int i = 0; i < 64; i++) {
				if(ar0[i] == 0) continue;
				if(i % 2 == 0) {
					if(ar0[i] < 10) {
						ar1[i] += ar0[i];
					}
					else {
						ar0[i+1] += ar0[i]/10;
						ar1[i] += ar0[i] % 10;
					}
				}
				else {
					if(ar0[i] < 10) {
						ar1[i] += 10 - ar0[i] % 10;
						ar0[i+1] += 1;
					}
					else {
						ar0[i+1] += ar0[i] / 10;
						if(ar0[i] % 10 != 0)
						ar1[i] += 10 - ar0[i] % 10;
						if(ar0[i] % 10 != 0)
						ar0[i+1] += 1;
					}
				}
			}
			else {
				for(int i = 0; i < 64; i++) {
					if(ar0[i] == 0) continue;
					if(i % 2 != 0) {
						if(ar0[i] < 10) {
							ar1[i] += ar0[i];
						}
						else {
							ar0[i+1] += ar0[i]/10;
							ar1[i] += ar0[i] % 10;
						}
					}
					else {
						if(ar0[i] < 10) {
							ar1[i] += 10 - ar0[i] % 10;
							ar0[i+1] += 1;
						}
						else {
							ar0[i+1] += ar0[i] / 10;
							if(ar0[i] % 10 != 0)
							ar1[i] += 10 - ar0[i] % 10;
							if(ar0[i] % 10 != 0)
							ar0[i+1] += 1;
						}
					}
				}
			}
			int check = 0;
			for(int i = 0; i < 64; i++) {
				if(ar1[i] != 0) {
					check = i;
				}
			}
			String a = "";
			for(int i = check; i >= 0; i--) {
				a += ar1[i];
			}
			
			System.out.println(Integer.parseInt(a));
		}
	}
	
}
