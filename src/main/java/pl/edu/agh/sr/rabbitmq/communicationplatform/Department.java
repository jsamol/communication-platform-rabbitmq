package pl.edu.agh.sr.rabbitmq.communicationplatform;

import pl.edu.agh.sr.rabbitmq.communicationplatform.worker.Administrator;
import pl.edu.agh.sr.rabbitmq.communicationplatform.worker.Doctor;
import pl.edu.agh.sr.rabbitmq.communicationplatform.worker.Technician;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Department {
    private static final int doctorsNum = 2;
    private static final int techniciansNum = 2;

    private static final String[] specializations = {"knee", "ankle", "elbow"};

    private List<Doctor> doctors;
    private List<Technician> technicians;
    private Administrator administrator;

    private Department() {
        doctors = new ArrayList<>();
        technicians = new ArrayList<>();
    }

    private void init() {
        for (int i = 0; i < doctorsNum; i++) {
            Doctor newDoctor = new Doctor(this, "Doctor #" + i);
            newDoctor.start();
            doctors.add(newDoctor);
        }

        for (int i = 0; i < techniciansNum; i++) {
            Technician newTechnician = new Technician(this, "Technician #" + i);
            newTechnician.start();
            technicians.add(newTechnician);
        }

        administrator = new Administrator("Administrator");

    }

    private void waitForWorkers() throws InterruptedException {
        for (Doctor doctor : doctors) {
            doctor.join();
        }

        for (Technician technician : technicians) {
            technician.join();
        }

        administrator.join();
    }

    public static void main(String[] args) {
        Department department = new Department();
        department.init();

        try {
            department.waitForWorkers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String log) {
        administrator.log(log);
    }
}
