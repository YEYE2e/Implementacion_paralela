package algorithms.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelBucketSort {

    public static void sort(int[] array) {
        if (array.length < 2) return;
        
        int max = Arrays.stream(array).parallel().max().orElse(0);
        int min = Arrays.stream(array).parallel().min().orElse(0);
        int range = max - min + 1;
        int numBuckets = (int) Math.sqrt(array.length);
        
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Distribuir elementos en buckets (sincronizado)
        for (int value : array) {
            int bucketIndex = (int) ((long) (value - min) * numBuckets / range);
            if (bucketIndex >= numBuckets) bucketIndex = numBuckets - 1;
            synchronized (buckets[bucketIndex]) {
                buckets[bucketIndex].add(value);
            }
        }
        
        // Calcular índices
        int[] indices = new int[numBuckets + 1];
        for (int i = 0; i < numBuckets; i++) {
            indices[i + 1] = indices[i] + buckets[i].size();
        }
        
        // Ordenar cada bucket en paralelo y combinar
        IntStream.range(0, numBuckets).parallel().forEach(i -> {
            int[] bucketArray = buckets[i].stream().mapToInt(Integer::intValue).toArray();
            Arrays.parallelSort(bucketArray);
            System.arraycopy(bucketArray, 0, array, indices[i], bucketArray.length);
        });
    }
}

