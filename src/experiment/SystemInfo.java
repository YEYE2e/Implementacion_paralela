package experiment;

public class SystemInfo {

    private String os;

    private String processor;

    private long ramMB;

    private int cores;

    

    public SystemInfo() {

        this.os = System.getProperty("os.name") + " " + 

                  System.getProperty("os.version");

        this.processor = System.getenv("PROCESSOR_IDENTIFIER");

        if (this.processor == null) {

            this.processor = "Unknown Processor";

        }

        this.ramMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        this.cores = Runtime.getRuntime().availableProcessors();

    }

    

    public String getOs() { return os; }

    public String getProcessor() { return processor; }

    public long getRamMB() { return ramMB; }

    public int getCores() { return cores; }

    

    @Override

    public String toString() {

        return String.format("OS: %s | CPU: %s | RAM: %d MB | Cores: %d",

                           os, processor, ramMB, cores);

    }

}

