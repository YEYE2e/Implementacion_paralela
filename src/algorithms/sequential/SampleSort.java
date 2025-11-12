package algorithms.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleSort {
    private static final int TAM_MUESTRA = 100;

    public static void sort(int[] arr) {
        if (arr.length < 2) return;

        int n = arr.length;
        int numCubetas = Math.min(n / 100 + 1, TAM_MUESTRA);

        int[] muestras = new int[Math.min(TAM_MUESTRA, n)];
        int paso = Math.max(1, n / muestras.length);
        for (int i = 0; i < muestras.length; i++) {
            muestras[i] = arr[i * paso];
        }
        Arrays.sort(muestras);

        int[] pivotes = new int[numCubetas - 1];
        for (int i = 0; i < pivotes.length; i++) {
            pivotes[i] = muestras[(i + 1) * muestras.length / numCubetas];
        }

        @SuppressWarnings("unchecked")
        List<Integer>[] cubetas = new ArrayList[numCubetas];
        for (int i = 0; i < numCubetas; i++) {
            cubetas[i] = new ArrayList<>();
        }

        for (int valor : arr) {
            int cubeta = buscarCubeta(valor, pivotes);
            cubetas[cubeta].add(valor);
        }

        int idx = 0;
        for (List<Integer> cubeta : cubetas) {
            int[] arrCubeta = cubeta.stream().mapToInt(i -> i).toArray();
            Arrays.sort(arrCubeta);
            for (int valor : arrCubeta) {
                arr[idx++] = valor;
            }
        }
    }

    private static int buscarCubeta(int valor, int[] pivotes) {
        for (int i = 0; i < pivotes.length; i++) {
            if (valor < pivotes[i]) return i;
        }
        return pivotes.length;
    }
}
