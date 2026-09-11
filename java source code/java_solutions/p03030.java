import java.util.*;
class Restraunt{
  private int id ;
  private String name ;
  private int score ;

  Restraunt(int id,String name,int score){
    this.id = id;
    this.name = name;
    this.score = score;
  }
  public int idget(){
    return this.id;
  }
  public String nameget(){
    return this.name;
  }
  public int scoreget(){
    return this.score;
  }
}

class Judge implements Comparator<Restraunt>{
  public int compare(Restraunt r1, Restraunt r2){
    int nam = r1.nameget().compareTo(r2.nameget());
    if(nam == 0){
      nam = -Integer.valueOf(r1.scoreget()).compareTo(Integer.valueOf(r2.scoreget()));
    }
    return nam;
  }
}

public class Main{

  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();



    List<Restraunt> list = new ArrayList<Restraunt>();
    for(int i=0; i<n; i++){
      Restraunt r = new Restraunt(Integer.valueOf(i+1).intValue(),sc.next(),Integer.valueOf(sc.next()).intValue());
      list.add(r);
    }
    //for(Restraunt restraunt:list){
      //System.out.println("id:"+restraunt.idget()+" name:"+restraunt.nameget()+" score:"+restraunt.scoreget());
    //}
    Collections.sort(list,new Judge());
    for(Restraunt restraunt:list){
      System.out.println(restraunt.idget());
    }




  }
}
