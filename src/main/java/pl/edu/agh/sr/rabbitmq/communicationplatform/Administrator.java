package pl.edu.agh.sr.rabbitmq.communicationplatform;

public class Administrator extends Thread {

    public Administrator(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        super.run();
    }

    public void log(String log) {
        System.out.println(log);
    }
}
