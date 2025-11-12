package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import java.util.Stack;

public class ParallelQuickSort {

    private static final int THRESHOLD = 10000;

    

    public static void sort(int[] array) {

        ForkJoinPool pool = ForkJoinPool.commonPool();

        pool.invoke(new QuickSortTask(array, 0, array.length - 1));

    }

    

    private static class QuickSortTask extends RecursiveAction {

        private int[] array;

        private int low, high;

        

        QuickSortTask(int[] array, int low, int high) {

            this.array = array;

            this.low = low;

            this.high = high;

        }

        

        @Override

        protected void compute() {

            if (high - low < THRESHOLD) {

                sequentialQuickSort(array, low, high);

                return;

            }

            

            if (low < high) {

                int pi = partition(array, low, high);

                QuickSortTask leftTask = new QuickSortTask(array, low, pi - 1);

                QuickSortTask rightTask = new QuickSortTask(array, pi + 1, high);

                

                invokeAll(leftTask, rightTask);

            }

        }

        

        private void sequentialQuickSort(int[] array, int low, int high) {

            // Usar stack en lugar de recursión para evitar StackOverflowError

            Stack<int[]> stack = new Stack<>();

            stack.push(new int[]{low, high});

            

            while (!stack.isEmpty()) {

                int[] bounds = stack.pop();

                int l = bounds[0];

                int h = bounds[1];

                

                if (l < h) {

                    int pi = partition(array, l, h);

                    

                    // Optimización: procesar la parte más pequeña primero

                    if (pi - l < h - pi) {

                        stack.push(new int[]{pi + 1, h});

                        stack.push(new int[]{l, pi - 1});

                    } else {

                        stack.push(new int[]{l, pi - 1});

                        stack.push(new int[]{pi + 1, h});

                    }

                }

            }

        }

        

        private int partition(int[] array, int low, int high) {

            // Mediana de tres para mejor pivote

            int mid = low + (high - low) / 2;

            if (array[mid] < array[low]) {

                swap(array, low, mid);

            }

            if (array[high] < array[low]) {

                swap(array, low, high);

            }

            if (array[high] < array[mid]) {

                swap(array, mid, high);

            }

            

            // Usar el medio como pivote

            int pivot = array[mid];

            swap(array, mid, high);

            

            int i = low - 1;

            

            for (int j = low; j < high; j++) {

                if (array[j] <= pivot) {

                    i++;

                    swap(array, i, j);

                }

            }

            

            swap(array, i + 1, high);

            return i + 1;

        }

        

        private void swap(int[] array, int i, int j) {

            int temp = array[i];

            array[i] = array[j];

            array[j] = temp;

        }

    }

}

