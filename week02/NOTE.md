## HashMap

### 简介

> HashMap是用于映射(键值对)处理的数据类型。随着JDK（Java Developmet Kit）版本的更新，JDK1.8对HashMap底层的实现进行了优化，例如引入红黑树的数据结构和扩容的优化等。

HashMap 根据键的 hashCode 值存储数据，大多数情况下可以直接定位到它的值，因而具有很快的访问速度，但遍历顺序却是不确定的。 HashMap 最多只允许一条记录的键为 null，允许多条记录的值为 null。HashMap 非线程安全，即任一时刻可以有多个线程同时写 HashMap，可能会导致数据的不一致。如果需要满足线程安全，可以用 Collections的synchronizedMap 方法使 HashMap 具有线程安全的能力，或者使用 ConcurrentHashMap。

从结构实现来讲，HashMap是数组+链表+红黑树（JDK1.8增加了红黑树部分）实现的，数组是 Node[] table，即哈希桶数组。Node是HashMap的一个内部类，实现了Map.Entry接口，本质是就是一个映射(键值对)。上图中的每个黑色圆点就是一个Node对象。

Node[] table 的初始化长度length(默认值是16)，Load factor 为负载因子(默认值是0.75)，threshold 是 HashMap 所能容纳的最大数据量的 Node (键值对)个数。threshold = length * Load factor。也就是说，在数组定义好长度之后，负载因子越大，所能容纳的键值对个数越多。在HashMap 中，哈希桶数组 table 的长度 length 大小必须为2的n次方，这种设计主要是为了在取模和扩容时做优化，同时为了减少冲突，HashMap 定位哈希桶索引位置时，也加入了高位参与运算的过程。

### HashMap 实现

**1. 确定哈希桶数组索引位置**

Hash 算法本质上就是三步：取 key 的 hashCode 值、高位运算、取模运算。

```java
// 方法一
static final int hash(Object key) {   //jdk1.8 & jdk1.7
     int h;
     // h = key.hashCode() 为第一步 取hashCode值
     // h ^ (h >>> 16)  为第二步 高位参与运算
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
// 方法二
static int indexFor(int h, int length) {  //jdk1.7的源码，jdk1.8没有这个方法，但是实现原理一样的
     return h & (length-1);  //第三步 取模运算
}
```

对于任意给定的对象，只要它的 hashCode() 返回值相同，那么程序调用方法一所计算得到的 Hash 码值总是相同的。一般上是把 hash 值对数组长度取模运算，这样一来，元素的分布相对来说是比较均匀的。但是，模运算的消耗还是比较大的，在HashMap中是这样做的：调用方法二来计算该对象应该保存在table数组的哪个索引处。它通过 `h & (table.length -1)` 来得到该对象的保存位，而 HashMap 底层数组的长度总是2的n次方。当length总是2的n次方时，`h& (length-1)` 运算等价于对 length 取模，也就是 `h%length` ，但是&比%具有更高的效率。

**2. HashMap 的 put 方法**

1. 判断键值对数组 table[i] 是否为空或为null，否则执行resize() 进行扩容；

2. 根据键值 key 计算 hash 值得到插入的数组索引i，如果 table[i]==null，直接新建节点添加，转向6，如果 table[i] 不为空，转向③；

3. 判断 table[i] 的首个元素是否和key一样，如果相同直接覆盖 value，否则转向④，这里的相同指的是 hashCode 以及 equals；

4. 判断table[i] 是否为 treeNode，即 table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对，否则转向5；

5. 遍历 table[i]，判断链表长度是否大于8，大于8的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作；遍历过程中若发现key已经存在直接覆盖 value 即可；

6. 插入成功后，判断实际存在的键值对数量size是否超多了最大容量threshold，如果超过，进行扩容。

**3. 扩容**

扩容(resize) 就是重新计算容量，向 HashMap 对象里不停的添加元素，而 HashMap 对象内部的数组无法装载更多的元素时，对象就需要扩大数组的长度，以便能装入更多的元素。当然Java里的数组是无法自动扩容的，方法是使用一个新的数组代替已有的容量小的数组，就像我们用一个小桶装水，如果想装更多的水，就得换大水桶。

## 堆

### 简介

通俗来说，堆可以被看作是一棵树的数组对象，堆中某个节点的值总是不大于或不小于其父节点的值。堆（二叉堆）可以视为一棵完全的二叉树。完全二叉树的一个优秀的性质就是，除了最底层之外，每一层都是满的，这使得堆可以利用数组来表示（一般的二叉树通常用链表作为基本容器表示），每一个结点对应数组中的一个元素。

**而二叉堆一般分为两种：最大堆和最小堆。**

* 最大堆：最大堆中的最大元素在根结点（堆顶）；堆中每个父节点的元素值都大于等于其子结点（如果子节点存在）

* 最小堆：最小堆中的最小元素出现在根结点（堆顶）；堆中每个父节点的元素值都小于等于其子结点（如果子节点存在）

### 堆排序

> 堆排序，就是利用堆的特征来对目标输入数组进行排序；依据目的是从大到小还是从小到大，我们可以相应地用最大堆排序或则最小堆排序来取出堆顶的最大数或最小数，再将剩下的堆调整为最大堆或最小堆；整个过程持续迭代至只有一个数剩下。

堆排序过程：

* **输入**：一系列的无序元素（比如说，数字）组成的输入数组A

* **经过**：堆排序的过程可以具体分为三步，创建堆，调整堆，堆排序。

	1. 创建堆，以数组的形式将堆中所有的数据重新排序，使其成为最大堆/最小堆。

	2. 调整堆，调整过程需要保证堆序性质：在一个二叉堆中任意父节点大于其子节点。

	3. 堆排序，取出位于堆顶的第一个数据（最大堆则为最大数，最小堆则为最小数），放入输出数组B 中，再将剩下的对作调整堆的迭代/重复运算直至输入数组 A中只剩下最后一个元素。

* **输出**：输出数组B，里面包含的元素都是A 中的但是已经按照要求排好了顺序

```java
public class HeapSort 
{ 
    public void sort(int arr[]) 
    { 
        int n = arr.length; 
  
        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapify(arr, n, i); 
  
        // One by one extract an element from heap 
        for (int i=n-1; i>0; i--) 
        { 
            // Move current root to end 
            int temp = arr[0]; 
            arr[0] = arr[i]; 
            arr[i] = temp; 
  
            // call max heapify on the reduced heap 
            heapify(arr, i, 0); 
        } 
    } 
  
    // To heapify a subtree rooted with node i which is 
    // an index in arr[]. n is size of heap 
    void heapify(int arr[], int n, int i) 
    { 
        int largest = i; // Initialize largest as root 
        int l = 2*i + 1; // left = 2*i + 1 
        int r = 2*i + 2; // right = 2*i + 2 
  
        // If left child is larger than root 
        if (l < n && arr[l] > arr[largest]) 
            largest = l; 
  
        // If right child is larger than largest so far 
        if (r < n && arr[r] > arr[largest]) 
            largest = r; 
  
        // If largest is not root 
        if (largest != i) 
        { 
            int swap = arr[i]; 
            arr[i] = arr[largest]; 
            arr[largest] = swap; 
  
            // Recursively heapify the affected sub-tree 
            heapify(arr, n, largest); 
        } 
    } 
  
    /* A utility function to print array of size n */
    static void printArray(int arr[]) 
    { 
        int n = arr.length; 
        for (int i=0; i<n; ++i) 
            System.out.print(arr[i]+" "); 
        System.out.println(); 
    } 
  
    // Driver program 
    public static void main(String args[]) 
    { 
        int arr[] = {12, 11, 13, 5, 6, 7}; 
        int n = arr.length; 
  
        HeapSort ob = new HeapSort(); 
        ob.sort(arr); 
  
        System.out.println("Sorted array is"); 
        printArray(arr); 
    } 
} 
```