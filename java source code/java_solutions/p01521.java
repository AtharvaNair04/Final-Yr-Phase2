import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

import javax.print.attribute.standard.MediaSize.Other;
import javax.xml.bind.ParseConversionEvent;

public class Main {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		char[] input = sc.next().toCharArray();
		
		boolean first_w = input[0] == 'o';
		
		int length = 1;
		char prev = input[0];
		for(int i = 1; i < input.length; i++){
			if(prev != input[i]){
				length++;
				prev = input[i];
			}
		}
		
		if(length % 2 == 0 || first_w){
			System.out.println('o');
		}else{
			System.out.println('x');
		}
		
	}

}
