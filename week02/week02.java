package week02;

import java.util.*;

public class week02 {
    class Node {
        public int val;
        public List<Node> children;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, List<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     * LeetCode: 242 (有效的字母异位词)
     *
     * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] counter = new int[26];
        // 统计 t 的所有字符能否覆盖掉 s 的所有字符
        for (int i = 0; i < s.length(); i++) {
            counter[s.charAt(i) - 'a']++;
            counter[t.charAt(i) - 'a']--;
        }
        for (int count : counter) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * LeetCode: 1 (两数之和)
     *
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     */
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            int residual = target - nums[i];
            // 判断另一个不同的数是否在数组里
            if (map.containsKey(residual) && i != map.get(residual)) {
                return new int[]{i, map.get(residual)};
            }
        }
        return new int[0];
    }

    /**
     * LeetCode: 589 (N叉树的前序遍历)
     *
     * 给定一个 N 叉树，返回其节点值的前序遍历。
     */
    public List<Integer> preorder(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        preorderCompute(root, res);
        return res;
    }

    public void preorderCompute(Node root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        for (Node node : root.children) {
            preorderCompute(node, res);
        }
    }

    /**
     * LeetCode: 49 (字母异位词分组)
     *
     * 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。
     */

    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        if (strs.length == 0) {
            return res;
        }
        HashMap<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chs = str.toCharArray();
            // 将所有字符都排序，使所有 String 的顺序都一样
            Arrays.sort(chs);
            String temp = String.valueOf(chs);
            if (!map.containsKey(temp)) {
                map.put(temp, new ArrayList<>());
            }
            map.get(temp).add(str);
        }
        for (String str : map.keySet()) {
            res.add(map.get(str));
        }
        return res;
    }

    /**
     * LeetCode: 94 (二叉树的中序遍历)
     *
     * 给定一个二叉树，返回它的中序 遍历。
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        inorderCompute(root, res);
        return res;
    }

    public void inorderCompute(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorderCompute(root.left, res);
        res.add(root.val);
        inorderCompute(root.right, res);
    }

    /**
     * LeetCode: 144 (二叉树的前序遍历)
     *
     * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        preorderCompute2(root, res);
        return res;
    }

    public void preorderCompute2(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        inorderCompute(root.left, res);
        inorderCompute(root.right, res);
    }

    /**
     * LeetCode: 429 (N叉树的层序遍历)
     *
     * 给定一个 N 叉树，返回其节点值的层序遍历。 (即从左到右，逐层遍历)。
     */
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        // 遍历 queue 的节点，并将节点的孩子节点放入 queue
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < queue.size(); i++) {
                Node temp = queue.poll();
                level.add(temp.val);
                for (Node node : temp.children) {
                    if (node != null) {
                        queue.add(node);
                    }
                }
            }
            res.add(level);
        }
        return res;
    }

    /**
     * LeetCode: 264 (丑数)
     *
     * 我们把只包含质因子 2、3 和 5 的数称作丑数（Ugly Number）。求按从小到大的顺序的第 n 个丑数。
     */
    public int nthUglyNumber(int n) {
        int[] uglyNumber = {2, 3 ,5};
        PriorityQueue<Long> heap = new PriorityQueue<>();
        heap.offer(1L);
        int count = 0;
        while (!heap.isEmpty()) {
            long curNumber = heap.poll();
            if (++count >= n) {
                return (int) curNumber;
            }
            // 得出所有比当前值大的丑树
            for (int num : uglyNumber) {
                if (!heap.contains(num * curNumber)) {
                    heap.add(num * curNumber);
                }
            }
        }
        return -1;
    }

    /**
     * LeetCode: 347 (前 K 个高频元素)
     *
     * 给定一个非空的整数数组，返回其中出现频率前 k 高的元素。
     */
    public int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>(
                ((o1, o2) -> map.get(o1) - map.get(o2))
        );
        for (int num : map.keySet()) {
            heap.add(num);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        int[] res = new int[k];
        for (int i = 0; i < res.length; i++) {
            res[i] = heap.poll();
        }
        return res;
    }
}
