import java.util.*;

class Main{
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String[] input = scan.nextLine().split(java.util.regex.Pattern.quote("."));
		//System.out.println(Arrays.toString(input));
		Stack<Integer> indentLengths = new Stack<>();
		int maxDepth = 0;
		indentLengths.push(0);
		while(scan.hasNext()) {
			String[] line = scan.nextLine().split(":");
			int indents = 0;
			for(char c : line[0].toCharArray()) if(c==' ')indents++;else break;
			line[0] = line[0].substring(indents);
			//System.out.println(Arrays.toString(line));
			//System.out.println(indentLengths.peek()+","+indents+","+indentLengths.size()+","+maxDepth);
			if(indentLengths.peek()==indents) {
				//tannsaku keizoku
			}
			else if(indentLengths.peek()<indents) {
				indentLengths.push(indents);
				//more kaisou
			}else {
				while(indentLengths.peek() != indents) {
					indentLengths.pop();
				}
				if(indentLengths.size()<=maxDepth) break;
			}
			if(input.length>indentLengths.size() && input[indentLengths.size()].equals(line[0])) {
				maxDepth++;
				if(maxDepth+1==input.length) {
					if(line.length==1) System.out.println("object");
					else System.out.println("string \""+line[1].substring(1)+"\"");
					return;
				}
			}
			//System.out.println(indentLengths.peek()+","+indents+","+indentLengths.size()+","+maxDepth);
		}
		System.out.println("no such property");
	}
}
