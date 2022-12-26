import java.io.*;
import java.util.*;

public class RoutesGraph {
    private final int V;
    private int E;
    private static String[] cities;
    private Bag<Edge>[] adj;
    public ArrayList<Route> routes;

    public RoutesGraph(String file)throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        V = Integer.parseInt(reader.readLine());
        E = 0;
        cities = new String[V];
        for(int i = 0; i < V; i++){
            cities[i] = reader.readLine();
        }
        adj = (Bag<Edge>[]) new Bag[V];
        for(int i = 0; i < V; i++){
            adj[i] = new Bag<Edge>();
        }
        while(reader.ready()){
            String[] line = reader.readLine().split(" ");
            int v = Integer.parseInt(line[0]);
            int w = Integer.parseInt(line[1]);
            int dist = Integer.parseInt(line[2]);
            double cost = Double.parseDouble(line[3]);
            Edge e = new Edge(v, w, dist, cost);
            addEdge(e);
        }
        reader.close();
    }

    public void addEdge(Edge e){
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Bag<Edge> adj(int v){
        return adj[v];
    }

    public int V(){
        return V;
    }

    public void directRoutes(){
        System.out.println("Cities Served");
        for(int i = 0; i < cities.length; i++){
            System.out.println(cities[i]);
        }
        System.out.println();
        System.out.println("Direct Flights Available: ");
        System.out.println();
        for(int i = 0; i < V; i++){
            for(Edge e: adj[i]){
                System.out.println(cities[e.either()] + " to " + cities[e.other(e.either())]);
                System.out.println("    Price: $" + e.cost());
                System.out.println("    Distance: " + e.dist());
            } 
        }
    }

    public void findRoute(String start, String dest, double cost, int hops){
        int s = 0;
        int d = 0;
        for(int i = 0; i < cities.length; i++){
            if(cities[i].equals(start)){
                s += i;
            }
            if(cities[i].equals(dest)){
                d += i;
            }
        }
        boolean[] marked = new boolean[V];
        Route route = new Route();
        routes = new ArrayList<>();
        getRoutes(route, s, d, marked, cost, hops);
    }

    private void getRoutes(Route route, int v, int d, boolean[] marked, double cost, int hops){
        if(v == d){
            if(route.hops <= hops && route.cost <= cost){
                routes.add(new Route(route));
            }
            return;
        }
        marked[v] = true;
        for (Edge e : adj[v]) {
            int vv = e.either();
            if (!marked[vv]) {
                route.add(e);
                getRoutes(route, vv, d, marked, cost, hops);
                route.remove(e);
            }
            if(!marked[e.other(vv)]){
                route.add(e);
                getRoutes(route, e.other(vv), d, marked, cost, hops);
                route.remove(e);
            }
        }
        marked[v] = false;
    }

    public static void main(String [] args)throws IOException{
        RoutesGraph r = new RoutesGraph(args[0]);
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Please choose from one of the options below: ");
            System.out.println("1) List cities served and direct routes");
            System.out.println("2) Find a route");
            System.out.println("3) Quit program");
            int input = scanner.nextInt();
            if(input == 1){
                r.directRoutes();
            }
            if(input == 2){
                System.out.println("Enter the starting city, destination city, maximum cost and maximum hops (in that order): ");
                String skip = scanner.nextLine();
                String[] line = scanner.nextLine().split(" ");
                String start = line[0];
                String dest = line[1];
                double cost = Double.parseDouble(line[2]);
                int hops = Integer.parseInt(line[3]);
                r.findRoute(start, dest, cost, hops);
                if(r.routes.isEmpty()){
                    System.out.println("There are no routes that match your criteria");
                    break;
                }else{
                    System.out.println("There are " + r.routes.size() + " paths from " + start + " to " + dest + " with your criteria");
                }
                System.out.println("How would you like to see these paths?");
                System.out.println("1) Ordered by hops (fewest to most)");
                System.out.println("2) Ordered by cost (least expensive to most expensive)");
                System.out.println("3) Ordered by distance (shortest overall to longest overall)");
                int order = scanner.nextInt();
                if(order == 1){
                    Collections.sort(r.routes, new Comparator<Route>(){
                        public int compare(Route r1, Route r2){
                            return Integer.valueOf(r1.hops).compareTo(r2.hops);
                        }
                    });
                    for(Route ro: r.routes){
                        System.out.println("Hops: " + ro.hops + " Cost: " + ro.cost + " Distance: " + ro.dist + " Edges:");
                        System.out.print("      ");
                        for(Edge e: ro.list){
                            System.out.print("[(" + cities[e.either()] + ", " + cities[e.other(e.either())] + ") c:" + e.cost() + " d:" + e.dist() + "]");
                        }
                        System.out.println();
                    }
                }
                if(order == 2){
                    Collections.sort(r.routes, new Comparator<Route>(){
                        public int compare(Route r1, Route r2){
                            return Double.valueOf(r1.cost).compareTo(r2.cost);
                        }
                    });
                    for(Route ro: r.routes){
                        System.out.println("Hops: " + ro.hops + " Cost: " + ro.cost + " Distance: " + ro.dist + " Edges:");
                        System.out.println();
                        System.out.print("      ");
                        for(Edge e: ro.list){
                            System.out.print("[(" + cities[e.either()] + ", " + cities[e.other(e.either())] + ") c:" + e.cost() + " d:" + e.dist() + "]");
                        }
                    }
                }
                if(order == 3){
                    Collections.sort(r.routes, new Comparator<Route>(){
                        public int compare(Route r1, Route r2){
                            return Integer.valueOf(r1.dist).compareTo(r2.dist);
                        }
                    });
                    for(Route ro: r.routes){
                        System.out.println("Hops: " + ro.hops + " Cost: " + ro.cost + " Distance: " + ro.dist + " Edges:");
                        System.out.println();
                        System.out.print("      ");
                        for(Edge e: ro.list){
                            System.out.print("[(" + cities[e.either()] + ", " + cities[e.other(e.either())] + ") c:" + e.cost() + " d:" + e.dist() + "]");
                        }
                    }
                }
            }
            if(input == 3){
                break;
            }
        }
    }
}
