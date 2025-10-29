package SearchEngine_Project;

import java.util.ArrayList;

public class MyWebGraph {
    // this field is made public for testing purposes
    public MyHashTable<String, WebVertex> vertexList;

    public MyWebGraph () {
        vertexList = new MyHashTable<String, WebVertex>();
    }


    // Adds vertex to given URL
    public boolean addVertex(String s) {
        //Check if it does not exist already
        if (this.vertexList.get(s) == null) {
            this.vertexList.put(s, new WebVertex(s));
            return true;
        }

        //If it exists already, return false
        return false;
    }


    // Adds edge between two vertices (true if graph changed, otherwise false)
    public boolean addEdge(String s, String t) {
        WebVertex fromVertex = vertexList.get(s);
        WebVertex toVertex = vertexList.get(t);

        //Check if inputs are valid
        if (fromVertex == null || toVertex == null) {
            return false;
        }

        //Add edge (returns true if it works)
        return fromVertex.addEdge(t);
    }


    // Returns an ArrayList of urls that are neighbors with the given url
    public ArrayList<String> getNeighbors(String url) {
        return vertexList.get(url).getNeighbors();
    }


    // Returns a list of all urls in the graph
    public ArrayList<String> getVertices() {
        return this.vertexList.keySet(); //return list of all keys (URLs)
    }

    // Returns the list of pages that have links to v
    public ArrayList<String> getEdgesInto(String v) {
        ArrayList<String> incoming = new ArrayList<>();

        for (String key : this.vertexList.keySet()) {
            WebVertex vertex = this.vertexList.get(key);
            if (vertex.containsEdge(v)) { //check if v is one of the edges
                incoming.add(key);
            }
        }

        return incoming;
    }


    // Returns the number of links in the page with the specified url
    public int getOutDegree(String url) {
        // NullPointerException raised if there's no vertex with specified url
        return vertexList.get(url).links.size();
    }

    // sets the pageRank of a given url
    public void setPageRank(String url, double pr) {
        vertexList.get(url).rank = pr;
    }


    // returns the page rank of a given url.
    // If the vertex with the specified url doesn't exist, returns 0
    public double getPageRank(String url) {
        if (vertexList.get(url)!=null)
            return (vertexList.get(url)).rank;

        return 0;
    }

    // sets the visited status of a given url
    public boolean setVisited(String url, boolean b) {
        if (vertexList.get(url)!=null) {
            (vertexList.get(url)).visited = b;
            return true;
        }
        return false;
    }

    // returns the visited status of a given url
    public boolean getVisited(String url) {
        if (vertexList.get(url)!=null)
            return (vertexList.get(url)).visited;

        return false;
    }


    public String toString() {
        String info = "";
        for (String s: vertexList.keySet()) {
            info += s.toString() + "\n";
        }
        return info;
    }

    public class WebVertex {
        private String url;
        public ArrayList<String> links;
        private boolean visited;
        private double rank;

        // Creates vertex on given URL
        public WebVertex (String url) {
            this.url = url;
            this.links = new ArrayList<String>();
            this.visited = false;
            this.rank = 0;
        }

        public boolean addEdge(String v) {
            if (!this.links.contains(v)) {
                this.links.add(v);
                return true;
            }
            return false;
        }


        public ArrayList<String> getNeighbors() {
            return this.links;
        }


        public boolean containsEdge(String e) {
            return this.links.contains(e);
        }


        public String toString() {
            return this.url + "\t" + this.visited + "\t" + this.rank;
        }

    }

}
