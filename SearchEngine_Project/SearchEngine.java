package SearchEngine_Project;

import java.util.ArrayList;

public class SearchEngine {
    public MyHashTable<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)
    public MyWebGraph internet;
    public XmlParser parser;
    private final static double DAMPING = 0.5;

    public SearchEngine(String filename) throws Exception{
        this.wordIndex = new MyHashTable<String, ArrayList<String>>();
        this.internet = new MyWebGraph();
        this.parser = new XmlParser(filename);
    }

    // Graph traversal of web, starting from given URL
    // For each new page seen, updates wordIndex, webGraph, and set of visited vertices
    public void crawlAndIndex(String url) {
        ArrayList<String> queue = new ArrayList<>();
        queue.add(url);

        ArrayList<String> content;
        ArrayList<String> links;

        while (!queue.isEmpty()) {
            String currentUrl = queue.remove(0);

            //Check if it has been visited yet
            if (this.internet.getVisited(currentUrl)) {
                continue;
            }

            //If it has not been visited, change status and add
            this.internet.setVisited(currentUrl, true);

            //Get content and links
            content = this.parser.getContent(currentUrl);
            links = this.parser.getLinks(currentUrl);

            //Index content
            for (String word : content) {
                word = word.toLowerCase(); //Case INSENSITIVE
                ArrayList<String> urlList = this.wordIndex.get(word);

                //Case 1: First time seeing word --> Add
                if (urlList == null) {
                    urlList = new ArrayList<>();
                    urlList.add(currentUrl);
                    this.wordIndex.put(word, urlList);
                }
                //Case 2: Word already exists --> add this URL to its list of URLs
                else if (!urlList.contains(currentUrl)) {
                    urlList.add(currentUrl);
                }
            }

            //Build internet graph
            this.internet.addVertex(currentUrl);
            for (String link : links) {
                this.internet.addVertex(link);
                this.internet.addEdge(currentUrl, link);

                //Add unvisited links to queue
                if (!this.internet.getVisited(link)) {
                    queue.add(link);
                }
            }

        }
    }


    // Computes pageRanks for every vertex in web graph
    // Only called after graph has been constructed using crawlAndIndex()
    public void assignPageRanks(double epsilon) {
        //Transpose all URLs into a 2D list
        ArrayList<String> urlList = this.internet.getVertices();

        //Create a separate list that keeps track of ranks as well
        ArrayList<Double> oldRanks = new ArrayList<>();

        //Initialize all values to 1.0
        for (String url : urlList) {
            this.internet.setPageRank(url, 1.0);
            oldRanks.add(this.internet.getPageRank(url)); //Ranks in separate list initialized to 1.0 as well
        }

        //Iterate until converged
        boolean converged = false;
        while (!converged) {
            //Make another separate list containing newly computed ranks
            ArrayList<Double> newRanks = computeRanks(urlList);

            //If reached convergence, then break out of loop
            for (int i = 0; i < urlList.size(); i++) {
                if (Math.abs(newRanks.get(i) - oldRanks.get(i)) < epsilon) {
                    converged = true;
                }
            }

            //Update ranks of pages (actual and on separate list)
            for (int i = 0; i < urlList.size(); i++) {
                this.internet.setPageRank(urlList.get(i), newRanks.get(i));
                oldRanks.set(i, newRanks.get(i));
            }
        }

    }

    // Takes as input ArrayList<String> representing URLs in web graph
    // Returns ArrayList<double> representing newly computed ranks for URLs
    public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
        ArrayList<Double> pageRanks = new ArrayList<>();

        for (String url : vertices) {
            double rankSum = 0.0;

            //Get array of all vertices pointing INTO a given URL
            ArrayList<String> inputs = this.internet.getEdgesInto(url);

            //Calculate sum (according to equation)
            for (String input : inputs) {
                int outDegInput = this.internet.getOutDegree(input);
                //Avoid divided by 0 errors
                if (outDegInput > 0) {
                    rankSum += this.internet.getPageRank(input) / outDegInput; //sum of all pr/outDeg
                }
            }

            //Calculate rank using equation
            double newRank = (1 - DAMPING) + DAMPING * rankSum;
            pageRanks.add(newRank); //ranks correspond to same position as input array
        }
        return pageRanks;
    }

    // Returns list of URLs containing query, ordered by rank (empty list if no web site contains query)
    public ArrayList<String> getResults(String query) {

        query = query.toLowerCase(); //Make case INSENSITIVE (matches with wordIndex)

        ArrayList<String> urlList = wordIndex.get(query);

        //Check if word exists in index or if it has any URL associated to it
        if (urlList == null || urlList.isEmpty()) {
            return new ArrayList<String>();
        }

        //Create new hash table for new query containing all relevant websites and sort
        MyHashTable<String, Double> urlToRank = new MyHashTable<String, Double>();
        for (String url : urlList) {
            urlToRank.put(url, this.internet.getPageRank(url));
        }

        return Sorting.fastSort(urlToRank);
    }

}