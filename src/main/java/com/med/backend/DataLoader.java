package com.med.backend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.entity.Patient;
import com.med.backend.persistence.entity.Doctor;
import com.med.backend.persistence.repository.UserRepository;
import com.med.backend.persistence.repository.PatientRepository;
import com.med.backend.persistence.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
        loadPatients();
        loadDoctors();
    }

    private void loadUsers() {
        System.out.println("Cargando datos desde data.json...");

        try (InputStream inputStream = new ClassPathResource("data.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            List<User> users = mapper.readValue(inputStream, new TypeReference<List<User>>() {});

            for (User user : users) {
                if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
                    userRepository.save(user);
                    System.out.println("Usuario " + user.getUsername() + " cargado.");
                } else {
                    System.out.println("Usuario " + user.getUsername() + " ya existe. No se cargará nuevamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPatients() {
        System.out.println("Cargando datos desde patient.json...");

        try (InputStream inputStream = new ClassPathResource("patient.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Patient> patients = mapper.readValue(inputStream, new TypeReference<List<Patient>>() {});

            for (Patient patient : patients) {
                if (!patientRepository.existsById(patient.getId())) {
                    patientRepository.save(patient);
                    System.out.println("Paciente con ID " + patient.getId() + " cargado.");
                } else {
                    System.out.println("Paciente con ID " + patient.getId() + " ya existe. No se cargará nuevamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        System.out.println("Cargando datos desde doctor.json...");

        try (InputStream inputStream = new ClassPathResource("doctor.json").getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            List<Doctor> doctors = mapper.readValue(inputStream, new TypeReference<List<Doctor>>() {});

            for (Doctor doctor : doctors) {
                if (!doctorRepository.existsById(doctor.getId())) {
                    doctorRepository.save(doctor);
                    System.out.println("Doctor con ID " + doctor.getId() + " cargado.");
                } else {
                    System.out.println("Doctor con ID " + doctor.getId() + " ya existe. No se cargará nuevamente.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
