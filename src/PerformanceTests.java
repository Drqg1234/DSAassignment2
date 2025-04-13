// Created a custom interface to cycle through the data structures to make testing easier and cleaner. Uses System.currentTimeMillis to count the 
// time complexity and the getRuntime() method from Runtime to find memory usage.

import java.lang.reflect.Array;
import java.util.Random;

public class PerformanceTests {
    
    interface Structure<K>{
        void insert(K key);
        boolean search(K key);
        void delete(K key);
        int size();
        String getName();
    }
    static class AVLTreeStructure implements Structure<Integer>{
        private final AVLTree<Integer> tree = new AVLTree<>();

        @Override
        public void insert(Integer key){
            tree.insert(key);
        }

        @Override
        public boolean search(Integer key){
            return tree.search(key);
        }

        @Override
        public void delete(Integer key){
            tree.delete(key);
        }

        @Override
        public int size(){
            return tree.size();
        }

        @Override
        public String getName(){
            return "AVL Tree"; 
        }
    }
    static class SplayTreeStructure implements Structure<Integer>{
        private final SplayTree<Integer> tree = new SplayTree<>();

        @Override
        public void insert(Integer key){
            tree.insert(key);
        }

        @Override
        public boolean search(Integer key){
            return tree.search(key);
        }

        @Override
        public void delete(Integer key){
            tree.delete(key);
        }

        @Override
        public int size(){
            return tree.size();
        }

        @Override
        public String getName(){
            return "Splay Tree"; 
        }
    }
    static class ChainingHashTableStructure implements Structure<Integer>{
        private final ChainingHashTable<Integer, String> table = new ChainingHashTable<>();

        @Override
        public void insert(Integer key){
            table.insert(key, "");
        }

        @Override
        public boolean search(Integer key){
            return table.search(key) != null;
        }

        @Override
        public void delete(Integer key){
            table.delete(key);
        }

        @Override
        public int size(){
            return table.size();
        }

        @Override
        public String getName(){
            return "Chaining Hash Table"; 
        }
    }
    static class QuadraticProbingHashTableStructure implements Structure<Integer>{
        private final QuadraticProbingHashTable<Integer, String> table = new QuadraticProbingHashTable<>();

        @Override
        public void insert(Integer key){
            table.insert(key, "");
        }

        @Override
        public boolean search(Integer key){
            return table.search(key) != null;
        }

        @Override
        public void delete(Integer key){
            table.delete(key);
        }

        @Override
        public int size(){
            return table.size();
        }

        @Override
        public String getName(){
            return "Quadratic Probing Hash Table"; 
        }
    }

    public static int[] generateRandomData(int size){
        int[] data = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++){
            data[i] = rand.nextInt(size * 10);
        }
        return data;
    }

    public static long getMemUsage(){
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        return rt.totalMemory() - rt.freeMemory();
    }

    private static class PerformanceResult {
        long time;
        long memory;
        
        PerformanceResult(long time, long memory) {
            this.time = time;
            this.memory = memory;
        }
    }

    private static PerformanceResult insertionPerformance(Structure<Integer> struct, int[] data){
        // Record memory before operation
        long memoryBefore = getMemUsage();
        
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.insert(key);
        }
        long timeTaken = System.currentTimeMillis() - start;
        
        // Record memory after operation
        long memoryAfter = getMemUsage();
        long memoryUsed = memoryAfter - memoryBefore;
        
        return new PerformanceResult(timeTaken, memoryUsed);
    }

    private static PerformanceResult deletionPerformance(Structure<Integer> struct, int[] data){
        long memoryBefore = getMemUsage();
        
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.delete(key);
        }
        long timeTaken = System.currentTimeMillis() - start;
                
        long memoryAfter = getMemUsage();
        long memoryUsed = memoryAfter - memoryBefore;
        
        return new PerformanceResult(timeTaken, memoryUsed);
    }
    
    private static PerformanceResult searchPerformance(Structure<Integer> struct, int[] data){
        long memoryBefore = getMemUsage();
        
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.search(key);
        }
        long timeTaken = System.currentTimeMillis() - start;
        
        long memoryAfter = getMemUsage();
        long memoryUsed = memoryAfter - memoryBefore;
        
        return new PerformanceResult(timeTaken, memoryUsed);
    }

    public static void main(String[] args) {
        int[] testSizes = {1_000, 10_000, 100_000};
        int search = 5_000;
        for (int curSize : testSizes){
            int[] insertData = generateRandomData(curSize);
            int[] searchData = generateRandomData(search);
            int[] deleteData = generateRandomData(curSize / 2);

            @SuppressWarnings("unchecked")
            Structure<Integer>[] dataStructures = (Structure<Integer>[]) Array.newInstance(Structure.class, 4);
            dataStructures[0] = new AVLTreeStructure();
            dataStructures[1] = new SplayTreeStructure();
            dataStructures[2] = new ChainingHashTableStructure();
            dataStructures[3] = new QuadraticProbingHashTableStructure();

            System.out.println("Performance for " + curSize + "\n<====================================>");

            for (Structure<Integer> struct : dataStructures){
                System.out.println(struct.getName() + " information:");
                
                PerformanceResult insertResult = insertionPerformance(struct, insertData);
                PerformanceResult deleteResult = deletionPerformance(struct, deleteData);
                PerformanceResult searchResult = searchPerformance(struct, searchData);
                
                System.out.println("--------------------\nInsertion Time: " + insertResult.time + "ms");                                
                System.out.println("Deletion Time: " + deleteResult.time + "ms");
                System.out.println("Search Time: " + searchResult.time + "ms");
                
                System.out.println("--------------------\nInsertion Memory: " + insertResult.memory / 1_000_000 + "MB");
                System.out.println("Deletion Memory: " + deleteResult.memory / 1_000_000 + "MB");
                System.out.println("Search Memory: " + searchResult.memory / 1_000_000 + "MB");

                System.out.println("<====================================>");
            }
            System.out.println();
        }        
    }
}