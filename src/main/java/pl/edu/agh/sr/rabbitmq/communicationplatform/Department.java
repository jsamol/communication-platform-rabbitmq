package pl.edu.agh.sr.rabbitmq.communicationplatform;

import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.DoctorThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.EmployeeThread;
import pl.edu.agh.sr.rabbitmq.communicationplatform.employees.TechnicianThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Department {
    private static final int doctorsNum = 1;
    private static final int techniciansNum = 0;

    public static final String[] specializations = {"knee", "ankle", "elbow"};

    private BlockingQueue<EmployeeThread> employees;
    private Administrator administrator;

    private Department() {
        employees = new LinkedBlockingQueue<>();
    }

    private void init() {
        administrator = new Administrator(this, "Administrator");
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
        } catch (InterruptedException ignored) {

        }
    }

    public void log(String log) {
        administrator.log(log);
    }

    public BlockingQueue<EmployeeThread> getEmployees() {
        return employees;
    }
}
