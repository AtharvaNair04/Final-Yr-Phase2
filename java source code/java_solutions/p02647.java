import java.util.Scanner;

public class Main{
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);

    //input
    int numOfLamps = sc.nextInt();
    int numOfReps = sc.nextInt();
    int[] brightnessArray = new int[numOfLamps];
    for(int i = 0; i < brightnessArray.length; i++){
      brightnessArray[i] = sc.nextInt();
    }

    //output
    int realReps = numOfReps;
    //(int)(Math.log(numOfLamps)/Math.log(2))*5
    if( numOfReps > 41 ){
      realReps = 41;
    }

    for(int i = 0; i < realReps; i++){

      int[] cumulativeSumArray = new int[numOfLamps];
      for(int j = 0; j < brightnessArray.length; j++){

        int leftNum = j-brightnessArray[j];
        if( j-brightnessArray[j] < 0 ){
          leftNum = 0;
        }
        int rightNum = j+brightnessArray[j];
        if( j+brightnessArray[j] > numOfLamps ){ //if beyond the end, right side set to end
          rightNum = numOfLamps;
        }

        //upon taking the cumulative sum, code below would add 1 to all in range
        cumulativeSumArray[leftNum]++;
        if( rightNum+1 < numOfLamps ){ //if not beyond the end
          cumulativeSumArray[rightNum+1]--;
        }

      }
      //take cumulative sum
      for(int m = 1; m < cumulativeSumArray.length; m++){
        cumulativeSumArray[m] += cumulativeSumArray[m-1];
      }

      //if there is no difference, break
      if( cumulativeSumArray.equals(brightnessArray) ){
        break;
      }else{ //if there is a difference, continue
        brightnessArray = cumulativeSumArray;
      }

    }
    //print out numbers
    for(int i = 0; i < brightnessArray.length; i++){
      System.out.print(brightnessArray[i]+" ");
    }

  }
}
