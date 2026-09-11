import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import javax.print.attribute.standard.MediaSize.Other;
import javax.xml.bind.ParseConversionEvent;

public class Main {

	public static String[][] flick = new String[][]
			{
				{"wa",  "i", "nn",  "e", "wo"},
				{ "a",  "i",  "u",  "e",  "o"},
				{"ka", "ki", "ku", "ke", "ko"},
				{"sa", "si", "su", "se", "so"},
				{"ta", "ti", "tu", "te", "to"},
				{"na", "ni", "nu", "ne", "no"},
				{"ha", "hi", "hu", "he", "ho"},
				{"ma", "mi", "mu", "me", "mo"},
				{"ya", "yi", "yu", "ye", "yo"},
				{"ra", "ri", "ru", "re", "ro"}
			};
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		char[] input = sc.next().toCharArray();
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		map.put('T', 0);
		map.put('L', 1);
		map.put('U', 2);
		map.put('R', 3);
		map.put('D', 4);
		
		int pos = 0;
		while(pos < input.length){
			int f = input[pos] - '0';
			int n = map.get(input[pos + 1]);
			
			System.out.print(flick[f][n]);
			
			pos += 2;
		}
		
		System.out.println();
	}

}
