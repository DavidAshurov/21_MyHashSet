package telran.set.model;

import telran.set.interfaces.ISet;

import java.util.Iterator;
import java.util.LinkedList;

public class MyHashSet<E> implements ISet<E> {
    private LinkedList<E>[] hashset;
    private int size;
    private int capacity;
    private double loadFactor;

    public MyHashSet(double loadFactor, int capacity) {
        this.loadFactor = loadFactor;
        this.capacity = capacity;
        hashset = new LinkedList[capacity];
    }

    public MyHashSet(int capacity) {
        this(0.75,capacity);
    }

    public MyHashSet() {
        this(16);
    }

    @Override
    public boolean add(E element) {
        if (size >= loadFactor * capacity) {
            rebuildArray();
        }
        int index = getIndex(element);
        if (hashset[index] == null) {
            hashset[index] = new LinkedList<>();
        }
        if (hashset[index].contains(element)) {
            return false;
        }
        hashset[index].add(element);
        size++;
        return true;
    }

    private void rebuildArray() {
        capacity *= 2;
        LinkedList<E>[] newHashSet = new LinkedList[capacity];
        for (int i = 0; i < hashset.length; i++) {
            if (hashset[i] != null) {
                for (E elem : hashset[i]) {
                    int index = getIndex(elem);
                    if (newHashSet[index] == null) {
                        newHashSet[index] = new LinkedList<>();
                    }
                    newHashSet[index].add(elem);
                }
            }
        }
        hashset = newHashSet;
    }

    private int getIndex(E element) {
        int hashCode = element.hashCode();
        hashCode = hashCode >= 0 ? hashCode : -hashCode;
        return hashCode % capacity;
    }

    @Override
    public boolean contains(E element) {
        int index = getIndex(element);
        if (hashset[index] == null) {
            return false;
        }
        return hashset[index].contains(element);
    }

    @Override
    public boolean remove(E element) {
       int index = getIndex(element);
       if (hashset[index] == null) {
           return false;
       }
       if (hashset[index].remove(element)) {
           size--;
           return true;
       }
       return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int i;
            int bucketIndex;
            int indexInBucket;
            LinkedList<E> currBucket;
            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public E next() {
                if (currBucket == null) {
                    while (hashset[bucketIndex] == null || hashset[bucketIndex].isEmpty()) {
                        bucketIndex++;
                    }
                    currBucket = new LinkedList<>(hashset[bucketIndex]);
                }
                E res = currBucket.get(indexInBucket);
                indexInBucket++;
                if (indexInBucket >= currBucket.size()) {
                    currBucket = null;
                    indexInBucket = 0;
                    bucketIndex++;
                }
                i++;
                return res;
            }
        };
    }
}
