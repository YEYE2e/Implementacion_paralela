package algorithms.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class ParallelSampleSort {
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
        Arrays.parallelSort(muestras);
        
        int[] pivotes = new int[numCubetas - 1];
        for (int i = 0; i < pivotes.length; i++) {
            pivotes[i] = muestras[(i + 1) * muestras.length / numCubetas];
        }
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        @SuppressWarnings("unchecked")
        List<Integer>[] cubetas = pool.submit(() ->
            IntStream.range(0, numCubetas)
                .parallel()
                .mapToObj(i -> new ArrayList<Integer>())
                .toArray(ArrayList[]::new)
        ).join();
        
        for (int valor : arr) {
            int cubeta = buscarCubeta(valor, pivotes);
            synchronized (cubetas[cubeta]) {
                cubetas[cubeta].add(valor);
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
    
    private static int buscarCubeta(int valor, int[] pivotes) {
        for (int i = 0; i < pivotes.length; i++) {
            if (valor < pivotes[i]) return i;
        }
        return pivotes.length;
    }
}
