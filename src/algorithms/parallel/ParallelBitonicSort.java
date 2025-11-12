package algorithms.parallel;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class ParallelBitonicSort {
    private static final int UMBRAL = 1000;
    
    public static void sort(int[] arr) {
        int n = arr.length;
        int tam = 1;
        while (tam < n) tam *= 2;
        
        int[] arrRelleno = new int[tam];
        System.arraycopy(arr, 0, arrRelleno, 0, n);
        for (int i = n; i < tam; i++) {
            arrRelleno[i] = Integer.MAX_VALUE;
        }
        
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new TareaBitonicSort(arrRelleno, 0, tam, true));
        
        System.arraycopy(arrRelleno, 0, arr, 0, n);
    }
    
    private static class TareaBitonicSort extends RecursiveAction {
        private int[] arr;
        private int inicio, cnt;
        private boolean dir;
        
        TareaBitonicSort(int[] arr, int inicio, int cnt, boolean dir) {
            this.arr = arr;
            this.inicio = inicio;
            this.cnt = cnt;
            this.dir = dir;
        }
        
        @Override
        protected void compute() {
            if (cnt <= UMBRAL) {
                ordenarSecuencial(arr, inicio, cnt, dir);
                return;
            }
            
            if (cnt > 1) {
                int k = cnt / 2;
                TareaBitonicSort t1 = new TareaBitonicSort(arr, inicio, k, true);
                TareaBitonicSort t2 = new TareaBitonicSort(arr, inicio + k, k, false);
                invokeAll(t1, t2);
                new TareaMezclaBitonic(arr, inicio, cnt, dir).invoke();
            }
        }
        
        private void ordenarSecuencial(int[] arr, int inicio, int cnt, boolean dir) {
            if (cnt > 1) {
                int k = cnt / 2;
                ordenarSecuencial(arr, inicio, k, true);
                ordenarSecuencial(arr, inicio + k, k, false);
                mezclarSecuencial(arr, inicio, cnt, dir);
            }
        }
        
        private void mezclarSecuencial(int[] arr, int inicio, int cnt, boolean dir) {
            if (cnt > 1) {
                int k = cnt / 2;
                for (int i = inicio; i < inicio + k; i++) {
                    compararIntercambiar(arr, i, i + k, dir);
                }
                mezclarSecuencial(arr, inicio, k, dir);
                mezclarSecuencial(arr, inicio + k, k, dir);
            }
        }
    }
    
    private static class TareaMezclaBitonic extends RecursiveAction {
        private int[] arr;
        private int inicio, cnt;
        private boolean dir;
        
        TareaMezclaBitonic(int[] arr, int inicio, int cnt, boolean dir) {
            this.arr = arr;
            this.inicio = inicio;
            this.cnt = cnt;
            this.dir = dir;
        }
        
        @Override
        protected void compute() {
            if (cnt > 1) {
                int k = cnt / 2;
                for (int i = inicio; i < inicio + k; i++) {
                    compararIntercambiar(arr, i, i + k, dir);
                }
                
                TareaMezclaBitonic t1 = new TareaMezclaBitonic(arr, inicio, k, dir);
                TareaMezclaBitonic t2 = new TareaMezclaBitonic(arr, inicio + k, k, dir);
                invokeAll(t1, t2);
            }
        }
    }
    
    private static void compararIntercambiar(int[] arr, int i, int j, boolean dir) {
        if (dir == (arr[i] > arr[j])) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}
