package pl.edu.agh.sr.rabbitmq.communicationplatform;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.DoctorThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private static final int doctorsNum = 1;
    private static final int techniciansNum = 1;

    private static final String[] specializations = {"knee", "ankle", "elbow"};

    private List<EmployeeThread> employees;
    private Administrator administrator;

    private Department() {
        employees = new ArrayList<>();
    }

    private void init() {
        administrator = new Administrator("Administrator");

        for (int i = 0; i < doctorsNum; i++) {
            DoctorThread newDoctor = new DoctorThread(this, "DoctorThread #" + i);
            newDoctor.start();
            employees.add(newDoctor);
        }

        for (int i = 0; i < techniciansNum; i++) {
            TechnicianThread newTechnician = new TechnicianThread(this, "TechnicianThread #" + i);
            newTechnician.start();
            employees.add(newTechnician);
        }
    }

    private void waitForEmployees() throws InterruptedException {
        for (EmployeeThread employee : employees) {
            employee.join();
        }

        administrator.join();
    }

    public static void main(String[] args) {
        Department department = new Department();
        department.init();

        try {
            department.waitForEmployees();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String log) {
        administrator.log(log);
    }

    public static String[] getSpecializations() {
        return specializations;
    }
}
