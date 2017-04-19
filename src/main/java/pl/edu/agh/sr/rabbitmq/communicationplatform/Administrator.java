package pl.edu.agh.sr.rabbitmq.communicationplatform;

import com.rabbitmq.client.*;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.DoctorThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Administrator extends Thread {
    public static final String EXCHANGE_NAME = "info";
    public static final String LOG_QUEUE_NAME = "log";

    private Department department;

    private Connection connection;
    private Channel channel;

    Administrator(Department department, String name) {
        this.department = department;
        this.setName(name);
    }

    @Override
    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(LOG_QUEUE_NAME, true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String log = new String(body, "UTF-8");
                    log(log);
                }
            };

            channel.basicConsume(LOG_QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            log("<< Error while creating new connection (Exception caught: " + e + "). >>");
        }

        while(true) {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            try {
                String message = input.readLine();
                if (message.startsWith("\\r doctor")) {
                    String name = message.substring(10);
                    if (!checkIfAlreadyRunning(name)) {
                        DoctorThread newDoctor = new DoctorThread(department, name);
                        newDoctor.start();
                        department.getEmployees().add(newDoctor);
                    }
                }
                else if (message.startsWith("\\r technician")) {
                    String name = message.substring(14);
                    if(!checkIfAlreadyRunning(name)) {
                        TechnicianThread newTechnician = new TechnicianThread(department, name);
                        newTechnician.start();
                        department.getEmployees().add(newTechnician);
                    }
                }
                else if ("\\exit".equals(message)) {
                    for (EmployeeThread employee : department.getEmployees()) {
                        if (!employee.isInterrupted()) {
                            employee.interrupt();
                        }
                    }
                    exit();
                    System.exit(0);
                }
                else {
                    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                }
            } catch (IOException e) {
                log("<< Error while sending message (Exception caught: " + e + "). >>");
            }
        }
    }

    private boolean checkIfAlreadyRunning(String name) {
        for (EmployeeThread employee : department.getEmployees()) {
            if (employee.getName().equals(name)) {
                log("<< " + name + " is already working >>");
                return true;
            }
        }
        return false;
    }

    void log(String log) {
        System.out.println(log);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        log("Administrator interrupted.");
        for (EmployeeThread employee : department.getEmployees()) {
            employee.interrupt();
        }
        exit();
    }

    private void exit() {
        try {
            channel.close();
            connection.close();
        } catch (TimeoutException e) {
            log("<< error while closing the channel (Exception caught: " + e + "). >>");
        } catch (IOException e) {
            log("<< error while closing the connection (Exception caught: " + e + "). >>");
        }
    }
}
