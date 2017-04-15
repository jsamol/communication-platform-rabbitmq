package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.TechnicianFrame;

public class TechnicianThread extends EmployeeThread {
    private TechnicianFrame ui;

    public TechnicianThread(Department department, String name) {
        super(department);
        this.setName(name);

    }

    @Override
    public void run() {
        ui = new TechnicianFrame(this);
    }
}
