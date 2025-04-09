
import java.util.ArrayList;
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

    public static ArrayList<Integer> generateRandomData(int size){
        ArrayList<Integer> data = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < size; i++){
            data.add(rand.nextInt(size * 10));
        }
        return data;
    }

    public static long getMemUsage(){
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private static long insertionTime(Structure<Integer> struct, ArrayList<Integer> data){
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.insert(key);
        }
        return System.currentTimeMillis() - start;
    }

    private static long deletionTime(Structure<Integer> struct, ArrayList<Integer> data){
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.delete(key);
        }
        return System.currentTimeMillis() - start;
    }
    
    private static long searchTime(Structure<Integer> struct, ArrayList<Integer> data){
        long start = System.currentTimeMillis();
        for (Integer key : data){
            struct.search(key);
        }
        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) {
        int[] testSizes = {1_000, 10_000, 100_000};
        int search = 5_000;
        for (int curSize : testSizes){
            ArrayList<Integer> insertData = generateRandomData(curSize);
            ArrayList<Integer> searchData = generateRandomData(search);
            ArrayList<Integer> deleteData = new ArrayList<>(insertData.subList(0, insertData.size() / 2));

            ArrayList<Structure<Integer>> dataStructures = new ArrayList<>();
                dataStructures.add(new AVLTreeStructure());
                dataStructures.add(new SplayTreeStructure());
                dataStructures.add(new ChainingHashTableStructure());
                dataStructures.add(new QuadraticProbingHashTableStructure());

            System.out.println("\t<Performance for " + curSize + ">\n<====================================>");

            for (Structure<Integer> struct : dataStructures){
                System.out.println(struct.getName() + " information:");
                System.out.println("Insertion Time: " + insertionTime(struct, insertData) + "ms");
                System.out.println("Deletion Time: " + deletionTime(struct, deleteData) + "ms");               
                System.out.println("Search Time: " + searchTime(struct, searchData) + "ms\nMemory Usage: " + getMemUsage() / 1000000 + "MB\n<====================================>");
            }
            System.out.println();
        }        
    }
}
