package algorithms.sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketSort {
    public static void sort(int[] array) {
        if (array.length < 2) return;

        int max = Arrays.stream(array).max().orElse(0);
        int min = Arrays.stream(array).min().orElse(0);
        int range = max - min + 1;
        int numBuckets = (int) Math.sqrt(array.length);

        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new ArrayList[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }

        // Distribuir elementos en buckets
        for (int value : array) {
            int bucketIndex = (int) ((long) (value - min) * numBuckets / range);
            if (bucketIndex >= numBuckets) bucketIndex = numBuckets - 1;
            buckets[bucketIndex].add(value);
        }

        // Ordenar cada bucket y combinar
        int index = 0;
        for (List<Integer> bucket : buckets) {
            int[] bucketArray = bucket.stream().mapToInt(i -> i).toArray();
            Arrays.sort(bucketArray);
            for (int value : bucketArray) {
                array[index++] = value;
            }
        }
    }
}