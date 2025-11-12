public class Main {

    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║                                                   ║");
        System.out.println("║   EVALUACIÓN EMPÍRICA DE ALGORITMOS DE SORT       ║");
        System.out.println("║   Implementaciones Secuenciales vs Paralelas      ║");
        System.out.println("║                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        
        experiment.ExperimentRunner runner = new experiment.ExperimentRunner();
        
        long startTime = System.currentTimeMillis();
        runner.runExperiments();
        long endTime = System.currentTimeMillis();
        
        long totalSeconds = (endTime - startTime) / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║            EXPERIMENTOS COMPLETADOS               ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.printf("\n⏱ Tiempo total de ejecución: %d min %d seg\n", minutes, seconds);
        System.out.println("\n¡Revisa el archivo results/experiment_results.csv!\n");
    }

}