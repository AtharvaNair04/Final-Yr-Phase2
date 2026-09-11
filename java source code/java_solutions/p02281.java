import java.io.*;
import java.util.StringTokenizer;
  
class Main{
  
static final PrintWriter out=new PrintWriter(System.out);
static boolean[] hasParent;
static int root;
  
static class Node{
int id;
int p;
int rank;
int height;
int s;
int deg;
int l;
int r;
String type;
Node(int id){
this.id=id; this.p=-1; this.rank=-1; this.height=-1; this.s=-1; this.l=-1; this.r=-1; this.deg=0; this.type="internal node";
}
}

static void preorder(int now,Node[] ns){
if(now<0) return;
out.print(" "+now);
preorder(ns[now].l,ns);
preorder(ns[now].r,ns);
}

static void inorder(int now,Node[] ns){
if(now<0) return;
inorder(ns[now].l,ns);
out.print(" "+now);
inorder(ns[now].r,ns);
}

static void postorder(int now,Node[] ns){
if(now<0) return;
postorder(ns[now].l,ns);
postorder(ns[now].r,ns);
out.print(" "+now);
}

public static void main(String[] args) throws IOException{
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
int num=Integer.parseInt(br.readLine());
Node[] ns=new Node[num];
hasParent=new boolean[num];
StringTokenizer st;
for(int i=0;i<num;i++){
ns[i]=new Node(i);
}
for(int i=0;i<num;i++){
st=new StringTokenizer(br.readLine());
int id=Integer.parseInt(st.nextToken());
Node n=ns[id];
int l=Integer.parseInt(st.nextToken());
n.l=l;
int r=Integer.parseInt(st.nextToken());
n.r=r;
if(l==-1&&r==-1){
n.type="leaf";
n.height=0;
}
else if(l==-1){
ns[r].p=id;
n.deg=1;
hasParent[r]=true;
}
else if(r==-1){
ns[l].p=id;
n.deg=1;
hasParent[l]=true;
}
else{
ns[l].p=id;
ns[l].s=r;
hasParent[l]=true;
ns[r].p=id;
ns[r].s=l;
n.deg=2;
hasParent[r]=true;
}
}
for(int i=0;i<num;i++){
Node an=ns[i];
if(an.rank<0) an.rank=setRank(i,ns);
if(an.height<0) an.height=setHeight(i,ns);
}
out.println("Preorder");
preorder(root,ns);
out.println();
out.println("Inorder");
inorder(root,ns);
out.println();
out.println("Postorder");
postorder(root,ns);
out.println();
out.flush();
}
  
private static int setRank(int now,Node[] ns){
if(ns[now].rank>=0) return ns[now].rank;
if(!hasParent[now]){
ns[now].type="root";
root=now;
return ns[now].rank=0;
}
return ns[now].rank=setRank(ns[now].p,ns)+1;
}
 
private static int setHeight(int now,Node[] ns){
if(now<0) return Integer.MIN_VALUE/2;
if(ns[now].height>=0) return ns[now].height;
return ns[now].height=Math.max(setHeight(ns[now].l,ns),setHeight(ns[now].r,ns))+1;
}
}
