package pl.edu.agh.sr.rabbitmq.communicationplatform.ui.dialog;

import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.ui.frame.TechnicianFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TechnicianDialog extends JDialog implements ActionListener {
    private TechnicianFrame technicianFrame;

    private List<JCheckBox> checkBoxList;

    public TechnicianDialog(JFrame parent) {
        super(parent, true);
        this.technicianFrame = (TechnicianFrame) parent;
        setTitle("Select specialization");
        checkBoxList = new ArrayList<>();
        setLocationRelativeTo(null);
        initLayout();
        setVisible(true);
    }

    private void initLayout() {
        setLayout(null);
        setResizable(false);
        setSize(160, 200);

        JPanel panel = new JPanel();
        panel.setBounds(30, 10, 100, 100);
        panel.setLayout(new GridLayout(3, 1));
        add(panel);

        for (String specialization : Department.getSpecializations()) {
            JCheckBox checkBox = new JCheckBox(specialization);
            checkBox.setSelected(false);
            checkBoxList.add(checkBox);
            panel.add(checkBox);
        }

        JButton okButton = new JButton("OK");
        okButton.setBounds(30, 120, 80, 25);
        okButton.addActionListener(this);
        add(okButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("OK")) {
            List<String> specializations = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    specializations.add(checkBox.getText());
                }
            }
            technicianFrame.setSpecializations(specializations);
            setVisible(false);
        }
    }
}
