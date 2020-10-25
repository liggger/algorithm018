# 学习笔记

## 改写 Deque

```java
/**
     * 改写 Deque
     */
    public static void updateDeque() {
        Deque<String> deque = new LinkedList<>();
        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");
        System.out.println(deque);

        String str = deque.peek();
        System.out.println(str);
        System.out.println(deque);

        while (deque.size() > 0) {
            System.out.println(deque.removeFirst());
        }
        System.out.println(deque);
    }
```

## Queue

### 简介

队列是一种特殊的线性表，它只允许在表的前端（front）进行删除操作，而在表的后端（rear）进行插入操作。进行插入操作的端称为队尾，进行删除操作的端称为队头。队列中没有元素时，称为空队列。

Java Queue是java.util包中提供的接口，并扩展了java.util.Collection接口。

就像Java List一样，Java Queue是有序元素（或对象）的集合，但它以不同方式执行插入和删除操作。 在处理这些元素之前，我们可以使用Queue存储元素。

### 源码

> Java Queue 提供以下方法：

```java
boolean add(E e)： 如果可以在不违反容量限制的情况下立即执行此操作，则将指定的元素插入此队列，成功时返回true，如果当前没有可用空间则抛出IllegalStateException。

boolean offer(E e)： 如果可以在不违反容量限制的情况下立即执行此操作，则将指定的元素插入此队列，成功时返回true，如果当前没有可用空间则返回 false。

E remove()：检索并删除此队列的头部元素。 此方法与poll的不同之处仅在于，如果此队列为空，则抛出异常 NoSuchElementException。

E poll()： 检索并删除此队列的头部，如果此队列为空，则返回null。

E element()：检索但不删除此队列的头部。 此方法与peek的不同之处仅在于，如果此队列为空，则抛出异常 NoSuchElementException。

E peek()：检索但不移除此队列的头部，如果此队列为空，则返回null。
```

## Priority Queue

### 简介

PriorityQueue 是 Queue 的一个继承者，相比于一般的列表，它的特点便如它的名字一样，出队的时候可以按照优先级进行出队，所以不像 LinkedList 那样只能按照插入的顺序出队，PriorityQueue 是可以根据给定的优先级顺序进行出队的。这里说的给定优先级顺序既可以是内部比较器，也可以是外部比较器。PriorityQueue 内部是根据小顶堆的结构进行存储的，所谓小顶堆的意思，便是最小的元素总是在最上面，每次出队总是将堆顶元素移除，这样便能让出队变得有序。

### 源码

> PriorityQueue的内部结构其实是按照小顶堆的结构进行存储的，具体源码如下

**初始化**

```java
    // 默认初始化容量
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    /**
     * 优先级队列是使用平衡二叉堆表示的: 节点queue[n]的两个孩子分别为
     * queue[2*n+1] 和 queue[2*(n+1)].  队列的优先级是由比较器或者
     * 元素的自然排序决定的， 对于堆中的任意元素n，其后代d满足：n<=d
     * 如果堆是非空的，则堆中最小值为queue[0]。
     */
    transient Object[] queue; 

    /**
     * 队列中元素个数
     */
    private int size = 0;

    /**
     * 比较器
     */
    private final Comparator<? super E> comparator;

    /**
     * 修改次数
     */
    transient int modCount = 0; 
```

**构造函数**

```java
    /**
     * 使用默认的容量（11）来构造一个空的优先级队列，使用元素的自然顺序进行排序（此时元素必须实现comparable接口）
     */
    public PriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * 使用指定容量来构造一个空的优先级队列，使用元素的自然顺序进行排序（此时元素必须实现comparable接口）
     * 但如果指定的容量小于1则会抛出异常
     */
    public PriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * 使用默认的容量（11）构造一个优先级队列，使用指定的比较器进行排序
     */
    public PriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /**
     * 使用指定容量创建一个优先级队列，并使用指定比较器进行排序。
     * 但如果指定的容量小于1则会抛出异常
     */
    public PriorityQueue(int initialCapacity,
                         Comparator<? super E> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;
    }

    /**
     * 使用指定集合的所有元素构造一个优先级队列，
     * 如果该集合为SortedSet或者PriorityQueue类型，则会使用相同的顺序进行排序，
     * 否则，将使用元素的自然排序（此时元素必须实现comparable接口），否则会抛出异常
     * 并且集合中不能有null元素，否则会抛出异常
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(Collection<? extends E> c) {
        if (c instanceof SortedSet<?>) {
            SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
            this.comparator = (Comparator<? super E>) ss.comparator();
            initElementsFromCollection(ss);
        }
        else if (c instanceof PriorityQueue<?>) {
            PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator();
            initFromPriorityQueue(pq);
        }
        else {
            this.comparator = null;
            initFromCollection(c);
        }
    }

    /**
     * 使用指定的优先级队列中所有元素来构造一个新的优先级队列.  将使用原有顺序进行排序。
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(PriorityQueue<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initFromPriorityQueue(c);
    }

    /**
     * 根据指定的有序集合创建一个优先级队列，将使用原有顺序进行排序
     */
    @SuppressWarnings("unchecked")
    public PriorityQueue(SortedSet<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initElementsFromCollection(c);
    }
```

从集合中构造优先级队列的时候，调用了几个初始化函数：

```java
    private void initFromPriorityQueue(PriorityQueue<? extends E> c) {
        if (c.getClass() == PriorityQueue.class) {
            this.queue = c.toArray();
            this.size = c.size();
        } else {
            initFromCollection(c);
        }
    }

    private void initElementsFromCollection(Collection<? extends E> c) {
        Object[] a = c.toArray();
        // If c.toArray incorrectly doesn't return Object[], copy it.
        if (a.getClass() != Object[].class)
            a = Arrays.copyOf(a, a.length, Object[].class);
        int len = a.length;
        if (len == 1 || this.comparator != null)
            for (int i = 0; i < len; i++)
                if (a[i] == null)
                    throw new NullPointerException();
        this.queue = a;
        this.size = a.length;
    }

    private void initFromCollection(Collection<? extends E> c) {
        initElementsFromCollection(c);
        heapify();
    }
```

initFromPriorityQueue 即从另外一个优先级队列构造一个新的优先级队列，此时内部的数组元素不需要进行调整，只需要将原数组元素都复制过来即可。但是从其他非 PriorityQueue 的集合中构造优先级队列时，需要先将元素复制过来后再进行调整，此时调用的是heapify 方法：

```java
    private void heapify() {
        // 从最后一个非叶子节点开始从下往上调整
        for (int i = (size >>> 1) - 1; i >= 0; i--)
            siftDown(i, (E) queue[i]);
    }

    // 划重点了，这个函数即对应上面的元素删除时从上往下调整的步骤
    private void siftDown(int k, E x) {
        if (comparator != null)
            // 如果比较器不为null，则使用比较器进行比较
            siftDownUsingComparator(k, x);
        else
            // 否则使用元素的compareTo方法进行比较
            siftDownComparable(k, x);
    }

    private void siftDownUsingComparator(int k, E x) {
        // 使用half记录队列size的一半，如果比half小的话，说明不是叶子节点
        // 因为最后一个节点的序号为size - 1，其父节点的序号为(size - 2) / 2或者(size - 3 ) / 2
        // 所以half所在位置刚好是第一个叶子节点
        int half = size >>> 1;
        while (k < half) {
            // 如果不是叶子节点，找出其孩子中较小的那个并用其替换
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                comparator.compare((E) c, (E) queue[right]) > 0)
                c = queue[child = right];
            if (comparator.compare(x, (E) c) <= 0)
                break;
            // 用c替换
            queue[k] = c;
            k = child;
        }
        // 
        queue[k] = x;
    }
    // 同上，只是比较的时候使用的是元素的compareTo方法
    private void siftDownComparable(int k, E x) {
        Comparable<? super E> key = (Comparable<? super E>)x;
        int half = size >>> 1;        // 如果是非叶子节点则继续循环
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                c = queue[child = right];
            if (key.compareTo((E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = key;
    }
```