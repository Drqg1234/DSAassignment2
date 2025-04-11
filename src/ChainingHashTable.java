// A Chaining Hash Table implementation that uses a LinkedList with a threshold of 75% Table size doubles if above this threshold
// or halves if less than 25% full

import java.util.LinkedList;

public class ChainingHashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] table;
    private int size;
    private int capacity;
    private int threshold;

    static class Entry<K, V>{
        K key;
        V value;

        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    public ChainingHashTable(){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ChainingHashTable(int capacity){
        this.capacity = capacity;
        this.threshold = (int)(capacity * LOAD_FACTOR);
        this.table = new LinkedList[capacity];
    }

    // Custom hashing method for generics
    private int hash(K key) {
        byte[] bytes = key.toString().getBytes();
        final int FNV_OFFSET_BASIS = 0x811c9dc5;
        final int FNV_PRIME = 0x01000193;
        
        int hash = FNV_OFFSET_BASIS;
        for (byte b : bytes) {
            hash ^= (b & 0xff);
            hash *= FNV_PRIME;
        }
        return Math.abs(hash % capacity);
    }

    public int size(){
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity){
        LinkedList<Entry<K, V>>[] oldTable = table;
        capacity = newCapacity;
        threshold = (int)(capacity * LOAD_FACTOR);
        table = new LinkedList[capacity];
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldTable){
            if (bucket != null){
                for (Entry<K, V> entry : bucket){
                    insert(entry.key, entry.value);
                }
            }
        }
    }

    public void insert(K key, V value){
        if (key == null){
            throw new IllegalArgumentException();
        }

        if (size >= threshold){
            resize(capacity * 2);
        }

        int index = hash(key);
        if (table[index] == null){
            table[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : table[index]){
            if (entry.key.equals(key)){
                entry.value = value;
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;
    }

    public boolean delete(K key){
        int index = hash(key);
        if (table[index] == null){
            return false;
        }

        for (Entry<K, V> entry : table[index]){
            if (entry.key.equals(key)){
                table[index].remove(entry);
                size--;

                if (capacity > DEFAULT_CAPACITY && size <= threshold / 4){
                    resize(capacity / 2);
                }

                return true;
            }
        }
        return false;
    }

    public V search (K key){
        int index = hash(key);
        if (table[index] == null){
            return null;
        }
        
        for (Entry<K, V> entry : table[index]){
            if (entry.key.equals(key)){
                return entry.value;
            }
        }
        return null;
    }

}
