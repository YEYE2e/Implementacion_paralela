package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSort {
    private static final int THRESHOLD = 10000;

    public static void sort(int[] array) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeSortTask(array, 0, array.length - 1));
    }

    private static class MergeSortTask extends RecursiveAction {
        private int[] array;
        private int left, right;

        MergeSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < THRESHOLD) {
                // Usar algoritmo secuencial para subarrays pequeños
                sequentialMergeSort(array, left, right);
                return;
            }

            if (left < right) {
                int mid = (left + right) / 2;
                MergeSortTask leftTask = new MergeSortTask(array, left, mid);
                MergeSortTask rightTask = new MergeSortTask(array, mid + 1, right);

                invokeAll(leftTask, rightTask);
                merge(array, left, mid, right);
            }
        }

        private void sequentialMergeSort(int[] array, int left, int right) {
            if (left < right) {
                int mid = (left + right) / 2;
                sequentialMergeSort(array, left, mid);
                sequentialMergeSort(array, mid + 1, right);
                merge(array, left, mid, right);
            }
        }

        private void merge(int[] array, int left, int mid, int right) {
            int n1 = mid - left + 1;
            int n2 = right - mid;

            int[] L = new int[n1];
            int[] R = new int[n2];

            System.arraycopy(array, left, L, 0, n1);
            System.arraycopy(array, mid + 1, R, 0, n2);

            int i = 0, j = 0, k = left;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    array[k++] = L[i++];
                } else {
                    array[k++] = R[j++];
                }
            }

            while (i < n1) array[k++] = L[i++];
            while (j < n2) array[k++] = R[j++];
        }
    }
}