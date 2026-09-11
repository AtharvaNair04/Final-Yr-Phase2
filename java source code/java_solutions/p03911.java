import java.util.*;
public class Main {
        static Scanner sc = new Scanner(System.in);
        static void myout(Object t){System.out.println(t);}//standard output
        static void myerr(Object t){System.err.println(t);}//standard error
        static String getStr(){return sc.next();}
        static int getInt(){return Integer.parseInt(getStr());}
        static long getLong(){return Long.parseLong(getStr());}
        static boolean hasNext(){return sc.hasNext();}
        static char[] mySplit(String str){return str.toCharArray();}
        static String toKaigyo(String[] list){return String.join("\n",list);}
        static String toHanSp(String[] list){return String.join(" ",list);}
        public static void main(String[] args){
          int N = getInt();
          int M = getInt();
          UnionFind uf = new UnionFind(N);
          ArrayList[] langList = new ArrayList[M];
          for(int i = 0; i < N; i++){
            int K = getInt();
            for(int j = 0; j < K; j++){
              int useLang = getInt() - 1;
              if(langList[useLang] == null){
                langList[useLang] = new ArrayList<Integer>();
              }
              langList[useLang].add(i);
            }
          }
          for(int i = 0; i < M; i++){
            ArrayList<Integer> tmpList = langList[i];
            if(tmpList == null){
              continue;
            }
            if(tmpList.size() > 1){
              int first = tmpList.get(0);
              for(int j = 1; j < tmpList.size(); j++){
                uf.doUnion(first,tmpList.get(j));
              }
            }
          }
          String output = "NO";
          for(int i = 0; i < N; i++){
            if(uf.getSize(i) == N){
              output = "YES";
              break;
            }
          }
          myout(output);
        }
        //Method addition frame start
public static class UnionFind{
  public int[] list;
  UnionFind(int size){
    list = new int[size];
    for(int i = 0; i < size; i++){
      list[i] = -1;
    }
  }
  public boolean isSame(int mae, int ato){
    return this.getRootIndex(mae) == this.getRootIndex(ato);
  }
  public int getRootIndex(int index){
    if(this.list[index] < 0){
      return index;
    }else{
      list[index] = this.getRootIndex(list[index]);
      return list[index];
    }
  }
  public void doUnion(int mae, int ato){
    int maeRoot = this.getRootIndex(mae);
    int atoRoot = this.getRootIndex(ato);
    if(!isSame(maeRoot, atoRoot)){
      if(maeRoot >= atoRoot){
        list[maeRoot] += list[atoRoot];
        list[atoRoot] = maeRoot;
      }else{
        list[atoRoot] += list[maeRoot];
        list[maeRoot] = atoRoot;
      }
    }
  }
  public int getSize(int index){
    return -list[this.getRootIndex(index)];
  }
}
        //Method addition frame end
}
