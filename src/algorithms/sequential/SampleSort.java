package algorithms.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleSort {
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
        Arrays.sort(samples);

        // Crear pivotes
        int[] pivots = new int[numBuckets - 1];
        for (int i = 0; i < pivots.length; i++) {
            pivots[i] = samples[(i + 1) * samples.length / numBuckets];
        }

        // Distribuir en buckets
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (int value : array) {
            int bucket = findBucket(value, pivots);
            buckets[bucket].add(value);
        }

        // Ordenar buckets y combinar
        int index = 0;
        for (List<Integer> bucket : buckets) {
            int[] bucketArray = bucket.stream().mapToInt(i -> i).toArray();
            Arrays.sort(bucketArray);
            for (int value : bucketArray) {
                array[index++] = value;
            }
        }
    }

    private static int findBucket(int value, int[] pivots) {
        for (int i = 0; i < pivots.length; i++) {
            if (value < pivots[i]) return i;
        }
        return pivots.length;
    }
}