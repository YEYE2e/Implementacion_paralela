package algorithms.parallel;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ParallelRadixSort {

    public static void sort(int[] array) {
        if (array.length < 2) return;
        
        int max = Arrays.stream(array).parallel().max().orElse(0);
        
        for (int exp = 1; max / exp > 0; exp *= 10) {
            parallelCountingSort(array, exp);
        }
    }
    
    private static void parallelCountingSort(int[] array, int exp) {
        int n = array.length;
        int[] output = new int[n];
        int[] count = new int[10];
        
        // Contar en paralelo
        ForkJoinPool pool = ForkJoinPool.commonPool();
        int[][] localCounts = pool.submit(() ->
            IntStream.range(0, pool.getParallelism())
                .parallel()
                .mapToObj(i -> {
                    int[] localCount = new int[10];
                    int start = i * n / pool.getParallelism();
                    int end = (i + 1) * n / pool.getParallelism();
                    for (int j = start; j < end; j++) {
                        localCount[(array[j] / exp) % 10]++;
                    }
                    return localCount;
                })
                .toArray(int[][]::new)
        ).join();
        
        // Combinar conteos
        for (int[] localCount : localCounts) {
            for (int i = 0; i < 10; i++) {
                count[i] += localCount[i];
            }
        }
        
        // Acumular
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        // Construir output
        for (int i = n - 1; i >= 0; i--) {
            int digit = (array[i] / exp) % 10;
            output[count[digit] - 1] = array[i];
            count[digit]--;
        }
        
        System.arraycopy(output, 0, array, 0, n);
    }
}

