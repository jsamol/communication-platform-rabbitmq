package pl.edu.agh.sr.rabbitmq.communicationplatform.worker;

import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;

public class Technician extends Thread {
    private Department department;

    public Technician(Department department, String name) {
        this.department = department;
        this.setName(name);
    }

    @Override
    public void run() {
        super.run();
    }
}
