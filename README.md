# Assignment 2 Report
Name: `Jonathan Rodriguez`
NetID: `jdr220004`
Section: `501`


## Project Setup
- **Test Environment**: Java 8, **CPU**: Ryzen 5 7600X, **MEMORY**: 32GB 5600Mhz
- **Test Data**: Random integers from 0 - 1_000_000
- **Operations**: Insert, Delete, Search
- **Test Sizes**: 1_000, 10_000, 100_000

## Implementation

### AVL Tree
- Basic node class with left and right children, with keys and height as data values
- Balancing `O(logn)`: uses a balance factor to track height for each node (left - right)
- Insertion `O(logn)`: recursively finds insertion points | updates heights and rebalances with 4 cases, double left, left right, double right, and right left
- Deletion `O(logn)`: finds nodes in the same manner as a BST | handles 3 deletion cases, no children (simple deletion), one child (replace with that child), two children (replace with inorder successor) | rebalances after deletion is done
- Search `O(logn)`: basic BST search algorithm

### Splay Tree
- Node class with left and right children, with keys and parent pointers as data
- Insertion `O(logn)`: BST insertion | splays new node to the root using rotations | handles duplicates by splaying existing node
- Deletion `O(logn)`: splays target to root node | 3 cases, no left child (replace with right tree), no right child (replace with left tree), two children (find max in left, splay to root, attach right tree)
- Search `O(logn)`: BST search | found node, or last accessed in the case of not found, gets splayed to the top | uses the working set properly so that frequent accesses are faster

### Chaining Hash Table
- Stores the Key Value pairs in linked list buckets
- Uses chaining to handle the hash collisions
- Load factor set to .75 so the array resizes at 75% full
- Doubles capacity when threshold is reached, halves when 25% full
- Insertion `O(1)`: hashes key to find its bucket index | Key exists (updates value), or New key (adds to linked list) | triggers the resize function if the load factor is exceeded
- Deletion `O(1)`: hashes to find bucket | removes entry from linked list if found | downsizes if table utilization goes below 25%
- Search `O(1)`: hashes to find bucket | finds matching key in linked list | returns null if not found
- Resizing `O(n)`: creates new table with either doubles or halved capacity | rehashes existing entries | mantains same key-to-value pair

### Quadratic Probing Hash Table
- Entry class stores key value pairs with a deletion boolean check
- Uses probing for open addressing
- Load factor set to .5 (more conservative than chaining)
- Entries are marked as deleted insread of being removed immediately
- Insertion `O(1)`: hashes given key to find position | probes using quadratic sequence for 3 cases, empty slot (inserts new entry), duplicate key (updates value), tombstone or deleted boolean found (reuses slot) | resizes when the threshold is reached
- Deletion `O(1)`: finds entry with probing | marks as deleted using the boolean (tombstone) instead of removing | downsizes table when utilization drops below 25%
- Search `O(1)`: follows same probing sequence | skips deleted boolean (tombstone) | returns null if key is not found
- Resizing `O(n)`: creates a new table, ignoring the tombstones | rehashes all active entries | maintains the same probing behavior in the newly created table


## Performance Results

The results shown are the averages of 10 runs

### Insertion (ms)

| Structure | 1_000 | 10_000 | 100_000 |
|-----------|-------|--------|----------|
|AVL Tree | 1 | 3 | 32|
|Splay Tree | 1 | 4 | 41|
|Chaining Hash Table | 2 | 3 | 22|
|Quadratic Probing Hash Table | 0 | 3 | 18|

### Deletion (ms)

| Structure | 1_000 | 10_000 | 100_000 |
|-----------|-------|--------|----------|
|AVL Tree | 0 | 1 | 16|
|Splay Tree | 1 | 2 | 28|
|Chaining Hash Table | 1 | 1 | 8|
|Quadratic Probing Hash Table | 1 | 2 | 6|

### Search (ms)

| Structure | 1_000 | 10_000 | 100_000 |
|-----------|-------|--------|----------|
|AVL Tree | 1 | 1 | 1|
|Splay Tree | 0 | 2 | 1|
|Chaining Hash Table | 1 | 1 | 1|
|Quadratic Probing Hash Table | 1 | 1 | 1|

### Memory Usage (MB)

| Structure | 1_000 | 10_000 | 100_000 |
|-----------|-------|--------|----------|
|AVL Tree | 3 | 5 | 15|
|Splay Tree | 3 | 5| 18|
|Chaining Hash Table | 4 | 9 | 18|
|Quadratic Probing Hash Table | 5 | 12 | 37|

## Observations

### Insertion

- Overall, both Hash Tables outperform both the AVL Tree and Splay Tree with the Probing being the fastest at just 2ms of insertion time (10_000)
- I would assume that due to the strict balancing rules of the AVL Tree, it outperforms the Splay Tree

### Deletion

- Quadratic Probing is by far the best at deletion, followed up by Chaining
- Both trees are the slowest as they have to rebalance themselves for every single deletion

### Search

- Nearly all structures have almost instant search times 

### Memory Usage

- At all levels, the 2 trees use less memory than the hash tables since they do not reserve any more memory than they need to
- Quadratic Probing uses almost double the memory than chaining for the largest data set, 100_000

## Graphs

![alt text](image.png)