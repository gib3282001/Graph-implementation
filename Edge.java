public class Edge implements Comparable<Edge> { 
    private final int v;
    private final int w;
    private final double cost;
    private final int dist;

    public Edge(int v, int w, int dist, double cost) {
        this.v = v;
        this.w = w;
        this.cost = cost;
        this.dist = dist;
    }

    public double cost() {
        return cost;
    }

    public int dist(){
        return dist;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }

    public int compareTo(Edge that) {
        if      (this.cost() < that.cost()) return -1;
        else if (this.cost() > that.cost()) return +1;
        else                                    return  0;
    }

    public int compareDist(Edge that) {
        if      (this.dist() < that.dist()) return -1;
        else if (this.dist() > that.dist()) return +1;
        else                                    return  0;
    }

    public String toString() {
        String s = v + " " + w + " " + dist + " " + cost;
        return s;
    }
}
