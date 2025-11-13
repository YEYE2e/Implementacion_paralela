package algorithms.parallel;

import java.util.Stack;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort {
    private static final int UMBRAL = 10000;

    public static void sort(int[] arr) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new TareaQuickSort(arr, 0, arr.length - 1));
    }

    private static class TareaQuickSort extends RecursiveAction {
        private final int[] arr;
        private final int izq;
        private final int der;

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
                int[] limitesPart = particionar3Vias(arr, izq, der);
                int lt = limitesPart[0];
                int gt = limitesPart[1];
                
                if (izq < lt - 1) {
                    TareaQuickSort tareaIzq = new TareaQuickSort(arr, izq, lt - 1);
                    if (gt + 1 < der) {
                        TareaQuickSort tareaDer = new TareaQuickSort(arr, gt + 1, der);
                        invokeAll(tareaIzq, tareaDer);
                    } else {
                        tareaIzq.invoke();
                    }
                } else if (gt + 1 < der) {
                    TareaQuickSort tareaDer = new TareaQuickSort(arr, gt + 1, der);
                    tareaDer.invoke();
                }
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
                    int[] limitesPart = particionar3Vias(arr, i, d);
                    int lt = limitesPart[0];
                    int gt = limitesPart[1];
                    
                    if (lt - i < d - gt) {
                        if (i < lt - 1) {
                            pila.push(new int[]{gt + 1, d});
                            pila.push(new int[]{i, lt - 1});
                        } else if (gt + 1 < d) {
                            pila.push(new int[]{gt + 1, d});
                        }
                    } else {
                        if (gt + 1 < d) {
                            pila.push(new int[]{i, lt - 1});
                            pila.push(new int[]{gt + 1, d});
                        } else if (i < lt - 1) {
                            pila.push(new int[]{i, lt - 1});
                        }
                    }
                }
            }
        }

        private int[] particionar3Vias(int[] arr, int izq, int der) {
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
            intercambiar(arr, medio, izq);
            
            int i = izq;
            int j = izq + 1;
            int k = der + 1;
            
            while (j < k) {
                if (arr[j] < pivote) {
                    intercambiar(arr, i, j);
                    i++;
                    j++;
                } else if (arr[j] > pivote) {
                    k--;
                    intercambiar(arr, j, k);
                } else {
                    j++;
                }
            }
            
            return new int[]{i, k - 1};
        }

        private void intercambiar(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
