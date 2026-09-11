import java.io.*;
import java.util.*;

class Main{
    void run() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String[] num = in.readLine().split(" ",0);
            int Ax = Integer.parseInt(num[0]);
            int Ay = Integer.parseInt(num[1]);
            int Bx = Integer.parseInt(num[2]);
            int By = Integer.parseInt(num[3]);
            int Cx = Integer.parseInt(num[4]);
            int Cy = Integer.parseInt(num[5]);
            if(Ax == 0 && Ay == 0 && Bx == 0 && By == 0 && Cx == 0 && Cy == 0)
                break;
            int AbecAx = Bx - Ax;
            int AbecAy = By - Ay;
            int AbecBx = Cx - Ax;
            int AbecBy = Cy - Ay;
            double A = Math.acos((AbecAx * AbecBx + AbecAy * AbecBy) / (Math.sqrt(AbecAx*AbecAx + AbecAy*AbecAy) * Math.sqrt(AbecBx*AbecBx + AbecBy*AbecBy)));
            int BbecAx = Ax - Bx;
            int BbecAy = Ay - By;
            int BbecBx = Cx - Bx;
            int BbecBy = Cy - By;
            double B = Math.acos((BbecAx * BbecBx + BbecAy * BbecBy) / (Math.sqrt(BbecAx*BbecAx + BbecAy*BbecAy) * Math.sqrt(BbecBx*BbecBx + BbecBy*BbecBy)));
            int CbecAx = Bx - Cx;
            int CbecAy = By - Cy;
            int CbecBx = Ax - Cx;
            int CbecBy = Ay - Cy;
            double C = Math.acos((CbecAx * CbecBx + CbecAy * CbecBy) / (Math.sqrt(CbecAx*CbecAx + CbecAy*CbecAy) * Math.sqrt(CbecBx*CbecBx + CbecBy*CbecBy)));
            //????§????????????\???
            double henA = Math.sqrt((Bx - Ax)*(Bx - Ax)+(By - Ay)*(By - Ay));
            double henB = Math.sqrt((Cx - Bx)*(Cx - Bx)+(Cy - By)*(Cy - By));
            double henC = Math.sqrt((Ax - Cx)*(Ax - Cx)+(Ay - Cy)*(Ay - Cy));
            double s = (henA + henB + henC)/2;
            double S = Math.sqrt(s*(s-henA)*(s-henB)*(s-henC));
            double r = S/s;
            //System.out.println("r"+r);

            //r1????±???????
            double r1 = (r*(1+Math.tan(B/4))*(1+Math.tan(C/4)))/(2*(1+Math.tan(A/4)));
            System.out.print(r1+" ");
            double r2 = (r*(1+Math.tan(A/4))*(1+Math.tan(C/4)))/(2*(1+Math.tan(B/4)));
            System.out.print(r2+" ");
            double r3 = (r*(1+Math.tan(B/4))*(1+Math.tan(A/4)))/(2*(1+Math.tan(C/4)));
            System.out.print(r3);
            System.out.println();
        }

    }
    public static void main(String[] args) throws IOException{
        Main ma = new Main();
        ma.run();
    }
}
