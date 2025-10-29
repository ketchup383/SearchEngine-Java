package SearchEngine_Project;

public class MyPair<K,V> {
	private K key;
	private V value;

	public MyPair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	// Returns key of HashPair
	public K getKey() {
		return this.key;
	}

	// Returns value of HashPair
	public V getValue() {
		return this.value;
	}

	// Set value of HashPair
	public void setValue(V value) {
		this.value = value;
	}
}

