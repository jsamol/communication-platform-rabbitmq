package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.dialog.EmployeeDialog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class EmployeeFrame extends JFrame {
    EmployeeThread employee;

    JTextArea textArea;

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

        new EmployeeDialog(this);
    }

    void initLayout() {
        setResizable(false);
        setLayout(null);

        JTextArea specializationsTextArea = new JTextArea();
        specializationsTextArea.setEditable(false);
        specializationsTextArea.setBounds(10, 10, 580, 25);
        specializationsTextArea.setText("Specializations: ");
        for (String specialization : employee.getSpecializations()) {
            specializationsTextArea.append(specialization + " ");
        }
        add(specializationsTextArea);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPaneMain = new JScrollPane(textArea);
        scrollPaneMain.setBounds(10, 45, 580, 230);
        add(scrollPaneMain);
    }

    public void setSpecializations(List<String> specializations) {
        employee.setSpecializations(specializations);
    }

    private void exit() {
        employee.interrupt();
        dispose();
    }
}
