import java.util.*;
import java.awt.geom.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true){
			int x = sc.nextInt();
			int y = sc.nextInt();
			if(x==0 && y==0) break;
			
			ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
			ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>();
			list.add(new Point2D.Double(x, y));
			
			int n = sc.nextInt();
			int q, r;
			Point2D.Double p;
			boolean flag = false;
			for(int i=0;i<n;i++){
				x = sc.nextInt();
				y = sc.nextInt();
				if(flag==false){
					for(int j=-1;j<=1;j++){
						for(int k=-1;k<=1;k++){
							if(0<=x+j && x+j<=9 && 0<=y+k && y+k<=9){
								for(int a=-1;a<=1;a++){
									for(int b=0;b<2;b++){
										q = x+j + 2*b + a*(1-b);
										r = y+k + 2*(1-b) + a*b;
										p = new Point2D.Double(q, r);
										if(list.contains(p)==true){
											temp.add(new Point2D.Double(x+j, y+k));
											a = 2;
											break;
										}
										q = x+j - 2*b + a*(1-b);
										r = y+k - 2*(1-b) + a*b;
										p = new Point2D.Double(q, r);
										if(list.contains(p)==true){
											temp.add(new Point2D.Double(x+j, y+k));
											a = 2;
											break;
										}
									}
								}
							}
						}
					}
					if(temp.size()==0) flag = true;
					list.clear();
					for(int j=0;j<temp.size();j++) list.add(temp.get(j));
					temp.clear();
				}
			}
			if(flag==true) System.out.println("NA");
			else System.out.println("OK");
		}
	}	
}
