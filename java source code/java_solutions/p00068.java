import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
 
class Main{
public static void main(String[] args) throws IOException{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
String line="";
ArrayList<Point2D> array;
while(!(line=br.readLine()).equals("0")){
int n=Integer.parseInt(line);
Point2D[] points=new Point2D[n];
for(int i=0;i<n;i++){
line=br.readLine();
String[] values=line.split(",");
points[i]=new Point2D.Double(Double.parseDouble(values[0]),Double.parseDouble(values[1]));
}
Arrays.sort(points,new Comparator<Point2D>(){
public int compare(Point2D p1,Point2D p2){
return (p1.getY()>p2.getY())?1:(p1.getY()<p2.getY())?-1:(p1.getX()>p2.getX())?1:(p1.getX()<p2.getX())?-1:0;
}
});
array=new ArrayList<Point2D>();
Point2D start=points[0];
array.add(start);
double px=start.getX();
double py=start.getY();
Point2D prev=start;
while(true){
Point2D min=null;
double minx=0;
double miny=0;
double maxcos=-1;
for(Point2D p : points){
if(p==prev){
continue;
}
double nx=p.getX()-prev.getX();
double ny=p.getY()-prev.getY();
double cos=(px*nx+py*ny)/(Math.hypot(px,py)*Math.hypot(nx,ny));
if(cos>maxcos){
maxcos=cos;
minx=nx;
miny=ny;
min=p;
}
}
if(min==start){
break;
}
px=minx;
py=miny;
prev=min;
array.add(min);
}
System.out.println(points.length-array.size());
}
}
}
