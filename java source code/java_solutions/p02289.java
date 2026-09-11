import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Heap heap = new Heap();
        int i =0, k;
        String action;
        while(!(action= in.next()).equals("end")){
            if(action.equals("insert")){
                k = Integer.parseInt(in.next());
                heap.insert(k);
            }
            else{
                System.out.println(heap.extract());
            }
        }
    }
}
class Heap {
    private int[] heap;
    private int i;
    Heap(){
        this.heap = new int[2000001];
        this.i =0;
    }
    public void insert(int k){
        this.i++;
        this.heap[this.i]=k;
        int pos = i;
        while(pos>1 && heap[pos]>heap[pos/2]){
            int temp = heap[pos];
            heap[pos] = heap[pos/2];
            heap[pos/2] = temp;
            pos /= 2;
        }
    }
    public int extract(){
        int answ;
        if(this.i==1){
            answ = this.heap[1];
            this.i--;
         }
        else {
            answ = this.heap[1];
            this.heap[1] = this.heap[(this.i)];
            this.i--;
            heapfy();
         }
        return answ;
    }
    private void heapfy(){
        int i = 1,j, n=this.i;
        boolean isHeap = false;
        while(!isHeap && i<=n/2) {
            j= i*2;
            if(j<n && heap[j+1]>heap[j]) j++;
            if(heap[j]>heap[i]){
                int temp = heap[i];
                heap[i] = heap[j];
                heap[j] = temp;
                i =j;
            }
            else {
                isHeap=true;
            }

        }
    }
}
