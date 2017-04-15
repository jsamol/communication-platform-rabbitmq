package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;

public class TechnicianFrame extends EmployeeFrame {

    public TechnicianFrame(TechnicianThread technician) {
        super(technician);
        setSize(600, 315);
        initLayout();
        setVisible(true);
    }
}
