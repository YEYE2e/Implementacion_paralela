package algorithms.sequential;

public class BitonicSort {
    public static void sort(int[] arr) {
        int n = arr.length;
        int tam = 1;
        while (tam < n) tam *= 2;

        int[] arrRelleno = new int[tam];
        System.arraycopy(arr, 0, arrRelleno, 0, n);
        for (int i = n; i < tam; i++) {
            arrRelleno[i] = Integer.MAX_VALUE;
        }

        ordenarBitonic(arrRelleno, 0, tam, true);
        System.arraycopy(arrRelleno, 0, arr, 0, n);
    }

    private static void ordenarBitonic(int[] arr, int inicio, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            ordenarBitonic(arr, inicio, k, true);
            ordenarBitonic(arr, inicio + k, k, false);
            mezclarBitonic(arr, inicio, cnt, dir);
        }
    }

    private static void mezclarBitonic(int[] arr, int inicio, int cnt, boolean dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            for (int i = inicio; i < inicio + k; i++) {
                compararIntercambiar(arr, i, i + k, dir);
            }
            mezclarBitonic(arr, inicio, k, dir);
            mezclarBitonic(arr, inicio + k, k, dir);
        }
    }

    private static void compararIntercambiar(int[] arr, int i, int j, boolean dir) {
        if (dir == (arr[i] > arr[j])) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
