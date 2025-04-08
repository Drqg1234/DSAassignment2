import java.util.ArrayList;
import java.util.List;

public class QuadraticProbingHashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.5;

    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    static class Entry<K, V>{
        K key;
        V value;
        boolean deleted;

        Entry(K key, V value){
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }

    public QuadraticProbingHashTable(){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public QuadraticProbingHashTable(int capacity){
        this.capacity = capacity;
        this.threshold = (int)(capacity * LOAD_FACTOR);
        this.table = new Entry[capacity];
    }

    private int hash(K key){
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    private int probe(int hash, int attempt){
        return (hash + attempt * attempt) % capacity;
    }

    public int size(){
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity){
        Entry<K, V>[] oldTable = table;
        capacity = newCapacity;
        threshold = (int)(capacity * LOAD_FACTOR);
        table = new Entry[capacity];
        size = 0;

        for (Entry<K, V> entry : oldTable){
            if (entry != null && !entry.deleted){
                insert(entry.key, entry.value);
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
        int attempt = 0;
        int firstDeleted = -1;

        while (true) { 
            int current = probe(index, attempt);

            if (table[current] == null){
                if (firstDeleted != -1){
                    current = firstDeleted;
                }

                table[current] = new Entry<>(key, value);
                size++;
                return;
            }
            else if (table[current].key.equals(key)){
                table[current].value = value;
                table[current].deleted = false;
                return;
            }
            else if (table[current].deleted && firstDeleted == -1){
                firstDeleted = current;
            }

            attempt++;
            if (attempt >= capacity){
                resize(capacity * 2);
                insert(key, value);
                return;
            }
        }
    }

    public boolean delete(K key){
        int index = hash(key);
        int attempt = 0;

        while (true){
            int current = probe(index, attempt);

            if (table[current] == null){
                return false;
            }
            else if (!table[current].deleted && table[current].key.equals(key)){
                table[current].deleted = true;
                size--;

                if (capacity > DEFAULT_CAPACITY && size <= threshold / 4){
                    resize(capacity / 2);
                }

                return true;
            }

            attempt++;
            if (attempt >= capacity){
                return false;
            }
        }
    }
    public V search(K key){
        int index = hash(key);
        int attempt = 0;

        while (true){
            int current = probe(index, attempt);

            if (table[current] == null){
                return null;
            }
            else if (!table[current].deleted && table[current].key.equals(key)){
                return table[current].value;
            }

            attempt++;
            if (attempt >= capacity){
                return null;
            }
        }
    }

    public List<K> keys(){
        List<K> keys = new ArrayList<>();
        for (Entry<K, V> entry : table){
            if (entry != null && !entry.deleted){
                keys.add(entry.key);
            }
        }
        return keys;
    }

    
}
