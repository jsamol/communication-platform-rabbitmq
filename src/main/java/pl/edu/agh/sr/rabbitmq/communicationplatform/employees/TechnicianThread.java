package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import com.rabbitmq.client.*;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.TechnicianFrame;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class TechnicianThread extends EmployeeThread {

    public TechnicianThread(Department department, String name) {
        super(department, name);
    }

    @Override
    public void run() {
        ui = new TechnicianFrame(this);
        init();
        try {
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    String[] messageArray = message.split("&");
                    ui.printMessage(
                            "[" + messageArray[0] + "] type \"" + envelope.getRoutingKey() + "\": " + messageArray[1]
                    );
                    makeTest();
                    AMQP.BasicProperties props = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    sendTestResult(properties.getReplyTo(), props,
                            "[" + getName() + "] " + messageArray[1] +
                                    ", test results, type: " + envelope.getRoutingKey() +
                                    ", test ID: " + properties.getCorrelationId()
                    );
                }
            };
            for (String specialization : specializations) {
                channel.basicConsume(specialization, true, consumer);
            }
        } catch (IOException e) {
            department.log(
                    "<< " + this.getName() + " | error while setting up (Exception caught: " + e + "). >>"
            );
        }
    }

    private void makeTest() {
        Random random = new Random();
        try {
            Thread.sleep((random.nextInt(5) + 5) * 1000);
        } catch (InterruptedException e) {
            department.log(
                    "<< " + this.getName() + " | error while making a test (Exception caught: " + e + "). >>"
            );
        }
    }

    private void sendTestResult(String replyTo, AMQP.BasicProperties props, String message) {
        try {
            channel.basicPublish("", replyTo, props, message.getBytes());
        } catch (IOException e) {
            department.log(
                    "<< " + this.getName() + " | error while sending results (Exception caught: " + e + "). >>"
            );
        }
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations.addAll(specializations);
    }

    public List<String> getSpecializations() {
        return specializations;
    }
}
