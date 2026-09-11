import java.io.*;
import java.util.StringTokenizer;
     
class Main{
     
static final PrintWriter out=new PrintWriter(System.out);
  
static class binarySearchTree{
  
static class Node{
int id;
Node p;
Node l;
Node r;
Node(int id){
this.id=id; this.p=null; this.l=null; this.r=null;
}
}
  
private static Node root;
  
binarySearchTree(){
root=null;
}
  
public static void insert(int v){
if(root==null) root=new Node(v);
else __insert(new Node(v));
}
  
private static void __insert(Node z){
Node x=root;
Node y=null;
while(x!=null){
y=x;
if(z.id<x.id) x=x.l;
else x=x.r;
}
z.p=y;
if(z.id<y.id) y.l=z;
else y.r=z;
}
  
public static boolean find(int v){
Node x=root;
while(x!=null){
if(x.id==v) return true;
else if(x.id>v) x=x.l;
else x=x.r;
}
return false;
}

public static boolean delete(int v){
Node x=root;
Node y=null;
boolean isLeftChild=true;
while(true){
if(x==null) return false;
else if(x.id==v) break;
y=x;
if(v<x.id){
x=x.l;
isLeftChild=true;
}
else{
x=x.r;
isLeftChild=false;
}
}
if(x.l==null){
if(x==root) root=x.r;
else if(isLeftChild) y.l=x.r;
else y.r=x.r;
}
else if(x.r==null){
if(x==root) root=x.l;
else if(isLeftChild) y.l=x.l;
else y.r=x.l;
}
else{
y=x;
Node right=x.r;
isLeftChild=false;
while(right.l!=null){
y=right;
right=right.l;
isLeftChild=true;
}
x.id=right.id;
if(isLeftChild) y.l=right.r;
else y.r=right.r;
}
return true;
}

public static void print(){
inorder(root);
out.println();
preorder(root);
out.println();
}
  
private static void preorder(Node now){
if(now==null) return;
out.print(" "+now.id);
preorder(now.l);
preorder(now.r);
}
   
private static void inorder(Node now){
if(now==null) return;
inorder(now.l);
out.print(" "+now.id);
inorder(now.r);
}
   
private static void postorder(Node now){
if(now==null) return;
postorder(now.l);
postorder(now.r);
out.print(" "+now.id);
}
}
  
public static void main(String[] args) throws IOException{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
StringTokenizer st;
binarySearchTree tree=new binarySearchTree();
int num=Integer.parseInt(br.readLine());
while(num-->0){
st=new StringTokenizer(br.readLine());
String command=st.nextToken();
int n;
if("insert".equals(command)){
n=Integer.parseInt(st.nextToken());
tree.insert(n);
}
else if("find".equals(command)) {
n=Integer.parseInt(st.nextToken());
out.println((tree.find(n))?"yes":"no");
}
else if("delete".equals(command)) {
n=Integer.parseInt(st.nextToken());
tree.delete(n);
}
else tree.print();
}
out.flush();
}
}
