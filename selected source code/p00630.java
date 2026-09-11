import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			final String input = sc.next();
			final String type = sc.next();
			
			if("X".equals(type)){
				break;
			}
			
			char[][] units;
			
			if(input.indexOf("_") >= 0){
				String[] ret = input.split("_");
				units = new char[ret.length][];
				for(int i = 0; i < ret.length; i++){
					units[i] = ret[i].toCharArray();
				}
			}else{
				char[] array = input.toCharArray();
				
				int count = 0;
				for(int i = 1; i < array.length; i++){
					if('A' <= array[i] && array[i] <= 'Z'){
						count++;
					}
				}
				count++;
				
				units = new char[count][];
				
				int start = 0;
				int cur = 0;
				for(int i = 1; i < array.length; i++){
					if('A' <= array[i] && array[i] <= 'Z'){
						final int size = i - start;
						
						units[cur] = new char[size];
						for(int j = 0; j < size; j++){
							if(j == 0){
								if('A' <= array[start + j] && array[start + j] <= 'Z'){
									units[cur][j] = (char) (array[start + j] - ('A' - 'a'));
								}else{
									units[cur][j] = array[start + j];
								}
							}else{
								units[cur][j] = array[start + j];
							}
						}
						
						start = i;
						cur++;
					}
				}
				
				final int size = input.length() - start;
				units[cur] = new char[size];
				for(int j = 0; j < size; j++){
					if(j == 0){
						if('A' <= array[start + j] && array[start + j] <= 'Z'){
							units[cur][j] = (char) (array[start + j] - ('A' - 'a'));
						}else{
							units[cur][j] = array[start + j];
						}
					}else{
						units[cur][j] = array[start + j];
					}
				}
			}
			/*
			for(char[] ch : units){
				System.out.println(Arrays.toString(ch));
			}
			*/
			if("L".equals(type)){
				for(int i = 0; i < units.length; i++){
					for(int j = 0; j < units[i].length; j++){
						System.out.print((char)(units[i][j] + (j == 0 && i != 0 ? 'A' - 'a' : 0)));
					}
				}
				
				System.out.println();
			}else if("U".equals(type)){
				for(int i = 0; i < units.length; i++){
					for(int j = 0; j < units[i].length; j++){
						System.out.print((char)(units[i][j] + (j == 0 ? 'A' - 'a' : 0)));
					}
				}
				
				System.out.println();
			}else if("D".equals(type)){
				for(int i = 0; i < units.length; i++){
					for(int j = 0; j < units[i].length; j++){
						System.out.print(units[i][j]);
					}
					
					if(i != units.length - 1){
						System.out.print("_");
					}
				}
				
				System.out.println();
			}
		}
		
	}

}
