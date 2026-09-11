import java.util.*;
import java.math.BigDecimal;

class Main{
	public static void main(String[] args){
		Scanner stdIn = new Scanner(System.in);
		int q = stdIn.nextInt();
		Point[] query = new Point[q];
		int judge = 0;
		boolean flag = true;
		for(int i=0;i<q;i++){
			double x = stdIn.nextDouble();
			double y = stdIn.nextDouble();
			query[i] = new Point(x,y);
		}
		for(int i=0;i<q;i++){
			Vector v1 = new Vector(query[i].x,query[i].y,query[(i+1)%q].x,query[(i+1)%q].y);
			Vector v2 = new Vector(query[(i+1)%q].x,query[(i+1)%q].y,query[(i+2)%q].x,query[(i+2)%q].y);
			Vector v3 = new Vector(query[(i+2)%q].x,query[(i+2)%q].y,query[(i+3)%q].x,query[(i+3)%q].y);
			double a = v1.cross(v2);
			double b = v2.cross(v3);
			if(a*b<0){
				flag = false;
				break;
			}
		}
		if(flag){
			System.out.println(1);
		}
		else{
			System.out.println(0);
		}

	}
}

class Vector{
	public final double EPS = Math.pow(10,-8);
	public double x,y;
	final int cutoff = 9;

	Vector( double x1 , double y1 , double x2 , double y2 ){
		this.x = x2 - x1;
		this.y = y2 - y1;
	}

	double dot( Vector a ){	
		return rounding(this.x * a.x + this.y * a.y);
	}
	double cross( Vector a ){	
		return rounding(this.x * a.y - this.y * a.x);
	}

	void add( Vector a ){
		this.x = rounding(this.x + a.x);
		this.y = rounding(this.y + a.y);
	}
	void sub( Vector a ){
		this.x = rounding(this.x - a.x);
		this.y = rounding(this.y - a.y);
	}
	void mul( double z ){
		this.x = rounding(this.x * z);
		this.y = rounding(this.y * z);
	}
	void div( double z ){
		this.x = rounding(this.x / z);
		this.y = rounding(this.y / z);
	}

	double abs(){
		return Math.sqrt(norm());
	}
	double norm(){
		return rounding(this.x * this.x + this.y * this.y);
	}

	void projection( Vector a ){
		if(judge(a.norm())){
			this.x = 0;
			this.y = 0;
		}
		else{
			double cos = dot(a)/(abs() * a.abs());
			double frac = a.abs() * cos/abs();
			mul(frac);
		}
	}
	Vector reflection( Vector a ){
		projection(a);
		return new Vector(a.x,a.y,this.x,this.y);
	}

	boolean judgeisOrthogonal( Vector a ){	
		return judge(dot(a)) ? true : false;
	}  
	boolean judgeisParallel( Vector a ){	
		return judge(cross(a)) ? true : false;
	}

	boolean judge( double a ){		
		return Math.abs(a)<EPS ? true : false;
	}
	double rounding(double a){
		BigDecimal bi = new BigDecimal(String.valueOf(a));
		return bi.setScale(cutoff,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
} 


class Point{

	public double x,y;
	final double EPS = Math.pow(10,-8);
	final int cutoff = 9;

	Point(double x,double y){
		this.x = x;
		this.y = y;
	}

	double abs(){
		Point a = new Point(0,0);	
		return Math.sqrt(norm(a));	
	}
	double abs( Point a ){
		return Math.sqrt(norm(a));
	}
	double norm( Point a ){	
		return rounding((this.x - a.x) * (this.x - a.x) + (this.y - a.y) * (this.y - a.y));
	}

	void add( Point a ){
		this.x = rounding(this.x + a.x);
		this.y = rounding(this.y + a.y);
	}
	void sub( Point a ){
		this.x = rounding(this.x - a.x);
		this.y = rounding(this.y - a.y);
	}
	void mul( double z){
		this.x = rounding(this.x * z);
		this.y = rounding(this.x * z);
	}
	void div( double z){
		this.x = rounding(this.x / z);
		this.y = rounding(this.x / z);
	}

	Point getCrossPoint( Point a , Point b , Point c ){
		Point d = new Point(this.x,this.y);
		Vector s1 = new Vector(d.x,d.y,a.x,a.y);
		Vector s2 = new Vector(b.x,b.y,c.x,c.y);
		s2.mul(-10000);
		Point b2 = new Point(s2.x+b.x,s2.y+b.y);
		s2.mul(-1);
		Point c2 = new Point(s2.x+c.x,s2.y+c.y);
		double t1 = getDistanceSP(b2,c2);
		double t2 = a.getDistanceSP(b2,c2);
		s1.mul(t1/(t1+t2));
		Vector s3 = new Vector(b.x,b.y,d.x,d.y);
		s3.add(s1);
		return new Point(b.x+s3.x,b.y+s3.y);
	}

	double getDistance( Point a , Point b , Point c ){
		if(intersect(a,b,c)){
			return 0.0;
		}
		Point d = new Point(this.x,this.y);
		double x = Math.min(getDistanceSP(b,c),a.getDistanceSP(b,c));
		double y = Math.min(b.getDistanceSP(a,d),c.getDistanceSP(a,d));
		return Math.min(x,y);
	}
	double getDistanceSP( Point a , Point b ){
		Vector s = new Vector( a.x , a.y , b.x , b.y);
		Vector sa = new Vector( a.x , a.y , this.x , this.y );
		Vector sb = new Vector( b.x , b.y , this.x , this.y );
		if(s.dot(sa)<0.00){
			return abs(a);
		}
		s.mul(-1);
		if(s.dot(sb)<0.00){
			return abs(b);
		}
		return Math.abs(s.cross(sb)/s.abs());
	}

	int CCW( Point a , Point b ){
		Vector sa = new Vector( this.x , this.y , a.x , a.y );
		Vector sb = new Vector( this.x , this.y , b.x , b.y );
		if(sa.cross(sb) > EPS){
			return -1;
		}
		else if(sa.cross(sb) < -EPS){
			return 1;
		}
		else if(sa.dot(sb) < -EPS){
			return -2;
		}
		else if(sa.norm() < sb.norm()){
			return 2;
		}
		else{
			return 0;
		}
	}
	boolean intersect( Point a , Point b , Point c ){
		Point d = new Point(this.x,this.y);
		if(CCW(a,b)*CCW(a,c) <= 0 && b.CCW(c,a)*b.CCW(c,d) <= 0){
			return true;
		}
		else{
			return false;
		}
	}

	int contains( Point[] query ){
		boolean flag1 = false;
		boolean flag2 = false;
		Point p1 = new Point(this.x+1,this.y);
		for(int i=0;i<query.length;i++){
			if(CCW(query[i],p1) == 2 || CCW(query[i],p1) == 0){
				flag1 = true;
			}
		}
		Point p2 = new Point(Integer.MAX_VALUE/2,this.y);
		final int l = query.length;
		for(int i=0;i<l;i++){
			if(intersect(p2,query[i%l],query[(i+1)%l])){
				flag2 = true;
			}
		}

		if(flag1){
			return 2;
		}
		else if(flag2){
			return 1;
		}
		return 0;
	}

	void temp( Point a ){
		Point temp = new Point(0,0);
		temp.x = this.x;
		temp.y = this.y;
		this.x = a.x;
		this.y = a.y;
		a.x = temp.x;
		a.y = temp.y;
	}
	void sort( Point[] query ){
		for(int i=0;i<query.length;i++){
			for(int j=0;j<query.length-1-i;j++){
				if(query[j].x > query[j+1].x){
					query[j].temp(query[j+1]);
				}
				else if(query[j].x == query[j+1].x && query[j].y > query[j+1].y){
					query[j].temp(query[j+1]);
				}
			}
		}
	}
	ArrayList<Point> andrewScan( Point[] query ){
		ArrayList<Point> list = new ArrayList<Point>();
		if(query.length<4){
			for(int i=0;i<query.length;i++){
				list.add(query[i]);
			}
		}
		else{
			sort(query);
			ArrayList<Point> list1 = new ArrayList<Point>();
			ArrayList<Point> list2 = new ArrayList<Point>();
			list1.add(query[0]);
			list1.add(query[1]);
			int next = 2;
			while(next<query.length-1){
				if(list1.size()<2){
					list1.add(query[next]);
					next++;
				}
				else{
					Point p1 = list1.get(list1.size()-2);
					Point p2 = list1.get(list1.size()-1);
					if(p1.CCW(p2,query[next])!=-1){
						list1.add(query[next]);
						next++;
					}
					else{
						list1.remove(list1.size()-1);
					}
				}
			}
			Point p1 = list1.get(list1.size()-2);
			Point p2 = list1.get(list1.size()-1);
			if(p1.CCW(p2,query[query.length-1])!=-1){
				list1.add(query[query.length-1]);
			}
			else{
				list1.remove(list1.size()-1);
				list1.add(query[query.length-1]);
			}
			list2.add(query[query.length-1]);
			list2.add(query[query.length-2]);
			next = query.length-3;
			while(next>0){
				if(list2.size()<2){
					list2.add(query[next]);
					next--;
				}
				else{
					Point a = list2.get(list2.size()-2);
					Point b = list2.get(list2.size()-1);
					if(a.CCW(b,query[next])!=-1){
						list2.add(query[next]);
						next--;
					}
					else{
						list2.remove(list2.size()-1);
					}
				}
			}
			p1 = list2.get(list2.size()-2);
			p2 = list2.get(list2.size()-1);
			if(p1.CCW(p2,query[0])>=0){
				list2.add(query[0]);
			}
			else{
				list2.remove(list2.size()-1);
				list2.add(query[0]);
			}
			for(int i=0;i<list1.size();i++){
				list.add(list1.get(i));
			}
			for(int i=1;i<list2.size()-1;i++){
				list.add(list2.get(i));
			}
		}
		return list;
	}
	double area( Point[] query ){
		double area = 0;
		for(int i=0;i<query.length-1;i++){
			area += (query[i].x - query[i+1].x) * (query[i].y + query[i+1].y)/2;
		}
		area += (query[query.length-1].x - query[0].x) * (query[query.length-1].y + query[0].y)/2;
		area = Math.abs(area);
		return area;
	}

	Point polarCos(double cos_a){
		double sin_a = Math.sqrt(1-Math.pow(cos_a,2));
		double x = cos_a*this.x - sin_a*this.y;
		double y = sin_a*this.x + cos_a*this.y;
		return new Point(x,y); 
	}
	Point polarSin(double sin_a){
		double cos_a = Math.sqrt(1-Math.pow(sin_a,2));
		double x = cos_a*this.x - sin_a*this.y;
		double y = sin_a*this.x + cos_a*this.y;
		return new Point(x,y); 
	}

	boolean judge( Point a ){	
		if( Math.abs( this.x - a.x ) < EPS && Math.abs( this.y - a.y ) < EPS){
			return true;
		}
		else{
			return false;
		}
	}
	double rounding(double a){
		BigDecimal bi = new BigDecimal(String.valueOf(a));
		return bi.setScale(cutoff,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
