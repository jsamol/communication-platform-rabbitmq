package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import com.rabbitmq.client.*;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Administrator;
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
            channel.exchangeDeclare(Administrator.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String infoQueueName = channel.queueDeclare().getQueue();
            channel.queueBind(infoQueueName, Administrator.EXCHANGE_NAME, "");

            channel.queueDeclare(Administrator.LOG_QUEUE_NAME, true,
                    false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    ui.printMessage("! Admin info: " + message);
                }
            };

            channel.basicConsume(infoQueueName, true, consumer);
        } catch (Exception e) {
            department.log(
                    "<< " + this.getName() + " | error while creating new connection (Exception caught: " + e + "). >>"
            );
        }
    }

    void log(String log) throws IOException {
        channel.basicPublish("", Administrator.LOG_QUEUE_NAME, null, log.getBytes());
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
