package experiment;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResultCollector {
    private List<String[]> resultados;
    private SystemInfo info;

    public ResultCollector() {
        this.resultados = new ArrayList<>();
        this.info = new SystemInfo();
        resultados.add(new String[]{
            "OS", "Processor", "RAM_MB", "Cores",
            "Algorithm", "Implementation", "DataType",
            "Size", "Iteration", "TimeMillis", "TimeNanos"
        });
    }

    public void addResult(String algoritmo, String implementacion,
                         String tipoDato, int tamano, int iteracion,
                         long tiempoMs, long tiempoNs) {
        resultados.add(new String[]{
            info.getOs(),
            info.getProcessor(),
            String.valueOf(info.getRamMB()),
            String.valueOf(info.getCores()),
            algoritmo,
            implementacion,
            tipoDato,
            String.valueOf(tamano),
            String.valueOf(iteracion),
            String.valueOf(tiempoMs),
            String.valueOf(tiempoNs)
        });
    }

    public void exportToCSV(String archivo) throws IOException {
        Files.createDirectories(Paths.get("results"));
        FileWriter escritor = new FileWriter(archivo);
        
        for (String[] fila : resultados) {
            StringBuilder linea = new StringBuilder();
            for (int i = 0; i < fila.length; i++) {
                String valor = fila[i];
                if (valor.contains(",") || valor.contains("\n") || valor.contains("\"")) {
                    valor = "\"" + valor.replace("\"", "\"\"") + "\"";
                }
                linea.append(valor);
                if (i < fila.length - 1) {
                    linea.append(",");
                }
            }
            escritor.append(linea.toString());
            escritor.append("\n");
        }
        
        escritor.flush();
        escritor.close();
        System.out.println("\n✓ Resultados exportados a: " + archivo);
    }

    public int getResultCount() {
        return resultados.size() - 1;
    }
}

