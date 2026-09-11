import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	Scanner sc;
	public void run(){
		sc = new Scanner(System.in);
		while(sc.hasNextInt()){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if(n == 0 && m == 0){
				break;
			}
			else{
				calc(n, m);
			}
		}
	}
	public void calc(int n, int m){
		Node root = new Node("", null);

		for(int i = 0; i < n; i++){
			String line = sc.next();
			String[] lines = line.split("/");
			Node node = root;
			for(int t = 0; t < lines.length-1; t++){
				if(node.map.containsKey(lines[t])){
					node = node.map.get(lines[t]);
				}
				else{
					node.map.put(lines[t], new Node(lines[t], node));
					node = node.map.get(lines[t]);
				}
			}
			node.filenameMap.put(lines[lines.length-1], lines[lines.length-1]);
		}

		for(int i = 0; i < m; i++){
			String html1 = sc.next();
			String html2 = sc.next();

			if(html1.charAt(html1.length()-1) == '/') html1 += " ";
			if(html2.charAt(html2.length()-1) == '/') html2 += " ";
			
			String[] h1 = html1.split("/");
			String[] h2 = html2.split("/");

			String node1 = getNode(h1, root);
			String node2 = getNode(h2, root);

			if(node1 == null || node2 == null) System.out.println("not found");
			else if(node1 == node2)
				System.out.println("yes");
			else System.out.println("no");
		}
	}
	public String getNode(String[] html, Node root){
		Node node = root;
		for(int i = 0; i < html.length; i++){
			String s = html[i];
			if(s.equals(".")){
				continue;
			}
			else if(s.equals("..")){
				node = node.ue;
			}
			else if(i == html.length - 1 && s.equals(" ")){
				break;
			}
			else if(node.map.containsKey(s)){
				node = node.map.get(s);
			}
			else if(i == html.length - 1 && node.filenameMap.containsKey(s)){
				return node.filenameMap.get(s);
			}
			else return null;
		}
		if(node.filenameMap.containsKey("index.html")){
			return node.filenameMap.get("index.html");
		}
		else return null;
	}

	public static void main(String[] args){
		String[] t = "/home/ACM/index.html/".split("/");
		new Main().run();
	}
}

class Node{
	String name;
	HashMap<String, Node> map;
	HashMap<String, String> filenameMap;
	Node ue;

	Node(String n, Node ue){
		this.name = n;
		this.map = new HashMap<String, Node>();
		this.filenameMap = new HashMap<String, String>();
		this.ue = ue;
	}
}
