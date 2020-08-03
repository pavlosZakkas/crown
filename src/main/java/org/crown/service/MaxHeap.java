package org.crown.service;

import java.util.Comparator;
import java.util.List;

public class MaxHeap<T> {
    private T[] heap;
    private int size;
    private int maxsize;
    private Comparator<T> comparator;

    public MaxHeap(List<T> nodes, Comparator<T> comparator) {
        this.maxsize = nodes.size();
        this.size = 0;
        heap = (T[])new Object[maxsize + 1];
        this.comparator = comparator;
        nodes.forEach(this::insert);
    }

    private int parent(int pos) {
        return pos / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    private boolean isLeaf(int pos) {
        if (pos >= (size / 2.0) && pos <= size) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos) {
        T tmp;
        tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }

    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;

        int left = leftChild(pos);
        int right = rightChild(pos);
        if (left <= size && comparator.compare(heap[pos], heap[left]) < 0 ||
            right<= size && comparator.compare(heap[pos], heap[right]) < 0) {

            if (left > size) {
                swap(pos, right);
                maxHeapify(right);
            }
            else if (right > size) {
                swap(pos, left);
                maxHeapify(left);
            }
            else if (comparator.compare(heap[left], heap[right]) > 0) {
                swap(pos, left);
                maxHeapify(left);
            } else {
                swap(pos, right);
                maxHeapify(right);
            }
        }
    }

    public void insert(T element) {
        heap[++size] = element;

        int current = size;
        while (heap[parent(current)] != null && comparator.compare(heap[current], heap[parent(current)]) > 0) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public void print() {
        for (int i = 1; i <= size / 2; i++) {
            System.out.print(" PARENT : " + heap[i] + " LEFT CHILD : " +
                heap[2 * i] + " RIGHT CHILD :" + heap[2 * i + 1]);
            System.out.println();
        }
    }

    public T extractMax() {
        T popped = heap[1];
        heap[1] = heap[size--];
        maxHeapify(1);
        return popped;
    }

    public Boolean isNotEmpty() {
        return size > 0;
    }
}
