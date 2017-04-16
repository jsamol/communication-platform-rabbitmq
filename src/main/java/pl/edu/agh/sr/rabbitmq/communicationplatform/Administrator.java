package pl.edu.agh.sr.rabbitmq.communicationplatform;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class Administrator extends Thread {
    public static final String EXCHANGE_NAME = "info";
    public static final String LOG_QUEUE_NAME = "log";
    private Connection connection;
    private Channel channel;

    Administrator(String name) {
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
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            } catch (IOException e) {
                log("<< Error while sending message (Exception caught: " + e + "). >>");
            }
        }
    }

    void log(String log) {
        System.out.println(log);
    }

    @Override
    public void interrupt() {
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
