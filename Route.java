import java.util.ArrayList;
import java.util.List;

public class Route {
    public List<Edge> list;
    public double cost;
    public int hops;
    public int dist;

    public Route(){
        list = new ArrayList<Edge>();
        cost = 0.0;
        hops = 0;
        dist = 0;
    }

    public Route(Route r){
        list = new ArrayList<>();
        for(Edge e : r.list){
            list.add(e);
        }
        cost = r.cost;
        hops = r.hops;
        dist = r.dist;
    }

    public void add(Edge e){
        list.add(e);
        cost += e.cost();
        hops++;
        dist += e.dist();
    }

    public void remove(Edge e){
        list.remove(e);
        cost -= e.cost();
        hops--;
        dist -= e.dist();
    }
}
