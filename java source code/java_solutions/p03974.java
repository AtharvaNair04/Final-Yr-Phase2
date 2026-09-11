import java.io.PrintWriter;
import java.util.Scanner;

/**
 * http://code-festival-2016-qualb.contest.atcoder.jp/tasks/codefestival_2016_qualB_e
 * 
 * @author Cummin
 */
public class Main {

    static int N ; //  1<= N <= 100000
    static int Q ; //  1<= Q <= 100000
    static int K[] ;
    static char S[][] ; // Σ|S| < 400000文字
    static char P[][];
    static PTNode root ;
    static int node_no = 0 ;  // 採番用
    static PTNode node_pool[] ;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        S = new char[N][] ;
        for (int i=0 ; i<N ;i++) {
            S[i] =sc.next().toCharArray() ;
        }
        Q = sc.nextInt();
        K = new int[Q] ;
        P = new char[Q][] ;
        for (int i=0 ; i<Q; i++) {
            K[i] = sc.nextInt() -1 ;
            P[i] = sc.next().toCharArray() ;
        }
        ///
        int size= 0 ;
        for (int i=0 ; i<N ; i++) {
            size = size + S[i].length +1;
        }
       node_pool = new PTNode[size];

        make_tree() ;
        /** /
        tree_dump() ;
        System.out.println() ;
        /**/
        
        ////// 出力 ////////////////
        PrintWriter out = new PrintWriter(System.out);
        for (int i = 0; i < Q; i++) {
            out.println(Solve(root, K[i], P[i]));
        }
        out.flush();
        ////////////////////////////
    }
        /**/
    /////////// tree dump /////////////

    static void tree_dump() {
        PTNode cn ;
        for (int n= 0 ; n< node_pool.length; n++) {
            if (node_pool[n]==null) return ;
            cn = node_pool[n] ;
            if (cn.s==null)  {
                System.out.printf("\n Node=%d, char=%s, link_cnt=%d, leaf=%d ",
                    cn.number, "-", cn.link_cnt, cn.leaf);
            } else {
            System.out.printf("\n Node=%d, char=%s, link_cnt=%d, leaf=%d ",
                    cn.number, String.valueOf(cn.s), cn.link_cnt, cn.leaf);
            }
            for (int i=0 ; i<26; i++){
                if (cn.next_node[i]==0) continue ;
                System.out.printf( "child=%d, ", cn.next_node[i]) ;
            }      
        }
    }
        ////////////////////////////////
        /**/
    
    //////////////////////////////////////
    static class PTNode {   // Patricia Trie  // 親がindexを持つ。親のnodeからchild_nodeを操作する
        char s[] ; // ponter to original string
        ////////
        int number ;
        //int index ; // ノードの下のNodeに該当する文字があるかのIndex
        int link_cnt ;
        int next_node[] = new int[26] ; // 次のレベルへのリンク
        int leaf ;
        PTNode() {
            this.number = node_no ;
            node_no ++ ;
//            System.out.printf("PTNode (New)= %d \n",number) ;
        }
    }
    static int compare(char a[], char b[]) { // マッチしない位置を返す　// 全体がマッチすると、min(a.length, b.length)+1を返す
        int len = a.length;
        if (len==0) return 0 ;
        if (len>b.length) len = b.length;
        for (int i=0 ; i<len ; i++){
            if (a[i]!=b[i]) return i+1 ;
        }
        return len +1 ;       
    }

    static void insert(PTNode node, char in[]) {
        // 子供のエントリーは一文字目から始まるものがない場合->nodeの追加
        if (node.next_node[in[0]-'a'] ==0) {
            PTNode p = new PTNode() ;
            node_pool[p.number] = p ;
            p.s = in ;
            //
            node.next_node[in[0]-'a']=p.number ;
            node.link_cnt ++ ;
            p.leaf = 1 ;
            return ;
        }
        // inの1文字目から始まるエントリーが子供にある
        PTNode child_node = node_pool[node.next_node[in[0]-'a']];
        if (child_node==null)    System.out.printf("Child node match ERROR\n") ;

        int pos = compare(child_node.s, in) ; // 全体がマッチすると、length+1を返す
        if ((pos> child_node.s.length) && (in.length == child_node.s.length)) { 
            // 全体がマッチし、かつ、同じ長さ==>Nodeは追加せずにLeafだけ追加
            node.link_cnt ++ ;
            child_node.leaf = 1 ;
            return ;
        }
        if ((pos > child_node.s.length)&&(in.length >child_node.s.length)) {
           // 全体がマッチし、かつ、inが長い -> 長い部分を、子供として追加する
            char in2[] = new char[in.length - pos + 1];
            System.arraycopy(in, pos-1, in2, 0, in2.length) ;
            insert(child_node, in2) ;
            node.link_cnt++ ;
            return ;
        }
        // 分割が必要な場合
        char head[] = new char[pos-1];
        System.arraycopy(child_node.s, 0, head, 0, pos-1) ;
        char tail[] = new char[child_node.s.length-pos +1] ;
        System.arraycopy(child_node.s, pos-1, tail, 0, tail.length);
        // マッチした長さがsより短い場合、元のノードを分割する
        PTNode temp = child_node ;
        node.next_node[child_node.s[0]-'a'] = 0 ;  // remove(child_node)
        PTNode new_n = new PTNode() ; // これを前に挿入する
        node_pool[new_n.number] = new_n ;
        new_n.s = head ;
        node.next_node[head[0]-'a']=new_n.number ;  // add(new_node) [replace with new node]
        new_n.link_cnt = temp.link_cnt + temp.leaf  ;        //
        temp.s = tail ;
        new_n.next_node[tail[0]-'a']=temp.number ;  // add(temp_node) after new node
         // inはsの部分文字列　-> 挿入したノードにLeafを追加
        if((pos-1)==in.length) { 
            new_n.leaf = 1;
            node.link_cnt++; // 最後に親を更新する
        } else {
        //  相違する部分のノードを追加
        char tail2[] = new char[in.length-pos+1];
        System.arraycopy(in, pos-1, tail2, 0, tail2.length);
            PTNode new_n2 = new PTNode();
            node_pool[new_n2.number] = new_n2 ;
            new_n2.s = tail2 ;
            new_n.next_node[tail2[0]-'a'] = new_n2.number ;
            new_n.link_cnt++ ;
            new_n2.leaf = 1;
            node.link_cnt++; // 最後に親を更新する
        }
    }    

    //////////////////////////////////////
    static int power_2[] = {1,2,4,8,16,32,64,128,256,512,1024,2048,4096,1<<13, 1<<14, 1<<15, 1<<16, 1<<17, 
                     1<<18, 1<<19, 1<<20, 1<<21, 1<<22, 1<<23, 1<<24, 1<<25}; 
    
    static void make_tree() {
        root = new PTNode() ;
        node_pool[root.number] = root ;
        for (int i=0 ; i<N; i++) {
            insert(root, S[i]) ;
        }
    }

    static int Solve(PTNode current_node, int k, char[] ptn) {
        int cnt = 0;
        for (int i = 0; i < S[k].length;) {
            OUTER:
            for (int j = 0; j < 26; j++) {
                if (current_node.next_node[ptn[j] - 'a'] == 0) {
                    continue;
                }
                int child_node_num = node_pool[current_node.next_node[ptn[j] - 'a']].number;
                int pt = S[k][i] - ptn[j];
                if (pt != 0) {
                    cnt = cnt + node_pool[child_node_num].link_cnt + node_pool[child_node_num].leaf;
                    continue;
                }
                if (pt == 0) {
                    cnt = cnt + node_pool[child_node_num].leaf;
                    i = i + node_pool[child_node_num].s.length;
                    current_node = node_pool[child_node_num];
                    break OUTER;
                }
            }
        }
        return cnt;
    }
}
