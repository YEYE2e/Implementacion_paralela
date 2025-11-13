package algorithms.parallel;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ParallelRadixSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;
        
        int min = Arrays.stream(arr).parallel().min().orElse(0);
        int max = Arrays.stream(arr).parallel().max().orElse(0);
        
        final int offset = (min < 0) ? -min : 0;
        if (offset > 0) {
            final int finalMax = max + offset;
            IntStream.range(0, arr.length).parallel().forEach(i -> arr[i] += offset);
            max = finalMax;
        }
        
        for (int exp = 1; max / exp > 0; exp *= 10) {
            contarParalelo(arr, exp);
        }
        
        if (offset > 0) {
            IntStream.range(0, arr.length).parallel().forEach(i -> arr[i] -= offset);
        }
    }
    
    private static void contarParalelo(int[] arr, int exp) {
        int n = arr.length;
        int[] salida = new int[n];
        int[] contador = new int[10];
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        int[][] conteosLocales = pool.submit(() ->
            IntStream.range(0, pool.getParallelism())
                .parallel()
                .mapToObj(i -> {
                    int[] contLocal = new int[10];
                    int inicio = i * n / pool.getParallelism();
                    int fin = (i + 1) * n / pool.getParallelism();
                    for (int j = inicio; j < fin; j++) {
                        contLocal[(arr[j] / exp) % 10]++;
                    }
                    return contLocal;
                })
                .toArray(int[][]::new)
        ).join();
        
        for (int[] contLocal : conteosLocales) {
            for (int i = 0; i < 10; i++) {
                contador[i] += contLocal[i];
            }
        }
        
        for (int i = 1; i < 10; i++) {
            contador[i] += contador[i - 1];
        }
        
        for (int i = n - 1; i >= 0; i--) {
            int digito = (arr[i] / exp) % 10;
            salida[contador[digito] - 1] = arr[i];
            contador[digito]--;
        }
        
        System.arraycopy(salida, 0, arr, 0, n);
    }
}
