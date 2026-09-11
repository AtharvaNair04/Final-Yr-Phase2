import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				/* input */
				int n = Integer.parseInt(br.readLine());
				if(n==0){
					break;
				}

				String tag = br.readLine();
				ArrayList<Panel> ps = parseTags(tag);

				for(int i=0;i<n;i++){
					String[] str = br.readLine().split(" ");
					int x = Integer.parseInt(str[0]), y = Integer.parseInt(str[1]);

					boolean found = false;
					Panel current = ps.get(0);
					while(true){
						if(!current.point(x, y)){
							break;
						}
						found = true;
						boolean c = false;
						for(Panel child : current.children){
							if(child.point(x, y)){
								c = true;
								current = child;
								break;
							}
						}
						if(!c) break;
					}
					if(found){
						System.out.println(current.name + " " + current.children.size());
					} else {
						System.out.println("OUT OF MAIN PANEL 1");
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	static class Panel{
		public String name;
		public int x1,x2,y1,y2;
		public ArrayList<Panel> children;

		public Panel(String name, String coordinates){
			this.name = name;
			String[] c = coordinates.split(",");
			this.x1 = Integer.parseInt(c[0]);
			this.y1 = Integer.parseInt(c[1]);
			this.x2 = Integer.parseInt(c[2]);
			this.y2 = Integer.parseInt(c[3]);
			this.children = new ArrayList<Panel>();
		}

		boolean point(int x, int y){
			if(x1<=x&&x<=x2&&y1<=y&&y<=y2){
				return true;
			} else {
				return false;
			}
		}

		public void output(){
			System.out.print(this.name + ":");
			System.out.println(this.x1 + ", " + this.x2 + ", " + this.y1 + ", " + this.y2);
			System.out.print("CHILDREN:");
			for(Panel p : this.children){
				System.out.print(" " + p.name);
			}
			System.out.println();
		}

		public boolean parentOf(Panel p){
			if(this.x1<p.x1&&this.x2>p.x2&&this.y1<p.y1&&this.y2>p.y2) return true;
			return false;
		}
	}
	
	public static ArrayList<Panel> parseTags(String tags){
		Stack<Panel> stack = new Stack<Panel>();
		ArrayList<Panel> p = new ArrayList<Panel>();
		String name, coordinates;
		while(true){
			int s = 0, t = 0, nexts = 0;
			while(tags.charAt(s)!='<'&&s<=tags.length()){
				s++;
				t++;
			}
			if(s>=tags.length()) break;
			while(tags.charAt(t)!='>'&&t<tags.length()){
				t++;
			}
			name = tags.substring(s+1,t);

			nexts = t;
			if(tags.substring(t).indexOf("<")>-1){
				while(tags.charAt(nexts)!='<'){
					nexts++;
				}
			} else {
				break;
			}
			if(tags.charAt(s+1)!='/'){
				coordinates = tags.substring(t+1,nexts);
				Panel pn = new Panel(name,coordinates);
				if(stack.size()>0) stack.peek().children.add(pn);
				stack.push(pn);
				p.add(pn);
			} else {
				stack.pop();
			}
			tags = tags.substring(nexts);
		}
		return p;
	}
}
