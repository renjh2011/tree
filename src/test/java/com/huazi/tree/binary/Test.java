package com.huazi.tree.binary;

public class Test {
    public static void main(String[] args) {
        BinaryTree<Integer> root = new BinaryTree<>(10);
        BinaryTreeUtil.insert(root,4);
        BinaryTreeUtil.insert(root,20);
        BinaryTreeUtil.insert(root,22);
        BinaryTreeUtil.insert(root,15);
        BinaryTreeUtil.insert(root,13);
        BinaryTreeUtil.insert(root,18);
        BinaryTreeUtil.insert(root,17);
        BinaryTreeUtil.display(root,1);
        BinaryTreeUtil.delete(root,17);
        BinaryTreeUtil.display(root,1);
    }
}
