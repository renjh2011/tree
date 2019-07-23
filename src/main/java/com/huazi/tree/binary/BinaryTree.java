package com.huazi.tree.binary;

public class BinaryTree<T extends Comparable<T>> {
    private BinaryTree<T> left;
    private BinaryTree<T> right;
    private T data;
    private int level=0;
    private BinaryTree<T> parent;

    public BinaryTree(T data) {
        this.data = data;
    }

    public BinaryTree<T> getLeft() {
        return left;
    }

    public void setLeft(BinaryTree<T> left) {
        this.left = left;
    }

    public BinaryTree<T> getRight() {
        return right;
    }

    public void setRight(BinaryTree<T> right) {
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public BinaryTree<T> getParent() {
        return parent;
    }

    public void setParent(BinaryTree<T> parent) {
        this.parent = parent;
    }
}
