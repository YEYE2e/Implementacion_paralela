package algorithms.sequential;

import java.util.Stack;

public class QuickSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;
        ordenar(arr, 0, arr.length - 1);
    }

    private static void ordenar(int[] arr, int izq, int der) {
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

    private static int particionar(int[] arr, int izq, int der) {
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

    private static void intercambiar(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
