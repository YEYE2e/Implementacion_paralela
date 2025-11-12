package algorithms.sequential;

import java.util.Arrays;

public class RadixSort {
    public static void sort(int[] arr) {
        if (arr.length < 2) return;

        int max = Arrays.stream(arr).max().orElse(0);

        for (int exp = 1; max / exp > 0; exp *= 10) {
            contar(arr, exp);
        }
    }

    private static void contar(int[] arr, int exp) {
        int n = arr.length;
        int[] salida = new int[n];
        int[] contador = new int[10];

        for (int i = 0; i < n; i++) {
            contador[(arr[i] / exp) % 10]++;
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
