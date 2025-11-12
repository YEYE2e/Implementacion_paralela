package experiment;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ResultCollector {

    private List<String[]> results;

    private SystemInfo sysInfo;

    

    public ResultCollector() {

        this.results = new ArrayList<>();

        this.sysInfo = new SystemInfo();

        

        // Header

        results.add(new String[]{

            "OS", "Processor", "RAM_MB", "Cores",

            "Algorithm", "Implementation", "DataType", 

            "Size", "Iteration", "TimeMillis", "TimeNanos"

        });

    }

    

    public void addResult(String algorithm, String implementation, 

                         String dataType, int size, int iteration,

                         long timeMillis, long timeNanos) {

        results.add(new String[]{

            sysInfo.getOs(),

            sysInfo.getProcessor(),

            String.valueOf(sysInfo.getRamMB()),

            String.valueOf(sysInfo.getCores()),

            algorithm,

            implementation,

            dataType,

            String.valueOf(size),

            String.valueOf(iteration),

            String.valueOf(timeMillis),

            String.valueOf(timeNanos)

        });

    }

    

    public void exportToCSV(String filename) throws IOException {

        // Crear directorio si no existe

        Files.createDirectories(Paths.get("results"));

        

        FileWriter writer = new FileWriter(filename);

        

        for (String[] row : results) {

            // Escapar valores que contienen comas con comillas

            StringBuilder csvRow = new StringBuilder();

            for (int i = 0; i < row.length; i++) {

                String value = row[i];

                // Si el valor contiene coma, nueva línea o comilla, envolverlo en comillas

                if (value.contains(",") || value.contains("\n") || value.contains("\"")) {

                    value = "\"" + value.replace("\"", "\"\"") + "\"";

                }

                csvRow.append(value);

                if (i < row.length - 1) {

                    csvRow.append(",");

                }

            }

            writer.append(csvRow.toString());

            writer.append("\n");

        }

        

        writer.flush();

        writer.close();

        System.out.println("\n✓ Resultados exportados a: " + filename);

    }

    

    public int getResultCount() {

        return results.size() - 1; // Excluir header

    }

}

