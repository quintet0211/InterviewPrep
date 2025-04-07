/*
Question: https://leetcode.com/problems/binary-tree-maximum-path-sum/description/?envType=company&envId=doordash&favoriteSlug=doordash-all.
He called leaf nodes "live nodes" and wanted the maximum sum of a path that starts and ends at a live node, which is basically the same as the Leetcode question. I solved it optimally, but was a bit slow on the follow up but finished the code.

*/

class Solution {
    private int maxLeafToLeafSum = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        helper(root);
        return maxLeafToLeafSum;
    }

    private int helper(TreeNode node) {
        if (node == null) return 0;

        // Recursively compute left and right path sums
        int left = helper(node.left);
        int right = helper(node.right);

        // If both children exist, this node can connect two leaves
        if (node.left != null && node.right != null) {
            int leafToLeafSum = left + right + node.val;
            maxLeafToLeafSum = Math.max(maxLeafToLeafSum, leafToLeafSum);

            // Return the best path up to parent using one side only
            return Math.max(left, right) + node.val;
        }

        // If only one child exists, return path through that child
        return (node.left != null ? left : right) + node.val;
    }
}

/*
Follow up: Now you can have "live nodes" anywhere in the tree, how would you modify the code so the maximum path still starts and ends at a "live node"? You cannot pass through a "live node".

*/

class Solution {
    private int maxSum = Integer.MIN_VALUE;

    public int maxLivePathSum(TreeNode root, Set<TreeNode> liveNodes) {
        dfs(root, liveNodes);
        return maxSum;
    }

    private int dfs(TreeNode node, Set<TreeNode> liveNodes) {
        if (node == null) return Integer.MIN_VALUE;

        boolean isLive = liveNodes.contains(node);

        // If it's a live node, treat it as a terminal (start/end only)
        if (isLive) {
            int value = node.val;
            maxSum = Math.max(maxSum, value);
            return value; // But do NOT allow propagation through this node
        }

        int left = dfs(node.left, liveNodes);
        int right = dfs(node.right, liveNodes);

        // Only valid paths come from children that return meaningful values
        int maxSingle = node.val;
        if (left != Integer.MIN_VALUE) maxSingle = Math.max(maxSingle, node.val + left);
        if (right != Integer.MIN_VALUE) maxSingle = Math.max(maxSingle, node.val + right);

        // Combine if both sides returned valid paths
        if (left != Integer.MIN_VALUE && right != Integer.MIN_VALUE) {
            int combined = left + node.val + right;
            maxSum = Math.max(maxSum, combined);
        }

        return maxSingle;
    }
}
