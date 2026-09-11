import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
 
class Point{
    public static int nextId = 0;

    public int id;
    public int x;
    public int y;
    /** パチクリのタイプ（0?4）、もしくは-1（S、G） */
    public int type;
    public int distanceFromStart;
    public List<Point> nextPointList;

    public Point(int x, int y, int type) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
        this.type = type;
        this.distanceFromStart = Integer.MAX_VALUE;
    }

    public int distance(Point p) {
        return Math.abs(this.x - p.x) + Math.abs(this.y - p.y);
    }
}

public class Main {
    public static BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

    public static Point start = null;
    public static Point goal = null;

    //パチモンのタイプ毎の頂点リスト
    public static List<List<Point> > pointListByType =
            new ArrayList<List<Point> >();
    static {
        // 枠だけ用意
        for(int i = 0; i < 5; i++) {
            pointListByType.add(new ArrayList<Point>());
        }
    }

    public static void main(String args[]) throws IOException {
        while(true) {
            String mapSizeStr = br.readLine();
            if("0 0".equals(mapSizeStr)) break;

            String[] splittedStr = mapSizeStr.split(" ");
            int x = Integer.parseInt(splittedStr[0]);
            int y = Integer.parseInt(splittedStr[1]);
             
            for(int i = 0; i < 5; i++) {
                pointListByType.get(i).clear();
            }
 
            Point.nextId = 0;
 
            for(int r = 0; r < y; r++){
                String line = br.readLine();
                for(int c = 0; c < x; c++) {
                    char pointLiteral = line.charAt(c);
                    if(pointLiteral == 'S') {
                        start = new Point(c, r, -1);
                        start.nextPointList = new ArrayList<Point>();
                    } else if(pointLiteral == 'G') {
                        goal = new Point(c, r, -1);
                        goal.nextPointList = new ArrayList<Point>();
                    } else if(pointLiteral != '.') {
                        int type = (int)pointLiteral - (int)'1';
                        pointListByType.get(type).add(new Point(c, r, type));
                    }
                }
            }
             
            //到達不可能判定
            //1匹もパチモンがいない属性が2つ以上あれば、到達不能
            int zeroTypeCount = 0;
            for(int i = 0 ; i < 5; i++) {
                if(pointListByType.get(i).size() == 0) {
                    if(++zeroTypeCount >= 2) {
                        break;
                    }
                }
            }
            if(zeroTypeCount >= 2) {
                System.out.println("NA");
                continue;
            }

            // 各属性間のつながりを生成
            List<Point> firePointList = pointListByType.get(0);
            List<Point> icePointList = pointListByType.get(1);
            List<Point> treePointList = pointListByType.get(2);
            List<Point> earthPointList = pointListByType.get(3);
            List<Point> waterPointList = pointListByType.get(4);
            //  fire to ice
            for (Point p : firePointList) {
                p.nextPointList = icePointList;
            }
            //  ice to tree
            for (Point p : icePointList) {
                p.nextPointList = treePointList;
            }
            //  tree to earth
            for (Point p : treePointList) {
                p.nextPointList = earthPointList;
            }
            //  earth to water
            for (Point p : earthPointList) {
                p.nextPointList = waterPointList;
            }
            //  water to fire
            for (Point p : waterPointList) {
                p.nextPointList = firePointList;
            }

            // 各属性を最初に選んだ場合についてそれぞれ最短経路を求める
            int minLenType = -1;
            int minLen = Integer.MAX_VALUE;
            for (int firstType = 0; firstType < 5; firstType++) {
                //最初の一匹以外のいずれかの属性のパチモンがいない場合到達不能
                if(
                    firstType == 0 && (
                        icePointList.size() == 0 ||
                        treePointList.size() == 0 ||
                        earthPointList.size() == 0 ||
                        waterPointList.size() == 0
                    ) ||
                    firstType == 1 && (
                        firePointList.size() == 0 ||
                        treePointList.size() == 0 ||
                        earthPointList.size() == 0 ||
                        waterPointList.size() == 0
                    ) ||
                    firstType == 2 && (
                        firePointList.size() == 0 ||
                        icePointList.size() == 0 ||
                        earthPointList.size() == 0 ||
                        waterPointList.size() == 0
                    ) ||
                    firstType == 3 && (
                        firePointList.size() == 0 ||
                        icePointList.size() == 0 ||
                        treePointList.size() == 0 ||
                        waterPointList.size() == 0
                    ) ||
                    firstType == 4 && (
                        firePointList.size() == 0 ||
                        icePointList.size() == 0 ||
                        treePointList.size() == 0 ||
                        earthPointList.size() == 0
                    )
                ) {
                    continue;
                }

                //スタートからのつながりを生成
                start.nextPointList.clear();
                //for(int i = 0; i < 5; i++) {
                //    // from Start to evry point expect Goal
                //    if(i == (firstType + 1) % 5) {
                //        start.nextPointList.addAll(pointListByType.get(i));
                //    }
                //}
                start.nextPointList.addAll(pointListByType.get((firstType + 1) % 5));

                 //最短経路を探索
                int shortestPathLen = searchShortestPath(firstType);

                if(minLen > shortestPathLen) {
                    minLen = shortestPathLen;
                    minLenType = firstType;
                }

                // 計算で更新された項目の初期化
                for(int i = 0; i < 5; i++) {
                    List<Point> tempPointList = pointListByType.get(i);
                    for(Point p : tempPointList) {
                        p.distanceFromStart = Integer.MAX_VALUE;
                    }
                }
                goal.distanceFromStart = Integer.MAX_VALUE;
            }

            if(minLen == Integer.MAX_VALUE) {
                System.out.println("NA");
            } else {
                System.out.println((minLenType + 1) + " " + minLen);
            }
        }
    }

    private static int searchShortestPath(int firstType) {
        start.distanceFromStart = 0;
        int searchedPointCount = 0;
        int lastType = (firstType + 4) % 5;
        
        Point searchingPoint = start;
        PriorityQueue<Point> q = new PriorityQueue<Point>(
                            5002,
                            new Comparator<Point>() {
                                @Override
                                public int compare(Point p1, Point p2) {
                                    return p1.distanceFromStart - p2.distanceFromStart;
                                }
                            }
                        );

        q.add(start);

        while(!q.isEmpty()) {
             //スタートからの距離が最小のものを頂点集合から削除
            searchingPoint = q.poll();

            if(searchingPoint.id == goal.id) break;

            if(searchingPoint.type == lastType) {
                int distance = searchingPoint.distance(goal);
                int searchinPointDistance = searchingPoint.distanceFromStart;
                if(goal.distanceFromStart > searchinPointDistance + distance) {
                    goal.distanceFromStart = searchinPointDistance + distance;
                    q.add(goal);
                }
            } else {
                //検索中のノードから繋がっているものを走査
                for(Point tempNextPoint : searchingPoint.nextPointList) {
                    int distance = searchingPoint.distance(tempNextPoint);
                    int searchinPointDistance = searchingPoint.distanceFromStart;
                    if(tempNextPoint.distanceFromStart > searchinPointDistance + distance) {
                        tempNextPoint.distanceFromStart = searchinPointDistance + distance;
                        q.add(tempNextPoint);
                    }
                }
            }
        }
 
        //探索完了
        return goal.distanceFromStart;
    }
}
