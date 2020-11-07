package week03;

import javax.swing.tree.TreeNode;
import java.util.*;

public class week03 {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    /**
     * LeetCode: 236 (二叉树的最近公共祖先)
     *
     * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
     *
     * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
     *
     * 复杂度：
     * - 时间复杂度：O(n)
     * - 空间复杂度：O(n)
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        HashMap<TreeNode, TreeNode> parent = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        parent.put(root, null);
        queue.add(root);
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                parent.put(node.left, node);
                queue.add(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                queue.add(node.right);
            }
        }
        Set<TreeNode> ancestors = new HashSet<>();
        while (p != null) {
            ancestors.add(p);
            p = parent.get(p);
        }
        while (!ancestors.contains(q)) {
            q = parent.get(q);
        }
        return q;
    }

    /**
     * LeetCode: 105 (从前序与中序遍历序列构造二叉树)
     *
     * 根据一棵树的前序遍历与中序遍历构造二叉树。
     *
     * 注意:
     * 你可以假设树中没有重复的元素。
     *
     * 复杂度：
     * - 时间复杂度：O(n)
     * - 空间复杂度：O(n)
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int[] current = {0};

        TreeNode root;
        // 记录中序遍历数组的值与索引值
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        // 递归建树
        root = buildTree(preorder, current, 0, preorder.length - 1, map);
        return root;
    }

    public TreeNode buildTree (int[] preoreder, int[] current, int low, int high, HashMap<Integer, Integer> map) {
        // 判断是否已经建好树
        if (current[0] >= preoreder.length) {
            return null;
        }
        TreeNode root = new TreeNode(preoreder[current[0]]);
        if (low > high) {
            return null;
        } else {
            current[0] += 1;
            // 找到中序遍历的根节点对应的索引值
            int i = map.get(root.val);
            root.left = buildTree(preoreder, current, low, i - 1, map);
            root.right = buildTree(preoreder, current, i + 1, high, map);
        }
        return root;
    }

    /**
     * LeetCode: 77 (组合)
     * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
     *
     * 复杂度：
     * - 时间复杂度：O(n 的 n 次方)
     * - 空间复杂度：O(n)
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        // 记录当前路径
        List<Integer> track = new ArrayList<>();
        backtrack(n, k, 1, res, track);
        return res;
    }

    public void backtrack(int n, int k, int start, List<List<Integer>> res, List<Integer> track) {
        // 满了就记录当前路径
        if (track.size() == k) {
            res.add(new ArrayList<>(track));
        }
        // 记录每个值
        for (int i = start; i <= n; i++) {
            track.add(i);
            backtrack(n, k, i + 1, res, track);
            track.remove(track.size() - 1);
        }
    }

    /**
     * LeetCode: 46 (全排列)
     * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
     *
     * 复杂度：
     * - 时间复杂度：O(n × n!)，其中 n 为序列的长度。
     * - 空间复杂度：O(n)
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        // 记录当前路径
        ArrayList<Integer> track = new ArrayList<>();
        backtrack(nums, res, track);
        return res;
    }

    void backtrack(int[] nums, List<List<Integer>> res, ArrayList<Integer> track) {
        if(track.size() == nums.length) {
            res.add(new ArrayList<>(track));
            return;
        }
        // 记录每个值，不包括已有的
        for (int num : nums) {
            if (track.contains(num)) {
                continue;
            }
            track.add(num);
            backtrack(nums, res, track);
            track.remove(track.size() - 1);
        }
    }

    /**
     * LeetCode: 47 (全排列 II)
     * 给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
     *
     * 复杂度：
     * - 时间复杂度：O(n × n!)，其中 n 为序列的长度。
     * - 空间复杂度：O(n)
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if(nums.length == 0) {
            return res;
        }
        // 排序
        Arrays.sort(nums);
        ArrayList<Integer> track = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        backtrack(nums, res, track, visited);
        return res;
    }

    void backtrack(int[] nums, List<List<Integer>> res, ArrayList<Integer> track, boolean[] visited) {
        if(track.size() == nums.length) {
            res.add(new ArrayList<>(track));
            return;
        }

        for(int i = 0; i < nums.length; i++) {
            if(visited[i]) {
                continue;
            }
            // 剪枝
            if(i > 0 && nums[i] == nums[i - 1] && visited[i - 1]) {
                continue;
            }
            track.add(nums[i]);
            visited[i] = true;
            backtrack(nums, res, track, visited);
            track.remove(track.size() - 1);
            visited[i] = false;
        }
    }
}
