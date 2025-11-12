package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import java.util.Stack;

public class ParallelQuickSort {
    private static final int UMBRAL = 10000;

    public static void sort(int[] arr) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new TareaQuickSort(arr, 0, arr.length - 1));
    }

    private static class TareaQuickSort extends RecursiveAction {
        private int[] arr;
        private int izq, der;

        TareaQuickSort(int[] arr, int izq, int der) {
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
                int pivote = particionar(arr, izq, der);
                TareaQuickSort tareaIzq = new TareaQuickSort(arr, izq, pivote - 1);
                TareaQuickSort tareaDer = new TareaQuickSort(arr, pivote + 1, der);
                invokeAll(tareaIzq, tareaDer);
            }
        }

        private void ordenarSecuencial(int[] arr, int izq, int der) {
            Stack<int[]> pila = new Stack<>();
            pila.push(new int[]{izq, der});

            while (!pila.isEmpty()) {
                int[] limites = pila.pop();
                int i = limites[0];
                int d = limites[1];

                if (i < d) {
                    int pivote = particionar(arr, i, d);

                    if (pivote - i < d - pivote) {
                        pila.push(new int[]{pivote + 1, d});
                        pila.push(new int[]{i, pivote - 1});
                    } else {
                        pila.push(new int[]{i, pivote - 1});
                        pila.push(new int[]{pivote + 1, d});
                    }
                }
            }
        }

        private int particionar(int[] arr, int izq, int der) {
            int medio = izq + (der - izq) / 2;
            if (arr[medio] < arr[izq]) {
                intercambiar(arr, izq, medio);
            }
            if (arr[der] < arr[izq]) {
                intercambiar(arr, izq, der);
            }
            if (arr[der] < arr[medio]) {
                intercambiar(arr, medio, der);
            }

            int pivote = arr[medio];
            intercambiar(arr, medio, der);

            int i = izq - 1;

            for (int j = izq; j < der; j++) {
                if (arr[j] <= pivote) {
                    i++;
                    intercambiar(arr, i, j);
                }
            }

            intercambiar(arr, i + 1, der);
            return i + 1;
        }

        private void intercambiar(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
