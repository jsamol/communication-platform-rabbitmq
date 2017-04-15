package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.dialog.TechnicianDialog;

import javax.swing.*;
import java.util.List;

public class TechnicianFrame extends EmployeeFrame {

    private TechnicianThread technician;

    public TechnicianFrame(TechnicianThread technician) {
        super(technician);
        this.technician = technician;

        new TechnicianDialog(this);

        setSize(600, 315);
        JTextArea specializationsTextArea = new JTextArea();
        specializationsTextArea.setEditable(false);
        specializationsTextArea.setBounds(10, 10, 580, 25);
        specializationsTextArea.setText("Specializations: ");
        for (String specialization : technician.getSpecializations()) {
            specializationsTextArea.append(specialization + " ");
        }
        add(specializationsTextArea);
        initLayout(10, 45, 580, 230);
        setVisible(true);
    }

    public void setSpecializations(List<String> specializations) {
        technician.setSpecializations(specializations);
    }
}
