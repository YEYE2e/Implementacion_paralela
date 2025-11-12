package experiment;

import generators.*;

public class ExperimentRunner {
    private static final int[] TAMANOS = {100, 1000, 10000, 100000, 1000000};
    private static final int ITERACIONES = 5;
    private ResultCollector recolector;
    private DataGenerator[] generadores;

    public ExperimentRunner() {
        this.recolector = new ResultCollector();
        this.generadores = new DataGenerator[]{
            new RandomGenerator(),
            new NearlySortedGenerator(),
            new ReversedGenerator(),
            new FewUniqueGenerator()
        };
    }

    public void runExperiments() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║     EXPERIMENTO DE ALGORITMOS DE SORT      ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        SystemInfo info = new SystemInfo();
        System.out.println(info + "\n");
        System.out.println("─".repeat(50));
        
        int totalPruebas = generadores.length * TAMANOS.length * ITERACIONES * 12;
        int pruebaActual = 0;
        
        for (DataGenerator gen : generadores) {
            System.out.println("\n📊 Tipo de datos: " + gen.getType());
            
            for (int tam : TAMANOS) {
                System.out.printf("  → Tamaño: %,d elementos\n", tam);
                
                for (int it = 0; it < ITERACIONES; it++) {
                    System.out.printf("     Iteración %d/%d... ", it + 1, ITERACIONES);
                    
                    int[] datos = gen.generate(tam);
                    
                    pruebaActual += probarAlgoritmo("MergeSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.MergeSort::sort);
                    pruebaActual += probarAlgoritmo("MergeSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelMergeSort::sort);
                    
                    pruebaActual += probarAlgoritmo("QuickSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.QuickSort::sort);
                    pruebaActual += probarAlgoritmo("QuickSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelQuickSort::sort);
                    
                    pruebaActual += probarAlgoritmo("RadixSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.RadixSort::sort);
                    pruebaActual += probarAlgoritmo("RadixSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelRadixSort::sort);
                    
                    pruebaActual += probarAlgoritmo("BitonicSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.BitonicSort::sort);
                    pruebaActual += probarAlgoritmo("BitonicSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelBitonicSort::sort);
                    
                    pruebaActual += probarAlgoritmo("SampleSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.SampleSort::sort);
                    pruebaActual += probarAlgoritmo("SampleSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelSampleSort::sort);
                    
                    pruebaActual += probarAlgoritmo("BucketSort", "Sequential", gen.getType(),
                        tam, it, datos.clone(), algorithms.sequential.BucketSort::sort);
                    pruebaActual += probarAlgoritmo("BucketSort", "Parallel", gen.getType(),
                        tam, it, datos.clone(), algorithms.parallel.ParallelBucketSort::sort);
                    
                    int porcentaje = (pruebaActual * 100) / totalPruebas;
                    System.out.printf("✓ [%d%%]\n", porcentaje);
                }
            }
        }
        
        System.out.println("\n" + "─".repeat(50));
        System.out.println("Total de pruebas realizadas: " + recolector.getResultCount());
        
        try {
            recolector.exportToCSV("results/experiment_results.csv");
        } catch (Exception e) {
            System.err.println("✗ Error al exportar resultados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int probarAlgoritmo(String algoritmo, String implementacion,
                                String tipoDato, int tamano, int iteracion,
                                int[] datos, AlgoritmoSort funcion) {
        System.gc();
        
        try {
            long inicioMs = System.currentTimeMillis();
            long inicioNs = System.nanoTime();
            
            funcion.sort(datos);
            
            long finNs = System.nanoTime();
            long finMs = System.currentTimeMillis();
            
            recolector.addResult(algoritmo, implementacion, tipoDato,
                tamano, iteracion,
                finMs - inicioMs,
                finNs - inicioNs);
            
            return 1;
            
        } catch (OutOfMemoryError e) {
            System.err.println("\n      ⚠ Error de memoria en " + algoritmo +
                " (" + implementacion + ") con " + tamano + " elementos");
            return 1;
        } catch (StackOverflowError e) {
            System.err.println("\n      ⚠ StackOverflowError en " + algoritmo +
                " (" + implementacion + ") con " + tamano + " elementos - " + tipoDato);
            return 1;
        } catch (Exception e) {
            System.err.println("\n      ✗ Error en " + algoritmo +
                " (" + implementacion + "): " + e.getMessage());
            return 1;
        } catch (Error e) {
            System.err.println("\n      ✗ Error crítico en " + algoritmo +
                " (" + implementacion + "): " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return 1;
        }
    }

    @FunctionalInterface
    interface AlgoritmoSort {
        void sort(int[] array);
    }
}
