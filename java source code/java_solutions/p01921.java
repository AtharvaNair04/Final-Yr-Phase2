import java.io.*;
import java.util.*;

/**
 * AIZU ONLINE JUDGE
 * 2821 Trees in a Forest
 *
 *    2018/02/05
 */
public class Main {

    class Tree {
        List<P> list;
        Tree() {
            list = new ArrayList<P>();
        }
        @Override
        public String toString() {
            String s = String.format("頂点:%d ", list.size());
            if (list.size() < 50)
                for(P p : list) {
                    s += String.format("%d(%d) ", p.no+1, p.list.size());
                }
            return s;
        }

        // 頂点の分布を調べる 特徴のある頂点を返す
        P tyosa() {
            int[] cnt = new int[100];
            for(P p  : list) {
                int s = p.list.size();
                if (s >= cnt.length) {
                    cnt[0]++;
                }
                else {
                    cnt[s]++;
                }
            }
            for(int i = 0; i < cnt.length; i++) {
                if (cnt[i] > 0) {
                    //log.printf("%d:%d\n", i, cnt[i]);
                }
            }
            int min = 999999;
            int mini = 0;
            for(int i = 0; i < cnt.length; i++) {
                if (cnt[i] > 0 && cnt[i] < min) {
                    min = cnt[i];
                    mini = i;
                }
            }
            for(P p : list) {
                int s = p.list.size();
                if (mini == s || (mini == 0 && s >= cnt.length)) {
                    //log.printf("最小 隣接点%d 数%d 特徴点%s\n", mini, min, p);
                    return p;
                }
            }
            assert false;
            return null;
        }
    }

    // 頂点
    class P {
        Tree tree;
        List<P> list; // 連結する頂点
        int no; // no 0はじまり
        int hash;  // 近傍の様子
        P(int no) {
            list = new ArrayList<P>();
            this.no = no;
        }

        void calcHash() {
            int s = 0;
            int m = 1;
            for(P p : list) {
                m *= p.list.size();
                s += p.list.size();
            }
            hash = m + s * 74577;
        }
        @Override
        public String toString() {
            String s = "";
            s += String.format("%d(%d) ", no+1, list.size());
            return s;
        }
    }

    class Stack {
        P r0;
        P p0;
        P r1;
        P p1;
        List<P> list0;
        List<P> list1;
        int i;
        Stack(P r0, P p0, P r1, P p1) {
            this.r0 = r0;
            this.p0 = p0;
            this.r1 = r1;
            this.p1 = p1;
            list0 = new ArrayList<P>(p0.list);
            list0.remove(r0);
            list1 = new ArrayList<P>(p1.list);
            list1.remove(r1);
            i = 0;
        }
    }



    // p0の隣点r0以外のツリーと、p1の隣点r1以外のツリーが同型か判断する
    // 非再帰
    boolean doukei(P _r0, P _p0, P _r1, P _p1) {

        List<Stack> stackList = new ArrayList<Stack>();
        Stack stack = new Stack(_r0, _p0, _r1, _p1);
        stackList.add(stack);

        int mode = 0;
        boolean ret = false;
        Stack s = null;

        while(true) {
            switch(mode) {
            case 0: // 最初
                s = stackList.get(stackList.size() - 1);
                if (s.p0.hash != s.p1.hash) {
                    mode = 3;
                    ret = false;
                    break;
                }
                if (s.p0.list.size() != s.p1.list.size()) {
                    mode = 3;
                    ret = false;// 連結数が違う
                    break;
                }
                mode = 1;

                break;
            case 1: // ループ1
                s.i = 0;
                mode = 2;
                break;
            case 2: // ループ2
                if (s.list0.isEmpty()) {
                    ret = true;
                    mode = 3;
                    break;
                }
                P p = s.list0.get(s.list0.size() - 1);
                P q = s.list1.get(s.i);
                stackList.add(new Stack(s.p0, p, s.p1, q)); // 次の問題を作成
                mode = 0;
                break;
            case 3: // 再帰後 retに戻り値
                stackList.remove(stackList.size() - 1);
                if (stackList.isEmpty())
                    return ret;
                s = stackList.get(stackList.size() - 1);
                if (ret) {
//                    if (s.list0.size() == 1) {
//                        ret = true;
//                        mode = 3;
//                        break;
//                    }
                    s.list0.remove(s.list0.size() - 1);
                    s.list1.remove(s.i);
                    mode = 1;
                    break;
                }
                else {
                    s.i++;
                    if (s.i >= s.list1.size()) {
                        ret = false;
                        mode = 3;
                        break;
                    }
                    mode = 2;
                    break;
                }
            }
        }
    }


    // 2つのtreeのp0とp1をルートとして同型か判断する
    boolean doukei(P p0, P p1) {
        return doukei(null, p0, null, p1);
    }

    // 2つのtreeが同型か判断する tp基準点
    boolean doukei(Tree t0, P tp, Tree t1) {

        // 頂点数が違うのはだめ
        if (t0.list.size() != t1.list.size())
            return false;

        // t0の最初の点について、t1のどれかの点が同型になればOK
        //P p = t0.list.get(0); // 木の比較する頂点
        for(P p1 : t1.list) {
            //log.printf(" %d をチェック\n", p1.no+1);
            if (tp.hash != p1.hash) // 高速化
                continue;
            if (doukei(tp, p1)) {
                //log.printf(" %s と %s で同型\n", tp, p1);
                return true;
            }
        }

        return false;
    }

    // 接続  非再帰
    void setsuzoku(P p, Tree tree) {
        //log.printf("%s\n", p);
        List<P> list = new ArrayList<P>();
        list.add(p);

        while(list.size() > 0) {
            P q = list.remove(list.size() - 1);
            q.tree = tree;
            tree.list.add(q);
            for(P p2 : q.list) {
                if (p2.tree == null) {
                    list.add(p2);
                }
            }
        }
    }


	// メイン return falseでおしまい
	boolean main() throws IOException {

		int[] ir = readIntArray();
		int N1 = ir[0]; // 森の頂点数
		int M1 = ir[1]; // 森の変数
//        if (n == 0)
//            return false;

		P[] pa = new P[N1];
		List<Tree> mori = new ArrayList<Tree>();

        for(int i= 0; i < N1; i++) {
            pa[i] = new P(i);
        }
        for(int i= 0; i < M1; i++) {
            ir = readIntArray();
            P p0 = pa[ir[0]-1];
            P p1 = pa[ir[1]-1];
            p0.list.add(p1);
            p1.list.add(p0);
        }
        // tree接続
        for(int i= 0; i < N1; i++) {
            Tree tree = pa[i].tree;
            if (tree == null) {
                tree = new Tree();
                mori.add(tree);
                setsuzoku(pa[i], tree);
            }
        }

        ir = readIntArray();
        int N2 = ir[0]; // 木の頂点数
        P[] tpa = new P[N2];
        Tree tree = new Tree();  // 見本木

        for(int i= 0; i < N2 - 1; i++) {
            ir = readIntArray();
            P p0 = tpa[ir[0]-1];
            P p1 = tpa[ir[1]-1];
            if (p0 == null && p1 == null) {
                p0 = tpa[ir[0]-1] = new P(ir[0]-1);
                p1 = tpa[ir[1]-1] = new P(ir[1]-1);
                p0.tree = p1.tree = tree;
                tree.list.add(p0);
                tree.list.add(p1);
                p0.list.add(p1);
                p1.list.add(p0);
            }
            else if (p0 == null && p1 != null) {
                p0 = tpa[ir[0]-1] = new P(ir[0]-1);
                p0.tree = tree;
                tree.list.add(p0);
                p0.list.add(p1);
                p1.list.add(p0);
            }
            else if (p0 != null && p1 == null) {
                p1 = tpa[ir[1]-1] = new P(ir[1]-1);
                p1.tree = tree;
                tree.list.add(p1);
                p0.list.add(p1);
                p1.list.add(p0);
            }
            else {
                p0.list.add(p1);
                p1.list.add(p0);
            }
        }
        log.printf("見本木 %d点 %s\n",  tree.list.size(), tree.toString());
        P tp = tree.tyosa();
        log.printf("森の木 %d\n", mori.size());
//assert false;

        // ハッシュを計算
        for(P p : pa) {
            p.calcHash();
        }
        for(P p : tpa) {
            p.calcHash();
        }

        int cnt = 0;
        for(int i = 0; i < mori.size(); i++) {
            Tree t = mori.get(i);
            //log.printf("No.%d %s\n", i, t.toString());
            t.tyosa();
            if (doukei(tree, tp, t)) {
                //log.printf("  同型\n");
                cnt++;
            }
        }

		System.out.printf("%d\n", cnt);

		return true; // 正常終了 次へ
	}

	static long time0;
//	private final static boolean DEBUG = true;  // debug 　argsに何か書くとdebugになる
	private final static boolean DEBUG = false; // release

	public static void main(String[] args) throws IOException {

		if (DEBUG || args.length > 0) {
			log = System.out;

//			String inputStr = "6 4:1 2:2 3:2 4:5 6:4:2 3:3 1:3 4"; // 1
            String inputStr = "14 9:5 8:5 11:10 14:6 4:13 9:3 14:1 2:6 12:7 9:3:3 1:2 1"; // 4
//			String inputStr = "5 4:1 2:2 3:3 4:4 5:5:1 2:2 3:3 4:4 5";
//			inputStr += "0 0:";

			inputStr = inputStr.replace(":", "\n");

//			reader = new BufferedReader(new StringReader(inputStr));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("in39.txt")));
		}
		else {
			log = new PrintStream(new OutputStream() { public void write(int b) {} } ); // 書き捨て
//            log = System.out;
			reader = new BufferedReader(new InputStreamReader(System.in)); // コンソールから
//            reader = new BufferedReader(new InputStreamReader(new FileInputStream("in.txt")));
		}

		time0 = System.currentTimeMillis();
		int N = Integer.MAX_VALUE;
		//int N = readIntArray()[0];

//		for(int i = 0; i < N; i++) {
			boolean b = new Main().main();
//			if (!b)
//				break;
//		}

	        long time1 = System.currentTimeMillis();
	        log.printf("end time = %.03fsec\n", (time1 - time0) / 1000.);
		reader.close();
	}


	static PrintStream log;
	static BufferedReader reader;


	// 標準入力より1行分の区切り文字区切りでの整数値を読む
	// EOFの場合はnullを返す
	private static int[] readIntArray() throws IOException {

		String s = null;
		for(;;) {
			s = reader.readLine();
//			log.printf("%s\n", s);
			if (s == null)
				return null;
			s = s.trim();
			if (s.length() != 0) // ※※※　どうも突然空行を読むことがある。読み飛ばすとうまくいくらしい。。。。
				break;
		}

		String[] sp = s.split("[ ,]"); // 区切り文字はスペースかカンマ
		int[] a = new int[sp.length];
		for(int i = 0; i < sp.length; i++) {
			a[i] = Integer.parseInt(sp[i]);
		}
		return a;
	}

}
