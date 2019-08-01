package com.huazi.tree.binary.btreetest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BPlusTree<Key extends Comparable<Key>, Value> {
    /**
     * btree的阶
     */
    private static final int M = 101;
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
        private Node[] children;
        private Comparable[] key;
        private Object[] val;

        private Node next;
        private Node prev;

        /**
         * 创建一个长度为k的节点
         * @param k
         */
        private Node(int k) {
            len = k;
            children = new Node[M];
            key = new Comparable[M];
//            val = new Object[M];
        }


    }


    public BPlusTree() {
        root = new Node(0);
        root.val=new Object[M];
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
        //叶子节点
        if (ht == 0) {
            int loc = Arrays.binarySearch(node.key, 0, node.len, key);
            if(loc>=0){
                return (Value) node.val[loc];
            }
        }
        //非叶子节点
        else {
            int loc = Arrays.binarySearch(node.key, 1, node.len, key);
            int index = loc>=0 ? loc : -1-loc-1;
            if(index <= node.len && index>=0){
                return search(node.children[index++],key,ht-1);
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
        t.key[0] = root.key[0];
        if(height==0) {
            t.val = new Object[M];
            t.val[0] = root.val[0];
        }
        t.children[0] = root;
        t.key[1] = u.key[0];
        if(height==0) {
            t.val[1] = u.val[0];
        }
        t.children[1] = u;
        root = t;
        height++;
    }

    private Node insert(Node node, Key key, Value val, int ht) {
        int j;
        Node t = new Node(1);
        if(ht==0){
            t.val=new Object[M];
        }
        //叶子节点
        if (ht == 0) {
            /*
             * 如果是叶子节点 找到key对应的下标
             */
            t.val=new Object[M];
            int loc = Arrays.binarySearch(node.key, 0, node.len, key);
            j = loc>=0?loc:-1-loc;
            t.key[j]=key;
            t.val[j]=val;
        }
        //非叶子节点
        else {
            int loc = Arrays.binarySearch(node.key, 1, node.len, key);
            int index = loc>=0 ? loc : -1-loc-1;
            if(index < node.len && index>=0){
                Node tempNode = insert( node.children[index++],key,val,ht-1);
                if(tempNode==null){
                    return null;
                }
                t.key[index]=tempNode.key[0];
//                t.val[index]=tempNode.val[0];
                t.children[index]=tempNode;
            }
            j=index;
        }


        if(node.len>j) {
            System.arraycopy(node.key, j, node.key, j + 1, node.len-j);
            if(ht==0) {
                System.arraycopy(node.val, j, node.val, j + 1, node.len - j);
            }
            System.arraycopy(node.children, j, node.children, j + 1, node.len-j);
        }

        node.key[j]=t.key[j];
        if(ht==0) {
            node.val[j] = t.val[j];
        }
        node.children[j]=t.children[j];
        node.len++;
        if (node.len < M) {
            return null;
        }
        else {
            return split(node,ht);
        }
    }

    private Node split(Node node, int ht) {
        Node t = new Node(1);
        node.len = M-1;
        t.key[0] = node.key[M-1];
        if(ht==0) {
            t.val=new Object[M];
            t.val[0] = node.val[M - 1];
        }
        t.children[0] = node.children[M-1];
        if(ht==0){
            t.next=node.next;
            if(t.next!=null) {
                t.next.prev=t;
            }
            node.next=t;
            t.prev=node;
        }
        return t;
    }

    public Node getRoot() {
        return root;
    }

    public int getHeight() {
        return height;
    }

//    @Override
//    public String toString() {
//        return toString(root, height, "") + "\n";
//    }
//
//    private String toString(Node h, int ht, String indent) {
//        StringBuilder s = new StringBuilder();
//        Entry[] children = h.children;
//
//        if (ht == 0) {
//            for (int j = 0; j < h.len; j++) {
//                s.append(indent + children[j].key + " " + children[j].val + "\n");
//            }
//        }
//        else {
//            for (int j = 0; j < h.len; j++) {
//                if (j > 0) s.append(indent + "(" + children[j].key + ")\n");
//                s.append(toString(children[j].next, ht-1, indent + "     "));
//            }
//        }
//        return s.toString();
//    }

    public static void main(String[] args) {
        BPlusTree<Integer, String> st = new BPlusTree<Integer, String>();
//        int[] test = {1,3,8,20,60,4,2,9,8,5,22,65,80,54,63};
        int[] test = {100,8,20,60,4,2,9,8,5,22,65,80,54,63,1};
        long start = System.currentTimeMillis();
        for (int i = 0; i < test.length; i++) {
//            Random random = new Random(i);
//            st.put(Math.abs(random.nextInt()), i + "");
//            st.put(i, i + "");
            st.put(test[i], test[i] + "");
        }
        long end = System.currentTimeMillis();
//        System.out.println(st);
        System.out.println("args = [" + (end-start) + "]");
        start = System.currentTimeMillis();
        String s = st.search(st.root,4,st.height);
        end = System.currentTimeMillis();
        System.out.println((end-start)+"--"+s+"-"+st.getHeight() + "]");

        List<String> list = st.find(st.root,8,60,st.height);
        System.out.println("args = [" + list + "]");
    }

    public List<Value> find(Node node, Key minKey, Key maxKey, int ht) {
        //叶子节点
        if (ht == 0) {
            int loc = BinarySearch.binarySearch(node.key, 0, node.len, minKey,false);
            Node tempNode = node;
            List<Value> valueList = new ArrayList<>();
            //如果找到的是第一个
            if(loc==0){
                tempNode = tempNode.prev;
                if (tempNode.key[tempNode.len - 1].compareTo(minKey) != 0) {
                    tempNode = tempNode.next;
                } else {
                    loc = BinarySearch.binarySearch(tempNode.key, 0, tempNode.len, minKey,false);
                }
            }

            loc=loc<0 ? -1-loc : loc;
            if(tempNode.key[tempNode.len-1].compareTo(maxKey)>0){
                int maxLoc = BinarySearch.binarySearch(tempNode.key, 0, tempNode.len, maxKey,true);
                maxLoc=maxLoc<0?-1-maxLoc:maxLoc;
                valueList.addAll(Arrays.asList((Value[]) Arrays.copyOfRange(tempNode.val,loc,maxLoc+1)));
                return valueList;
            }else {
                valueList.addAll(Arrays.asList((Value[]) Arrays.copyOfRange(tempNode.val,loc,tempNode.len)));
                tempNode=tempNode.next;
            }
            while (tempNode!=null && tempNode.key[tempNode.len-1].compareTo(maxKey)<=0){
                valueList.addAll(Arrays.asList((Value[])Arrays.copyOf(tempNode.val,tempNode.len)));
                tempNode=tempNode.next;
            }
            if(tempNode!=null){
                int maxLoc = BinarySearch.binarySearch(tempNode.key, 0, tempNode.len, maxKey,true);
                if(maxLoc==0){
                    valueList.add((Value) tempNode.val[0]);
                    return valueList;
                }
                maxLoc=maxLoc<0?-1-maxLoc:maxLoc;
                if(maxLoc==0){
                    return valueList;
                }
                valueList.addAll(Arrays.asList((Value[]) Arrays.copyOfRange(tempNode.val,0,maxLoc+1)));
                return valueList;
            }
            return valueList;
        }
        //非叶子节点
        else {
            int loc = Arrays.binarySearch(node.key, 1, node.len, minKey);
            int index = loc>=0 ? loc : -1-loc-1;
            if(index <= node.len && index>=0){
                return find(node.children[index++],minKey,maxKey,ht-1);
            }
        }
        return null;
    }
}
