package algorithms.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelBucketSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;
        
        int max = Arrays.stream(arr).parallel().max().orElse(0);
        int min = Arrays.stream(arr).parallel().min().orElse(0);
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
            synchronized (cubetas[idx]) {
                cubetas[idx].add(valor);
            }
        }
        
        int[] indices = new int[numCubetas + 1];
        for (int i = 0; i < numCubetas; i++) {
            indices[i + 1] = indices[i] + cubetas[i].size();
        }
        
        IntStream.range(0, numCubetas).parallel().forEach(i -> {
            int[] arrCubeta = cubetas[i].stream().mapToInt(Integer::intValue).toArray();
            Arrays.parallelSort(arrCubeta);
            System.arraycopy(arrCubeta, 0, arr, indices[i], arrCubeta.length);
        });
    }
}
