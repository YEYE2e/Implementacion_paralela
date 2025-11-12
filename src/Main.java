public class Main {
    public static void main(String[] args) {
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("   EVALUACIÓN EMPÍRICA DE ALGORITMOS DE SORT       ");
        System.out.println("   Implementaciones Secuenciales vs Paralelas      ");
        System.out.println("═══════════════════════════════════════════════════");
        
        experiment.ExperimentRunner ejecutor = new experiment.ExperimentRunner();
        
        long inicio = System.currentTimeMillis();
        ejecutor.runExperiments();
        long fin = System.currentTimeMillis();
        
        long segundos = (fin - inicio) / 1000;
        long minutos = segundos / 60;
        long seg = segundos % 60;
        
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("            EXPERIMENTOS COMPLETADOS                ");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.printf("\n Tiempo total: %d min %d seg\n", minutos, seg);
        System.out.println("\nResultados en: results/experiment_results.csv\n");
    }
}