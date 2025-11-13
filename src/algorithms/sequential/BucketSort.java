package algorithms.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;

        int max = Arrays.stream(arr).max().orElse(0);
        int min = Arrays.stream(arr).min().orElse(0);
        int rango = max - min + 1;
        if (rango == 1) return;
        
        int numCubetas = (int) Math.sqrt(arr.length);

        @SuppressWarnings("unchecked")
        List<Integer>[] cubetas = new ArrayList[numCubetas];
        for (int i = 0; i < numCubetas; i++) {
            cubetas[i] = new ArrayList<>();
        }

        for (int valor : arr) {
            int idx = (int) ((long) (valor - min) * numCubetas / rango);
            if (idx >= numCubetas) idx = numCubetas - 1;
            cubetas[idx].add(valor);
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
}
