package com.huazi.tree.binary;

public class BinaryTreeUtil {
    public static <T extends Comparable<T>> BinaryTree<T> insertLeft(BinaryTree<T> parent,T data){
        BinaryTree<T> left =  new BinaryTree(data);
        left.setLevel(parent.getLevel()+1);
        left.setParent(parent);
        parent.setLeft(left);
        return left;
    }

    public static <T extends Comparable<T>> BinaryTree<T> insertRight(BinaryTree<T> parent,T data){
        BinaryTree<T> right =  new BinaryTree(data);
        right.setLevel(parent.getLevel()+1);
        right.setParent(parent);
        parent.setRight(right);
        return right;
    }

    public static <T extends Comparable<T>> BinaryTree<T> insert(BinaryTree<T> root,T data){
        if(root==null){
            return null;
        }
        BinaryTree<T> newTree ;
        //如果小于 查找左子树
        if(root.getData().compareTo(data)>0){
            if(root.getLeft()==null){
                return insertLeft(root,data);
            }
            newTree = insert(root.getLeft(),data);
        }else {
            if(root.getRight()==null){
                return insertRight(root,data);
            }
            newTree = insert(root.getRight(),data);
        }
        return newTree;
    }

    public static <T extends Comparable<T>> BinaryTree<T> find(BinaryTree<T> root,T value) {
        if(root==null || root.getData().equals(value)){
            return root;
        }
        BinaryTree<T> temp;
        if(root.getData().compareTo(value)<0){
            temp = root.getRight();

        }else {
            temp = root.getLeft();
        }
        while (temp!=null){
            if(temp.getData().equals(value)){
                return temp;
            }
            if(temp.getData().compareTo(value)<0){
                temp = temp.getRight();
                continue;
            }
            if(temp.getData().compareTo(value)>0){
                temp = temp.getLeft();
            }
        }
        return null;

    }
    //删除节点
    public static <T extends Comparable<T>> void delete(BinaryTree<T> root,T value) {

        BinaryTree<T> delNode = find(root, value);	//p是要删除的结点
        if(delNode==null){
            return;
        }

        BinaryTree<T> parent = delNode.getParent();
        //左右节点都不为空
        if(delNode.getLeft()!=null && delNode.getRight()!=null) {
            BinaryTree<T> replaceNode;
            //找到左子树的最右节点 或者 找到右子树的最左节点
            BinaryTree<T> left = delNode.getLeft();
            while (left.getRight()!=null){
                left=left.getRight();
            }
            replaceNode=left;
            if(replaceNode.getLeft()!=null){
                replaceNode.getParent().setRight(replaceNode.getLeft());
            }else {
                replaceNode.getParent().setLeft(null);
            }
            BinaryTree<T> tempLeft = delNode.getLeft();
            BinaryTree<T> tempRight = delNode.getRight();
            if(delNode.getData().compareTo(parent.getData())>0){
                parent.setRight(replaceNode);
            }else {
                parent.setLeft(replaceNode);
            }
            replaceNode.setParent(parent);
            replaceNode.setLeft(tempLeft);
            replaceNode.setRight(tempRight);
        }else{
            if(delNode.getData().compareTo(parent.getData())>0){
                parent.setRight(delNode.getLeft());
            }else {
                parent.setLeft(delNode.getLeft());
            }
        }

    }
    public static <T extends Comparable<T>> void display(BinaryTree<T> root,int n){

            int i;
            if(root==null){

                return ;//递归出口
            }
            display(root.getRight(),n+1);//遍历打印右子树
            //訪问根节点
            for(i=0;i<n-1;i++){
                System.out.print("  ");
            }

            if(n>0){

                System.out.print("---");
                System.out.println(root.getData());
            }

        display(root.getLeft(),n+1);//遍历打印右子树

        }
}
