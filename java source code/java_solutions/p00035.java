import java.util.Scanner;

class Main{

  public static double crossProduct(double[] vec1, double[] vec2){
    double cp = vec1[0]*vec2[1] - vec1[1]*vec2[0];
    return cp;
  }

  public static int isCross(double[][] point){
    double[] abVec = {point[1][0]-point[0][0],point[1][1]-point[0][1]};
    double[] acVec = {point[2][0]-point[0][0],point[2][1]-point[0][1]};
    double[] bdVec = {point[3][0]-point[1][0],point[3][1]-point[1][1]};
    double c0 = crossProduct(acVec, bdVec);
    if (c0 == 0) {
      return -1;
    }
    double ca = crossProduct(abVec, acVec);
    double cb = crossProduct(abVec, bdVec);
    if (ca/c0 <= 1 && ca/c0 >= 0 && cb/c0 <= 1 && cb/c0 >= 0) {
      return 1;
    }
    else {
      return 0;
    }
  }

  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    while(sc.hasNext()) {
      double[][] point = new double[4][2];
      String str = sc.next();
      String[] strArr = str.split(",");
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 2; j++) {
          point[i][j] = Double.parseDouble(strArr[2*i+j]);
        }
      }
      if(isCross(point) > 0) {
        System.out.println("YES");
      }
      else {
        System.out.println("NO");
      }
    }
  }
}
