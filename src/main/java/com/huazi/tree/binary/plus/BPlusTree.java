package com.huazi.tree.binary.plus;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class BPlusTree<Key extends Comparable<Key>, Value> {
    /**
     * btree的阶
     */
    private static final int M = 100;
    /**
     * root节点
     */
    private Node root;
    /**
     * btree的高度
     */
    private int height;
    /**
     * 每个节点的长度
     */
    private int num;

    private static final class Node {
        /**
         * 孩子的个数
         */
        private int len;
        /**
         * 孩子数组
         */
        private Entry[] children = new Entry[M];

        /**
         * 创建一个长度为k的节点
         * @param k
         */
        private Node(int k) {
            len = k;
        }
    }

    /**
     * 节点的数据
     */
    private static class Entry{
        private Comparable key;
        private final Object val;
        private Node next;
        private Node parent;

        public Entry(Comparable key, Object val,Node next,Node parent) {
            this.key = key;
            this.val = val;
            this.next = next;
            this.parent = parent;
        }
    }

    public BPlusTree() {
        root = new Node(0);
    }
    public int size() {
        return num;
    }
    public int height() {
        return height;
    }

    public Value get(Key key) {
        if (key == null){
            throw new IllegalArgumentException("argument to get() is null");
        }
        return search(root, key, height);
    }

    private Value search(Node node, Key key, int ht) {
        Entry[] children = node.children;
        Entry keyEntry = new Entry(key,null,null,null);

        //叶子节点
        if (ht == 0) {
            int loc = Arrays.binarySearch(children, 0, node.len, keyEntry, Comparator.comparing(o -> o.key));
            if(loc>=0){
                return (Value) children[loc].val;
            }
        }
        //非叶子节点
        else {
            int loc = Arrays.binarySearch(children, 1, node.len, keyEntry,Comparator.comparing(o -> o.key));
            int index = loc>=0 ? loc : -1-loc-1;
            if(index <= node.len && index>=0){
                return search(children[index++].next,key,ht-1);
            }
        }
        return null;
    }

    private int comparing(Comparable k1, Comparable k2) {
        return k1.compareTo(k2);
    }

    /**
     * 插入一个节点
     * @param key
     * @param val
     */
    public void put(Key key, Value val) {
        if (key == null){
            throw new IllegalArgumentException("argument key to put() is null");
        }
        Node u = insert(root, key, val, height);
        num++;
        if (u == null) {
            return;
        }

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root,null);
        t.children[1] = new Entry(u.children[0].key, null, u,null);
        root = t;
        height++;
    }

    private Node insert(Node node, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry(key, val, null,null);

        Entry[] children = node.children;
        Entry keyEntry = new Entry(key,null,null,null);
        //叶子节点
        if (ht == 0) {
            /*
             * 如果是叶子节点 找到key对应的下标
             */
            int loc = Arrays.binarySearch(children, 0, node.len, keyEntry, Comparator.comparing(o -> o.key));
            j = loc>=0?loc:-1-loc;
//            for (j = 0; j < node.len.; j++) {
//                if (comparing(key, h.children[j].key)<0) break;
//            }
        }
        //非叶子节点
        else {
            int loc = Arrays.binarySearch(children, 1, node.len, keyEntry, Comparator.comparing(o -> o.key));
            int index = loc>=0 ? loc : -1-loc-1;
            if(index < node.len && index>=0){
                Node tempNode = insert( children[index++].next,key,val,ht-1);
                if(tempNode==null){
                    return null;
                }
                t.key = tempNode.children[0].key;
                t.next = tempNode;
            }
            j=index;
        }

        for (int i = node.len; i > j; i--) {
            try {
                node.children[i] = node.children[i - 1];
            }catch (Exception e){
                System.out.println(node.len+"--"+i+"--"+j);
            }
        }
        node.children[j] = t;
        node.len++;
        if (node.len < M) {
            return null;
        }
        else {
            return split(node);
        }
    }

    private Node split(Node node) {
        Node t = new Node(1);
        node.len = M-1;
        t.children[0] = node.children[M-1];
        return t;
    }

    public Node getRoot() {
        return root;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.len; j++) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        }
        else {
            for (int j = 0; j < h.len; j++) {
                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
                s.append(toString(children[j].next, ht-1, indent + "     "));
            }
        }
        return s.toString();
    }

    public static void main(String[] args) {
        BPlusTree<Integer, String> st = new BPlusTree<Integer, String>();
//        int[] test = {1,3,8,20,60,4,2,9,8,5,22,65,80,54,63};
        int[] test = {100,8,20,60,4,2,9,8,5,22,65,80,54,63,1};
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            Random random = new Random(i);
            st.put(Math.abs(random.nextInt()), i + "");
//            st.put(i, i + "");
//            st.put(test[i], test[i] + "");
        }
        long end = System.currentTimeMillis();
//        System.out.println(st);
        System.out.println("args = [" + (end-start) + "]");
        start = System.currentTimeMillis();
        String s = st.search(st.root,5635435,st.height);
        end = System.currentTimeMillis();
        System.out.println((end-start)+"---"+st.getHeight() + "]");
    }
}
