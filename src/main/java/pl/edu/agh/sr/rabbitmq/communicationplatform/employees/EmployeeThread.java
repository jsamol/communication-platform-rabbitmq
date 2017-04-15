package pl.edu.agh.sr.rabbitmq.communicationplatform.employees;

import javafx.application.Application;
import pl.edu.agh.sr.rabbitmq.communicationplatform.Department;

import java.util.ArrayList;
import java.util.List;

public class EmployeeThread extends Thread {
    Department department;

    List<String> specializations;

    EmployeeThread(Department department) {
        this.department = department;

        specializations = new ArrayList<>();
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations.addAll(specializations);
    }

    public List<String> getSpecializations() {
        return specializations;
    }
}
