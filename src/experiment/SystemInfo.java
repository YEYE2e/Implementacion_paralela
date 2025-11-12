package experiment;

public class SystemInfo {
    private String so;
    private String cpu;
    private long ram;
    private int nucleos;

    public SystemInfo() {
        this.so = System.getProperty("os.name") + " " + System.getProperty("os.version");
        this.cpu = System.getenv("PROCESSOR_IDENTIFIER");
        if (this.cpu == null) {
            this.cpu = "Unknown Processor";
        }
        this.ram = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        this.nucleos = Runtime.getRuntime().availableProcessors();
    }

    public String getOs() { return so; }
    public String getProcessor() { return cpu; }
    public long getRamMB() { return ram; }
    public int getCores() { return nucleos; }

    @Override
    public String toString() {
        return String.format("OS: %s | CPU: %s | RAM: %d MB | Cores: %d", so, cpu, ram, nucleos);
    }
}

