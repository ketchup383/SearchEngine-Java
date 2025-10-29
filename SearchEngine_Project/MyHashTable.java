package SearchEngine_Project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<MyPair<K,V>>{
    // num of entries to the table
    private int size;
    // num of buckets
    private int capacity = 16;
    // load factor needed to check for rehashing
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<MyPair<K,V>>> buckets;


    // constructors
    public MyHashTable() {
        this(16); //Use second constructor to make this one
    }

    public MyHashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) { //Linked list at every bucket
            buckets.add(new LinkedList<>());
        }
    }

    public void clear() {
        this.size = 0;
        for (int i=0; i<this.buckets.size(); i++) {
            this.buckets.get(i).clear();
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int numBuckets() {
        return this.capacity;
    }


    // Returns buckets variable
    public ArrayList<LinkedList< MyPair<K,V> > > getBuckets(){
        return this.buckets;
    }


    // Return bucket position for given key
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.capacity;
        return hashValue;
    }


    // Takes key and value as input and adds corresponding HashPair to HashTable
    public V put(K key, V value) {
        int index = Math.abs(key.hashCode()) % this.capacity; //Compute bucket index + Compression (modulo)

        //Check if already exists
        LinkedList<MyPair<K,V>> bucket = this.buckets.get(index);

        for (MyPair<K, V> pair : bucket) {
            if (pair.getKey().equals(key)) {
                V oldValue = pair.getValue();
                pair.setValue(value); //Overwrite with new value
                return oldValue;
            }
        }

        //Add if does not exist already
        bucket.add(new MyPair<>(key, value));
        this.size++;

        //Check if exceeded max load factor
        if ((double) size / capacity > MAX_LOAD_FACTOR) {
            rehash();
        }

        return null;
    }


    // Get value corresponding to key
    public V get(K key) {
        int index = Math.abs(key.hashCode()) % this.capacity; //Compute bucket index + Compression (modulo)

        //Check if exists
        LinkedList<MyPair<K,V>> bucket = this.buckets.get(index);

        for (MyPair<K,V> pair : bucket) {
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }

        //Key is not found
        return null;
    }


    // Remove HashPair corresponding to key
    public V remove(K key) {
        int index = Math.abs(key.hashCode() % capacity);

        //Check if exist
        LinkedList<MyPair<K, V>> bucket = buckets.get(index);
        Iterator<MyPair<K, V>> iterator = bucket.iterator(); //use iterator so can safely modify

        while (iterator.hasNext()) {
            MyPair<K, V> pair = iterator.next();
            if (pair.getKey().equals(key)) {
                V value = pair.getValue();
                iterator.remove(); //can remove without any errors while iterating
                this.size--;
                return value;
            }
        }

        //Key is not found
        return null;
    }


    // Method to double size of HashTable if load factor increases beyond MAX_LOAD_FACTOR
    public void rehash() {
        //Double capacity
        this.capacity *= 2;

        ArrayList<LinkedList<MyPair<K, V>>> oldBuckets = this.buckets;
        this.buckets = new ArrayList<>(this.capacity);

        //Initialize new empty buckets
        for (int i = 0; i < this.capacity; i++) {
            this.buckets.add(new LinkedList<>());
        }

        //Put back original pairs in new buckets
        for (LinkedList<MyPair<K, V>> bucket : oldBuckets) {
            for (MyPair<K, V> pair : bucket) {
                int newIndex = Math.abs(pair.getKey().hashCode() % this.capacity); //find new bucket index + compression
                this.buckets.get(newIndex).add(pair);
            }
        }
    }

    // Return list of all keys present in HashTable
    public ArrayList<K> keySet() {
        ArrayList<K> keys = new ArrayList<>();

        for (LinkedList<MyPair<K, V>> bucket : this.buckets) {
            for (MyPair<K, V> pair : bucket) {
                keys.add(pair.getKey());
            }
        }

        return keys;
    }


    // Returns ArrayList of unique values present in hashtable
    public ArrayList<V> values() {
        ArrayList<V> uniqueValues = new ArrayList<>();

        for (LinkedList<MyPair<K, V>> bucket : buckets) {
            for (MyPair<K, V> pair : bucket) {
                V value = pair.getValue();
                if (!uniqueValues.contains(value)) { //Only add unique value
                    uniqueValues.add(value);
                }
            }
        }

        return uniqueValues;
    }


    // Returns ArrayList of all key-value pairs present in HashTable
    public ArrayList<MyPair<K, V>> entrySet() {
        ArrayList<MyPair<K, V>> entries = new ArrayList<>();

        for (LinkedList<MyPair<K, V>> bucket : buckets) {
            entries.addAll(bucket); //add whole list of pairs
        }

        return entries;
    }



    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }

    private class MyHashIterator implements Iterator<MyPair<K,V>> {
        private LinkedList<MyPair<K,V>> entries;

        private MyHashIterator() {
            entries = new LinkedList<>();
            for (LinkedList<MyPair<K, V>> bucket : buckets) { //O(m) time
                entries.addAll(bucket);
            }
        }

        @Override
        public boolean hasNext() {
            return !entries.isEmpty(); //runs in constant time O(1)
        }

        @Override
        public MyPair<K,V> next() {
            if (!hasNext()) {
                return null;
            }
            return entries.removeFirst(); //remove from linked list to keep in O(1)
        }

    }


}
