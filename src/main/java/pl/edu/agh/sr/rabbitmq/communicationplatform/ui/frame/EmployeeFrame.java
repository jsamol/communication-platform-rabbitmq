package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmployeeFrame extends JFrame {
    EmployeeThread employee;

    private JTextArea textArea;

    EmployeeFrame(EmployeeThread employee) {
        super(employee.getName());
        this.employee = employee;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                exit();
            }
        });
        setLocationRelativeTo(null);
    }

    void initLayout(int x, int y, int width, int height) {
        setResizable(false);
        setLayout(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPaneMain = new JScrollPane(textArea);
        scrollPaneMain.setBounds(x, y, width, height);
        add(scrollPaneMain);
    }

    public void printMessage(String message) {
        textArea.append(message + "\n");
    }

    private void exit() {
        employee.interrupt();
        dispose();
    }
}
