package Http.Unit;

public class Process {
    private static int processesCounter = 0;

    private int id;
    private String processName;
    private String status;

    public Process(String processName) {
        id = processesCounter++;
        this.processName = processName;
        status = "created";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
