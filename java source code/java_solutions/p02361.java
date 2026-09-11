import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by huzhejie on 2016/10/10.
 * dijkstra algorithm for single source shortest path
 */
public class Main {
    /**
     * the main execution part of dijkstra algorithm
     * @param source source vertex
     */
    @SuppressWarnings("unchecked")
    private static void dijkstra(Vertex source){
        source.minDistance = 0;
        PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
        queue.add(source);
        while(!queue.isEmpty()){
            Vertex v = queue.poll();
            for(Edge edge:v.nighbors){
                int newDist = v.minDistance +edge.weight;
                /*
                make a comparision with new distance
                 */
                if(edge.target.minDistance>newDist){
                    /*
                    change the value of minDistance of the target vertex of the directed edge
                     */
                    queue.remove(edge.target);
                    edge.target.minDistance = newDist;
                    queue.add(edge.target);
                }
            }
        }
    }

    public static void main(String args[]){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int l = 1;
        String str;
        /*
        read the data and make the graph
         */
        try {
            str = br.readLine();
            Graph g = new Graph(Integer.parseInt(str.split(" ")[0]));
            int source  = Integer.parseInt(str.split(" ")[2]);
            int edge = Integer.parseInt(str.split(" ")[1]);
            while ((str = br.readLine()) != null&&l<=edge){
                String record[] = str.split(" ");
                g.addEdge(Integer.parseInt(record[0]),Integer.parseInt(record[1]),Integer.parseInt(record[2]));
                l++;
            }
            br.close();
        /*
        execute dijkstra algorithm
         */
            dijkstra(g.vertices.get(source));
        /*
        output the result
        */
            for(Vertex v:g.vertices.values()){
                if(v.minDistance == Integer.MAX_VALUE)
                    System.out.println("INF");
                else
                    System.out.println(v.minDistance);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
class Edge{
    /*
    the target vertex of the edge
     */
    public final Vertex target;
    /*
    the weight of the edge
     */
    public final int weight;

    public Edge(Vertex target,int weight){
        this.target = target;
        this.weight = weight;
    }
}
class Graph{
    /*
    the set of the vertices of the graph
     */
    Map<Integer,Vertex> vertices;
    public Graph(int vertexNumber){
        vertices = new LinkedHashMap<Integer, Vertex>();
        for(int i = 0;i<vertexNumber;i++){
            vertices.put(i,new Vertex(i));
        }
    }

    /**
     * add an edge into the graph
     * @param source the start vertex of the edge
     * @param target the target vertex of the edge
     * @param weight the weight of the edge
     */
    public void addEdge(int source,int target,int weight){
        Vertex v = vertices.get(source);
        Edge edge = new Edge(vertices.get(target),weight);
        v.nighbors.add(edge);
    }
}
@SuppressWarnings("unchecked")
class Vertex implements Comparable<Vertex>{
    /*
    the id of vertex
     */
    int id;
    /*
    minimal distance from the source to this vertex
     */
    int minDistance = Integer.MAX_VALUE;
    /*
    stores edges whose start vertex's id equals to this vertex's id
     */
    ArrayList<Edge> nighbors;

    public Vertex(int id){
        this.id = id;
        this.nighbors = new ArrayList<Edge>();
    }
    public int compareTo(Vertex o) {
        return Integer.compare(minDistance,o.minDistance);
    }
}
