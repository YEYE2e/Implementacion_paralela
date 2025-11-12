package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelMergeSort {
    private static final int UMBRAL = 10000;

    public static void sort(int[] arr) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new TareaMergeSort(arr, 0, arr.length - 1));
    }

    private static class TareaMergeSort extends RecursiveAction {
        private int[] arr;
        private int izq, der;

        TareaMergeSort(int[] arr, int izq, int der) {
            this.arr = arr;
            this.izq = izq;
            this.der = der;
        }

        @Override
        protected void compute() {
            if (der - izq < UMBRAL) {
                ordenarSecuencial(arr, izq, der);
                return;
            }

            if (izq < der) {
                int medio = (izq + der) / 2;
                TareaMergeSort tareaIzq = new TareaMergeSort(arr, izq, medio);
                TareaMergeSort tareaDer = new TareaMergeSort(arr, medio + 1, der);
                invokeAll(tareaIzq, tareaDer);
                mezclar(arr, izq, medio, der);
            }
        }

        private void ordenarSecuencial(int[] arr, int izq, int der) {
            if (izq < der) {
                int medio = (izq + der) / 2;
                ordenarSecuencial(arr, izq, medio);
                ordenarSecuencial(arr, medio + 1, der);
                mezclar(arr, izq, medio, der);
            }
        }

        private void mezclar(int[] arr, int izq, int medio, int der) {
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
}
