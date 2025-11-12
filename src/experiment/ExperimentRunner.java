package experiment;

import generators.*;

import algorithms.sequential.*;

import algorithms.parallel.*;

import java.util.Arrays;

public class ExperimentRunner {

    private static final int[] SIZES = {100, 1000, 10000, 100000, 1000000};

    private static final int ITERATIONS = 5;

    

    private ResultCollector collector;

    private DataGenerator[] generators;

    

    public ExperimentRunner() {

        this.collector = new ResultCollector();

        this.generators = new DataGenerator[]{

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

        

        int totalTests = generators.length * SIZES.length * ITERATIONS * 12; // 6 algoritmos x 2 versiones

        int currentTest = 0;

        

        for (DataGenerator generator : generators) {

            System.out.println("\n📊 Tipo de datos: " + generator.getType());

            

            for (int size : SIZES) {

                System.out.printf("  → Tamaño: %,d elementos\n", size);

                

                for (int iter = 0; iter < ITERATIONS; iter++) {

                    System.out.printf("     Iteración %d/%d... ", iter + 1, ITERATIONS);

                    

                    int[] originalData = generator.generate(size);

                    

                    // Test todos los algoritmos

                    currentTest += testAlgorithm("MergeSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.MergeSort::sort);

                    currentTest += testAlgorithm("MergeSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelMergeSort::sort);

                    

                    currentTest += testAlgorithm("QuickSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.QuickSort::sort);

                    currentTest += testAlgorithm("QuickSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelQuickSort::sort);

                    

                    currentTest += testAlgorithm("RadixSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.RadixSort::sort);

                    currentTest += testAlgorithm("RadixSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelRadixSort::sort);

                    

                    currentTest += testAlgorithm("BitonicSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.BitonicSort::sort);

                    currentTest += testAlgorithm("BitonicSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelBitonicSort::sort);

                    

                    currentTest += testAlgorithm("SampleSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.SampleSort::sort);

                    currentTest += testAlgorithm("SampleSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelSampleSort::sort);

                    

                    currentTest += testAlgorithm("BucketSort", "Sequential", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.sequential.BucketSort::sort);

                    currentTest += testAlgorithm("BucketSort", "Parallel", generator.getType(), 

                                                size, iter, originalData.clone(), 

                                                algorithms.parallel.ParallelBucketSort::sort);

                    

                    int percentage = (currentTest * 100) / totalTests;

                    System.out.printf("✓ [%d%%]\n", percentage);

                }

            }

        }

        

        System.out.println("\n" + "─".repeat(50));

        System.out.println("Total de pruebas realizadas: " + collector.getResultCount());

        

        // Guardar resultados incluso si hubo errores

        try {

            collector.exportToCSV("results/experiment_results.csv");

        } catch (Exception e) {

            System.err.println("✗ Error al exportar resultados: " + e.getMessage());

            e.printStackTrace();

        }

    }

    

    private int testAlgorithm(String algorithm, String implementation,

                            String dataType, int size, int iteration,

                            int[] data, SortAlgorithm sortFunc) {

        System.gc(); // Sugerir limpieza de memoria

        

        try {

            long startMillis = System.currentTimeMillis();

            long startNanos = System.nanoTime();

            

            sortFunc.sort(data);

            

            long endNanos = System.nanoTime();

            long endMillis = System.currentTimeMillis();

            

            // Verificar que está ordenado (opcional, comentar para mayor velocidad)

            // verifySorted(data, algorithm, implementation);

            

            collector.addResult(algorithm, implementation, dataType, 

                              size, iteration,

                              endMillis - startMillis, 

                              endNanos - startNanos);

            

            return 1;

            

        } catch (OutOfMemoryError e) {

            System.err.println("\n      ⚠ Error de memoria en " + algorithm + 

                             " (" + implementation + ") con " + size + " elementos");

            return 1;

        } catch (StackOverflowError e) {

            System.err.println("\n      ⚠ StackOverflowError en " + algorithm + 

                             " (" + implementation + ") con " + size + " elementos - " + dataType);

            return 1;

        } catch (Exception e) {

            System.err.println("\n      ✗ Error en " + algorithm + 

                             " (" + implementation + "): " + e.getMessage());

            return 1;

        } catch (Error e) {

            System.err.println("\n      ✗ Error crítico en " + algorithm + 

                             " (" + implementation + "): " + e.getClass().getSimpleName() + " - " + e.getMessage());

            return 1;

        }

    }

    

    private void verifySorted(int[] array, String algorithm, String implementation) {

        for (int i = 1; i < array.length; i++) {

            if (array[i] < array[i-1]) {

                throw new RuntimeException("Array no ordenado correctamente por " + 

                                         algorithm + " " + implementation);

            }

        }

    }

    

    @FunctionalInterface

    interface SortAlgorithm {

        void sort(int[] array);

    }

}

