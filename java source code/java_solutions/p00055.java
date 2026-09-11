import java.io.*;
import java.math.*;

class Main{
public static void main(String[] args) throws IOException{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
String line="";
while((line=br.readLine())!=null){
double n=Double.parseDouble(line);
double sum=n;
for(int i=0;i<9;i++){
if(i%2==0){
n*=2;
}
else{
n/=3;
}
sum+=n;
}
BigDecimal x=BigDecimal.valueOf(sum);
x=x.setScale(8,BigDecimal.ROUND_HALF_UP);
System.out.println(x);
}
}
}
