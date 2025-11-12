package algorithms.sequential;

public class BitonicSort {
    public static void sort(int[] array) {
        int n = array.length;
        // Asegurar que n es potencia de 2
        int size = 1;
        while (size < n) size *= 2;

        int[] paddedArray = new int[size];
        System.arraycopy(array, 0, paddedArray, 0, n);
        for (int i = n; i < size; i++) {
            paddedArray[i] = Integer.MAX_VALUE;
        }

        bitonicSort(paddedArray, 0, size, true);
        System.arraycopy(paddedArray, 0, array, 0, n);
    }

    private static void bitonicSort(int[] array, int low, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            bitonicSort(array, low, k, true);
            bitonicSort(array, low + k, k, false);
            bitonicMerge(array, low, cnt, dir);
        }
    }

    private static void bitonicMerge(int[] array, int low, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            for (int i = low; i < low + k; i++) {
                compareAndSwap(array, i, i + k, dir);
            }
            bitonicMerge(array, low, k, dir);
            bitonicMerge(array, low + k, k, dir);
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