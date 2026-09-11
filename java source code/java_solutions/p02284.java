import java.util.Scanner;
import java.io.IOException;
class Main {
    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        int m = scan.nextInt();
 
        Node root = null;
        for(int i = 0;i < m;i++){
            String str = scan.next();
            if(str.equals("insert")){
                int temp = scan.nextInt();
                Node node = new Node(temp);
                if(root == null){
                    root = node;
                    continue;
                }
                insert(root, node);
            }else if(str.equals("find")){
                int temp = scan.nextInt();
                if(root.search(temp)){
                    System.out.println("yes");
                }else{
                    System.out.println("no");
                }
            }else if(str.equals("print")){
                Print(root);
            }
        }
    }
     
    /**  ?????\???????????? */
    public static void insert(Node T, Node Z){
        Node parent = T;
        for(;;){
            if(Z.key < parent.key){
                if(parent.left == null){
                    parent.left = Z;
                    break;
                }
                parent = parent.left;
            }else{
                if(parent.right == null){
                    parent.right = Z;
                    break;
                }
                parent = parent.right;
            }
        }
    }
     
    /** ?????????????????? */
    public static void Print(Node root){
        StringBuilder sb = new StringBuilder();
        root.intermediateOrderCrawl(sb);
        System.out.println(sb.toString());
        sb = new StringBuilder();
        root.precedingOrderCrawl(sb);
        System.out.println(sb.toString());
    }
}
 
/**  ???????????????????????? */
class Node{
    int key;
    Node parent;
    Node left;
    Node right;
     
    public Node(int key) {
        this.key = key;
    }
     
   /**  ??????????????????????????? */
    public void intermediateOrderCrawl(StringBuilder sb){
        if(left != null) left.intermediateOrderCrawl(sb);
        sb.append(" " + String.valueOf(key));
        if(right != null) right.intermediateOrderCrawl(sb);
    }
     
    /** ??????????????????????????? */
    public void precedingOrderCrawl(StringBuilder sb){
        sb.append(" " + String.valueOf(key));
        if(left != null) left.precedingOrderCrawl(sb);
        if(right != null) right.precedingOrderCrawl(sb);
    }
     
   /** findKey search???????????? */
    public boolean search(int findKey){
        if(findKey == key){
            return true;
        }else if(findKey < key){
            if(left == null){
                return false;
            }
            return left.search(findKey);
        }else{
            if(right == null){
                return false;
            }
            return right.search(findKey);
        }
    }
}
