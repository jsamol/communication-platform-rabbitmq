package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.DoctorThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorFrame extends EmployeeFrame implements ActionListener {
    private JTextField textFieldType;
    private JTextField textFieldPatient;

    public DoctorFrame(DoctorThread doctor) {
        super(doctor);
        setSize(600, 400);

        initLayout();

        JLabel typeLabel = new JLabel("type");
        typeLabel.setBounds(12, 300, 48, 15);
        add(typeLabel);

        textFieldType = new JTextField();
        textFieldType.setBounds(75, 295, 300, 25);
        add(textFieldType);

        JLabel patientLabel = new JLabel("patient");
        patientLabel.setBounds(12, 330, 98, 15);
        add(patientLabel);

        textFieldPatient = new JTextField();
        textFieldPatient.setBounds(75, 325, 300, 25);
        add(textFieldPatient);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(385, 325, 80, 25);
        sendButton.addActionListener(this);
        add(sendButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Send")) {
            String type = textFieldType.getText();
            String patient = textFieldPatient.getText();

            if (type != null && patient != null) {
                textFieldType.setText(null);
                textFieldPatient.setText(null);

                ((DoctorThread) employee).orderTest(type, patient);
            }
        }
    }

    public void printMessage(String message) {
        textArea.append(message + "\n");
    }
}
