package com.huazi.tree.binary.btreetest;

public class BinarySearch {
    public static int binarySearch(Object[] a, int fromIndex, int toIndex,
                                    Object key) {
        return binarySearch(a, fromIndex, toIndex, key,null);
    }
    /**
     *
     * @param a
     * @param fromIndex
     * @param toIndex 最后一个元素 不包括
     * @param key
     * @param up 重复数的查找 true表示向后查找最后 false表示向前查找
     * @return
     */
    public static int binarySearch(Object[] a, int fromIndex, int toIndex,
                                     Object key,Boolean up) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            @SuppressWarnings("rawtypes")
            Comparable midVal = (Comparable)a[mid];
            @SuppressWarnings("unchecked")
            int cmp = midVal.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                if(up==null){
                    return mid;
                }else {
                    while (mid >= 0 && mid < a.length) {
                        mid = up ? ++mid : --mid;
                        if(mid<0 || mid>=a.length){
                            break;
                        }
                        midVal = (Comparable) a[mid];
                        if (midVal.compareTo(key) != 0) {
                            break;
                        }
                    }
                    // key found
                    return up ? --mid : ++mid;
                }
            }
        }
        // key not found.
        return -(low + 1);
    }
    public static void main(String[] args) {
        Integer[] a = {245542,245542,245543,245544,245544};
        int index = binarySearch(a,0,a.length,245544,true);
        System.out.println("args = [" + index + "]");
    }
}
