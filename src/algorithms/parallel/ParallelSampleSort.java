package algorithms.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelSampleSort {

    private static final int SAMPLE_SIZE = 100;
    
    public static void sort(int[] array) {
        if (array.length < 2) return;
        
        int n = array.length;
        int numBuckets = Math.min(n / 100 + 1, SAMPLE_SIZE);
        
        // Seleccionar muestras
        int[] samples = new int[Math.min(SAMPLE_SIZE, n)];
        int sampleStep = Math.max(1, n / samples.length);
        for (int i = 0; i < samples.length; i++) {
            samples[i] = array[i * sampleStep];
        }
        Arrays.parallelSort(samples);
        
        // Crear pivotes
        int[] pivots = new int[numBuckets - 1];
        for (int i = 0; i < pivots.length; i++) {
            pivots[i] = samples[(i + 1) * samples.length / numBuckets];
        }
        
        // Distribuir en buckets en paralelo
        ForkJoinPool pool = ForkJoinPool.commonPool();
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = pool.submit(() ->
            IntStream.range(0, numBuckets)
                .parallel()
                .mapToObj(i -> new ArrayList<Integer>())
                .toArray(ArrayList[]::new)
        ).join();
        
        // Distribuir elementos (esta parte debe ser sincronizada)
        for (int value : array) {
            int bucket = findBucket(value, pivots);
            synchronized (buckets[bucket]) {
                buckets[bucket].add(value);
            }
        }
        
        // Ordenar buckets en paralelo y combinar
        int[] indices = new int[numBuckets + 1];
        for (int i = 0; i < numBuckets; i++) {
            indices[i + 1] = indices[i] + buckets[i].size();
        }
        
        IntStream.range(0, numBuckets).parallel().forEach(i -> {
            int[] bucketArray = buckets[i].stream().mapToInt(Integer::intValue).toArray();
            Arrays.parallelSort(bucketArray);
            System.arraycopy(bucketArray, 0, array, indices[i], bucketArray.length);
        });
    }
    
    private static int findBucket(int value, int[] pivots) {
        for (int i = 0; i < pivots.length; i++) {
            if (value < pivots[i]) return i;
        }
        return pivots.length;
    }
}

