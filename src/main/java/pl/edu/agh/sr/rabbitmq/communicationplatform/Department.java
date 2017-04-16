package pl.edu.agh.sr.rabbitmq.communicationplatform;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.DoctorThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private static final int doctorsNum = 2;
    private static final int techniciansNum = 2;

    public static final String[] specializations = {"knee", "ankle", "elbow"};

    private List<EmployeeThread> employees;
    private Administrator administrator;

    private Department() {
        employees = new ArrayList<>();
    }

    private void init() {
        administrator = new Administrator("Administrator");
        administrator.start();

        for (int i = 0; i < doctorsNum; i++) {
            DoctorThread newDoctor = new DoctorThread(this, "Doctor #" + i);
            newDoctor.start();
            employees.add(newDoctor);
        }

        for (int i = 0; i < techniciansNum; i++) {
            TechnicianThread newTechnician = new TechnicianThread(this, "Technician #" + i);
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
}
