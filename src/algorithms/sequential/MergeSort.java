package algorithms.sequential;

public class MergeSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;
        ordenar(arr, 0, arr.length - 1);
    }

    private static void ordenar(int[] arr, int izq, int der) {
        if (izq < der) {
            int medio = (izq + der) / 2;
            ordenar(arr, izq, medio);
            ordenar(arr, medio + 1, der);
            mezclar(arr, izq, medio, der);
        }
    }

    private static void mezclar(int[] arr, int izq, int medio, int der) {
        int n1 = medio - izq + 1;
        int n2 = der - medio;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, izq, L, 0, n1);
        System.arraycopy(arr, medio + 1, R, 0, n2);

        int i = 0, j = 0, k = izq;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }
}
