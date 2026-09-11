import java.io.BufferedReader;
import java.io.*;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
    static final int UPPER=1;
    static final int MIDDLE=2;
    static final int NEXTBOOKS_UPPER_AND_LOWER=11;
    static final int NEXTBOOKS_UPPER=12;
    static final int NEXTBOOKS_LOWER=13;
    static final int NEXTBOOKS_NONE=14;
    static char[] upperBooks;
    static char[] lowerBooks;
    static int[] nums;
    static int positon;

    public static void main(String[] args){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int testCaseNum = Integer.parseInt(br.readLine());
            nums=new int[testCaseNum];

            for(int i = 0;i<testCaseNum;i++){
                int n = Integer.parseInt(br.readLine());
                String books=br.readLine();

                upperBooks = new char[2*n];
                lowerBooks = new char[2*n];

                for(int j =0; j<upperBooks.length;j++){
                    upperBooks[j]=books.charAt(j);
                }



                for(int j =0; j<lowerBooks.length;j++){
                    lowerBooks[j]=books.charAt(j+(2*n));
                }

                positon=UPPER;

                for(int j=0;j<=n;j++){
                    positon = calculateCost(j,n,positon,i);
                }

                System.out.println(nums[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    static boolean areBooksInUpperShelf(int num){
        return upperBooks[num*2]=='Y'||upperBooks[num*2-1]=='Y';
    }
    static boolean areBooksInLowerShelf(int num){
        return lowerBooks[num*2]=='Y'||lowerBooks[num*2-1]=='Y';
    }


    static int whereNextBooks(int num,int maxNum){
        if(num==maxNum){
            if(lowerBooks[num*2-1]=='Y'&&upperBooks[num*2-1]=='Y'){
                return NEXTBOOKS_UPPER_AND_LOWER;
            }else if(upperBooks[num*2-1]=='Y'){
                return NEXTBOOKS_UPPER;
            }else if(lowerBooks[num*2-1]=='Y'){
                return NEXTBOOKS_LOWER;
            }else{
                return NEXTBOOKS_NONE;
            }
        }else{
            if(areBooksInLowerShelf(num)&&areBooksInUpperShelf(num)){
                return NEXTBOOKS_UPPER_AND_LOWER;
            }else if(areBooksInUpperShelf(num)){
                return NEXTBOOKS_UPPER;
            }else if(areBooksInLowerShelf(num)){
                return NEXTBOOKS_LOWER;
            }else{
                return whereNextBooks(num+1,maxNum);
            }
        }
    }

    static int calculateCost(int num,int maxNum,int position,int testCaseNum){

        if(num==0){
            if(lowerBooks[num]=='Y'){
                nums[testCaseNum]=3;
                position=MIDDLE;
                return position;
            }else if(upperBooks[num]=='Y'){
                int nextBooksState=whereNextBooks(num+1,maxNum);

                if(nextBooksState==NEXTBOOKS_LOWER||nextBooksState==NEXTBOOKS_UPPER_AND_LOWER){
                    position=MIDDLE;
                }else{
                    position=UPPER;
                }

                nums[testCaseNum]=2;
                return position;
            }else{
                nums[testCaseNum]=1;
                return position;
            }

        }else if(num==maxNum){

            if(position==UPPER){
                if(lowerBooks[num*2-1]=='Y'){
                    nums[testCaseNum]+= 3;
                }else if(upperBooks[num*2-1]=='Y'){
                    nums[testCaseNum]+= 1;
                }else{
                    nums[testCaseNum]+=0;
                }
            }else{
                if(lowerBooks[num*2-1]=='Y') {
                    nums[testCaseNum]+=2;
                }else{
                    nums[testCaseNum]+=1;
                }
            }
            return 0;

        }else{
            if(position == UPPER){
                if(areBooksInLowerShelf(num)){
                    position = MIDDLE;
                    nums[testCaseNum]+= 3;

                    return position;
                }else if(areBooksInUpperShelf(num)){

                    int nextBooksState=whereNextBooks(num+1,maxNum);
                    if(nextBooksState==NEXTBOOKS_LOWER||nextBooksState==NEXTBOOKS_UPPER_AND_LOWER){
                        position=MIDDLE;
                    }else{
                        position=UPPER;
                    }
                    nums[testCaseNum]+=2;
                    return position;
                }else{
                    nums[testCaseNum]+=1;
                    return position;
                }
            }else{
                if(areBooksInUpperShelf(num)){

                    int nextBooksState=whereNextBooks(num+1,maxNum);
                    if(nextBooksState==NEXTBOOKS_LOWER ||nextBooksState==NEXTBOOKS_UPPER_AND_LOWER){
                        position=MIDDLE;
                    }else{
                        position=UPPER;
                    }

                    if(areBooksInLowerShelf(num)){
                        nums[testCaseNum]+=3;
                    }else{
                        nums[testCaseNum]+=2;
                    }

                    return position;


                }else if(areBooksInLowerShelf(num)){
                    nums[testCaseNum]+=2;
                    return position;
                }else{
                    nums[testCaseNum]+=1;
                    return position;
                }
            }
        }



    }
}
