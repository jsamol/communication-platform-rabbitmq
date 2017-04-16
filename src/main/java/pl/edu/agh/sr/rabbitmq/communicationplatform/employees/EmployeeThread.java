package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import com.rabbitmq.client.*;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.EmployeeFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class EmployeeThread extends Thread {
    List<String> specializations;

    EmployeeFrame ui;

    Department department;

    private Connection connection;
    Channel channel;

    EmployeeThread(Department department, String name) {
        this.department = department;
        this.setName(name);

        specializations = new ArrayList<>();
    }

    void init() {
        department.log(this.getName() + " started working.");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            for (String specialization : specializations) {
                channel.queueDeclare(specialization, true, false, false, null);
            }
        } catch (Exception e) {
            department.log(
                    "<< " + this.getName() + " | error while creating new connection (Exception caught: " + e + "). >>"
            );
        }
    }

    @Override
    public void interrupt() {
        department.log(this.getName() + " finished working.\n");
        try {
            channel.close();
            connection.close();
        } catch (TimeoutException e) {
            department.log(
                    "<< " + this.getName() + " | error while closing the channel (Exception caught: " + e + "). >>"
            );
        } catch (IOException e) {
            department.log(
                    "<< " + this.getName() + " | error while closing the connection (Exception caught: " + e + "). >>"
            );
        }
    }
}
