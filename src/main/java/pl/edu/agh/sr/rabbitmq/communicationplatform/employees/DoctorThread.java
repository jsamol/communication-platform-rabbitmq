package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.DoctorFrame;

import java.io.IOException;
import java.util.Arrays;

public class DoctorThread extends EmployeeThread {

    public DoctorThread(Department department, String name) {
        super(department);
        this.setName(name);
        specializations.addAll(Arrays.asList(Department.getSpecializations()));
    }

    @Override
    public void run() {
        ui = new DoctorFrame(this);
        init();
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                ui.printMessage("type \"" + envelope.getRoutingKey() + "\": " + message);
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            department.log(
                    "<< " + this.getName() + " | error while setting consumer (Exception caught: " + e + "). >>"
            );
        }
    }

    public void orderTest(String type, String patient) {
        try {
            channel.basicPublish(EXCHANGE_NAME, type, null, patient.getBytes());
        } catch (IOException e) {
            department.log(
                    "<< " + this.getName() + " | error while ordering a test (Exception caught: " + e + "). >>"
            );
        }
    }
}
