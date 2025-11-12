package algorithms.sequential;

import java.util.Stack;

public class QuickSort {
    public static void sort(int[] array) {
        if (array.length < 2) return;
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int low, int high) {
        // Usar stack en lugar de recursión para evitar StackOverflowError
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{low, high});

        while (!stack.isEmpty()) {
            int[] bounds = stack.pop();
            int l = bounds[0];
            int h = bounds[1];

            if (l < h) {
                // Usar mediana de tres para elegir mejor pivote
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

    private static int partition(int[] array, int low, int high) {
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

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
