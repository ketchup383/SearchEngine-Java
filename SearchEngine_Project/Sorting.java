package SearchEngine_Project;

import java.util.ArrayList;

public class Sorting {

    // Takes as input MyHashTable with values that are Comparable
    // Returns ArrayList containing all the keys from map, ordered in descending order based in values they mapped to
    // O(n^2), where n is number of pairs in map
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
            for(int j=0; j<N-i-1; j++){
                if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
                    K temp = sortedUrls.get(j);
                    sortedUrls.set(j, sortedUrls.get(j+1));
                    sortedUrls.set(j+1, temp);
                }
            }
        }
        return sortedUrls;
    }


    // Takes as input MyHashTable with values that are Comparable
    // Returns ArrayList contains all keys from map, ordered in descending order based on values they are mapped to
    // O(n*log(n)), where n is number of pairs in map
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        ArrayList<K> keys = results.keySet();
        mergeSort(keys, results);
        return keys;
    }

    //MERGE SORT (HELPER FUNCTIONS)
    private static <K, V extends Comparable<V>> void mergeSort(ArrayList<K> keys, MyHashTable<K, V> map) {
        //Base Case
        if (keys.size() <= 1) {
            return;
        }

        //Inductive step
        else {
            int mid = keys.size() / 2;
            ArrayList<K> left = new ArrayList<>(keys.subList(0, mid));
            ArrayList<K> right = new ArrayList<>(keys.subList(mid, keys.size()));

            //Recursively mergesort on both sides
            mergeSort(left, map);
            mergeSort(right, map);

            //Merge
            merge(keys, left, right, map);
        }
    }

    private static <K, V extends Comparable<V>> void merge(ArrayList<K> merged, ArrayList<K> left,
                                                           ArrayList<K> right, MyHashTable<K, V> map) {
        merged.clear();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            V leftVal = map.get(left.get(i));
            V rightVal = map.get(right.get(j));

            //DESCENDING order
            if (leftVal.compareTo(rightVal) >= 0) {
                merged.add(left.get(i));
                i++;
            }
            else {
                merged.add(right.get(j));
                j++;
            }
        }

        //Add remaining elements
        while (i < left.size()) {
            merged.add(left.get(i++));
        }
        while (j < right.size()) {
            merged.add(right.get(j++));
        }
    }
}