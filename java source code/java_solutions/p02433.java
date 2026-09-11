import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) throws IOException {
    
    try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

      int q = Integer.parseInt(br.readLine());
      String[] query;
      MyList list = new MyList();

      for(int i=0; i<q; i++) {
        query = br.readLine().split(" ");
        switch( query[0] ) {
          case "0" :
            list.insert( Integer.parseInt(query[1]) );
            break;
          case "1" :
            list.move( Integer.parseInt(query[1]) );
            break;
          case "2" :
            list.erase();
            break;
          default :
            System.out.println("n:" + i + " is failed.");
        }
      }

      System.out.println ( list.getAllElements() );
    }
  }
}

class Node {
  Node next,prev;
  int key;

  public Node(int key) {
    this.key = key;
  }
}

class MyList {
  Node nil;
  Node current;
  int size;

  public MyList() {
    nil = new Node(0);
    nil.next = nil.prev = nil;
    size = 0;
    
  }

  public String getAllElements() {
    StringBuilder buf = new StringBuilder(size*5);

    if(size > 0){
      current = nil.next;
      for(int i=0; i<size; i++){
        buf.append(current.key).append("\n");
        current = current.next;
      }
    }
    buf.setLength(buf.length()-1);

    return buf.toString();
  }
  public void insert(int x) {
    if(size == 0) {
      current = new Node(x);
      nil.next = current;
      nil.prev = current;
      current.prev = nil;
      current.next = nil;
      size++;
    }
    else {
      Node add = new Node(x);
      
      current.prev.next = add;
      add.prev = current.prev;

      add.next = current;
      current.prev = add;

      current = add;
      size++;
    }
  }
  public void move(int d){
    if( d > 0 ) {
      for(int i=0; i<d; i++)
        current = current.next;
    } else if( d < 0 ) {
      for(int i=0; i>d; i--)
        current = current.prev;
    }
  }
  public void erase() {
      current.prev.next = current.next;
      current.next.prev = current.prev;
      current = current.next;
      size--;
  }
}
