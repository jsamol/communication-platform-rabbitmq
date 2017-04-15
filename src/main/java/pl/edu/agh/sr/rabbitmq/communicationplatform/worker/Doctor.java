package pl.edu.agh.sr.rabbitmq.communicationplatform.worker;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;

import java.util.List;

public class Doctor extends Thread {
    private Department department;
    private List<String> specializations;

    public Doctor(Department department, String name, String... specialization) {
        this.department = department;
        this.setName(name);
    }

    @Override
    public void run() {
        department.log(this.getName() + " started working.\n");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
        } catch (Exception e) {
            department.log(
                    "<<" + this.getName() + "| error while creating new connection (Exception caught: " + e + ").>>\n"
            );
        }
    }

    @Override
    public void interrupt() {
        department.log(this.getName() + " finished working.\n");
    }
}
