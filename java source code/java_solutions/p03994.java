import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		String finalOutput = "";
		String line;
		char[] charSet = null;
		int count = 0;
		int times = 0;
		HashMap<Character, Integer> mMap1 = new HashMap<>();
		HashMap<Integer, Character> mMap2 = new HashMap<>();
		mMap1.put('a', 97);

		mMap1.put('b', 98);
		mMap1.put('c', 99);
		mMap1.put('d', 100);
		mMap1.put('e', 101);
		mMap1.put('f', 102);
		mMap1.put('g', 103);
		mMap1.put('h', 104);
		mMap1.put('i', 105);
		mMap1.put('j', 106);
		mMap1.put('k', 107);
		mMap1.put('l', 108);
		mMap1.put('m', 109);
		mMap1.put('n', 110);
		mMap1.put('o', 111);
		mMap1.put('p', 112);
		mMap1.put('q', 113);
		mMap1.put('r', 114);
		mMap1.put('s', 115);
		mMap1.put('t', 116);
		mMap1.put('u', 117);
		mMap1.put('v', 118);
		mMap1.put('w', 119);
		mMap1.put('x', 120);
		mMap1.put('y', 121);
		mMap1.put('z', 122);
		for (Entry<Character, Integer> e : mMap1.entrySet()) {
			mMap2.put(e.getValue(), e.getKey());
		}

		finalOutput += " ";

		while ((line = inReader.readLine()) != null) {
			if (line.trim().compareTo("") == 0 || line == null || count == 3)
				break;

			switch (count) {
			case 0:
				// noPair = Integer.parseInt(line.trim());
				charSet = line.trim().toLowerCase().toCharArray();
				break;
			case 1:
				times = Integer.parseInt(line.trim());

				break;
			}

			if (count == 1)
				break;
			++count;
		}

		for (int i = 0; i < charSet.length - 1; i++) {
			if (mMap1.get(charSet[i]) + times > mMap1.get('z') && charSet[i] != 'a') {

				times = times - (mMap1.get('z') - mMap1.get(charSet[i]) + 1);
				charSet[i] = 'a';
			}

		}
		times %= 26;
		if (mMap1.get(charSet[charSet.length - 1]) + times > mMap1.get('z')) {

			
			times = times - (mMap1.get('z') - mMap1.get(charSet[charSet.length - 1]) + 1);
			charSet[charSet.length - 1] = 'a';
		}
		charSet[charSet.length - 1] = mMap2.get(mMap1.get(charSet[charSet.length - 1]) + times);
		
		System.out.println(new String(charSet));

		inReader.close();
		outWriter.close();

	}

}
