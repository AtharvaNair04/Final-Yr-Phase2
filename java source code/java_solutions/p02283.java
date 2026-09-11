import java.util.Scanner;

public class Main {
    static Node[] list;
    public static void main(String[] args){
    	Scanner sc = new Scanner(System.in);
        int m= Integer.parseInt(sc.next());
        String s;
        int reg=0;
        int flag=1;
        list=new Node[m];
        for(int i=0;i<m;i++) list[i] = new Node();
        
        for(int i=0;i<m;i++){
        	s=sc.next();
        	if(s.equals("insert")){
        		list[i].number=Integer.parseInt(sc.next());
        		if(flag==1){
        			reg=i;
        			flag=0;
        		}else{
        			insert(list[reg],list,i);
        		}
        	}else if(s.equals("print")){
        		naka(list,list[reg]);
        		System.out.print("\n");
        		mae(list,list[reg]);
        		System.out.print("\n");
            }
        }
    }
    static void insert(Node node,Node[] list,int i){
        if(list[i].number>node.number){
            if(node.right==null){
            	node.right=list[i];
            }else{
            	insert(node.right,list,i);
            }
        }else {
        	if(node.left==null){
        		node.left=list[i];
        	}else{
        		insert(node.left,list,i);
        	}
        }
    }
    static void mae(Node[] list, Node node){
    	System.out.print(" ");
    	System.out.print(node.number);
    	if(node.left!=null){
    		mae(list,node.left);
    	}
    	if(node.right!=null){
    		mae(list,node.right);
    	}
    }
    static void naka(Node[] list, Node node){
    	if(node.left!=null){
    		naka(list,node.left);
    	}
    	System.out.print(" ");
    	System.out.print(node.number);
    	if(node.right!=null){
    		naka(list,node.right);
    	}
    }
}

class Node{
	int number;
	Node left;
	Node right;
	Node(){
		number=-1;
		left=null;
		right=null;
	}
}
