package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.TechnicianFrame;

import java.util.List;

public class TechnicianThread extends EmployeeThread {

    public TechnicianThread(Department department, String name) {
        super(department);
        this.setName(name);
    }

    @Override
    public void run() {
        ui = new TechnicianFrame(this);
        init();
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations.addAll(specializations);
    }

    public List<String> getSpecializations() {
        return specializations;
    }
}
