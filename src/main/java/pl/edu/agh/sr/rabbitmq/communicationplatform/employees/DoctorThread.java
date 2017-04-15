package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.DoctorFrame;

public class DoctorThread extends EmployeeThread {
    private DoctorFrame ui;

    public DoctorThread(Department department, String name) {
        super(department);
        this.setName(name);
    }

    @Override
    public void run() {
        ui = new DoctorFrame(this);
        department.log(this.getName() + " started working.");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
        } catch (Exception e) {
            department.log(
                    "<< " + this.getName() + " | error while creating new connection (Exception caught: " + e + "). >>"
            );
        }
    }

    @Override
    public void interrupt() {
        department.log(this.getName() + " finished working.\n");
    }

    public void orderTest(String type, String patient) {

    }
}
