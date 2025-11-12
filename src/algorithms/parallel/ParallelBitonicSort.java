package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelBitonicSort {

    private static final int THRESHOLD = 1000;
    
    public static void sort(int[] array) {
        int n = array.length;
        int size = 1;
        while (size < n) size *= 2;
        
        int[] paddedArray = new int[size];
        System.arraycopy(array, 0, paddedArray, 0, n);
        for (int i = n; i < size; i++) {
            paddedArray[i] = Integer.MAX_VALUE;
        }
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new BitonicSortTask(paddedArray, 0, size, true));
        
        System.arraycopy(paddedArray, 0, array, 0, n);
    }
    
    private static class BitonicSortTask extends RecursiveAction {
        private int[] array;
        private int low, cnt;
        private boolean dir;
        
        BitonicSortTask(int[] array, int low, int cnt, boolean dir) {
            this.array = array;
            this.low = low;
            this.cnt = cnt;
            this.dir = dir;
        }
        
        @Override
        protected void compute() {
            if (cnt <= THRESHOLD) {
                sequentialBitonicSort(array, low, cnt, dir);
                return;
            }
            
            if (cnt > 1) {
                int k = cnt / 2;
                BitonicSortTask task1 = new BitonicSortTask(array, low, k, true);
                BitonicSortTask task2 = new BitonicSortTask(array, low + k, k, false);
                
                invokeAll(task1, task2);
                
                new BitonicMergeTask(array, low, cnt, dir).invoke();
            }
        }
        
        private void sequentialBitonicSort(int[] array, int low, int cnt, boolean dir) {
            if (cnt > 1) {
                int k = cnt / 2;
                sequentialBitonicSort(array, low, k, true);
                sequentialBitonicSort(array, low + k, k, false);
                sequentialBitonicMerge(array, low, cnt, dir);
            }
        }
        
        private void sequentialBitonicMerge(int[] array, int low, int cnt, boolean dir) {
            if (cnt > 1) {
                int k = cnt / 2;
                for (int i = low; i < low + k; i++) {
                    compareAndSwap(array, i, i + k, dir);
                }
                sequentialBitonicMerge(array, low, k, dir);
                sequentialBitonicMerge(array, low + k, k, dir);
            }
        }
    }
    
    private static class BitonicMergeTask extends RecursiveAction {
        private int[] array;
        private int low, cnt;
        private boolean dir;
        
        BitonicMergeTask(int[] array, int low, int cnt, boolean dir) {
            this.array = array;
            this.low = low;
            this.cnt = cnt;
            this.dir = dir;
        }
        
        @Override
        protected void compute() {
            if (cnt > 1) {
                int k = cnt / 2;
                for (int i = low; i < low + k; i++) {
                    compareAndSwap(array, i, i + k, dir);
                }
                
                BitonicMergeTask task1 = new BitonicMergeTask(array, low, k, dir);
                BitonicMergeTask task2 = new BitonicMergeTask(array, low + k, k, dir);
                
                invokeAll(task1, task2);
            }
        }
    }
    
    private static void compareAndSwap(int[] array, int i, int j, boolean dir) {
        if (dir == (array[i] > array[j])) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}

