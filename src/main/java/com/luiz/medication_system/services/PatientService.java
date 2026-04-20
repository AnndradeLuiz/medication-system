package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.InclusionProgram;
import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dto.PatientInsertDTO;
import com.luiz.medication_system.repository.PatientRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient findById(String id) {
        Optional<Patient> patient = repository.findById(id);
        return patient.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public Patient insert(Patient patient) {
        return repository.insert(patient);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(Patient patient) {
        Patient newPatient = findById(patient.getId());
        updateData(newPatient, patient);
        repository.save(newPatient);
    }

    public Patient fromDto(PatientInsertDTO patientDto) {
        Patient patient = new Patient(patientDto.id(), patientDto.name(), patientDto.cpf(), patientDto.cns(), patientDto.birthDate());

        if (patientDto.phones() != null) {
            patient.getPhones().addAll(patientDto.phones());
        }

        if (patientDto.programs() != null) {
            List<InclusionProgram> programsEntity = patientDto.programs().stream()
                    .map(p -> new InclusionProgram(p.name(), p.inclusionDate(), p.status()))
                    .toList();
            patient.getPrograms().addAll(programsEntity);
        }
        return patient;
    }

    private void updateData(Patient newPatient, Patient patient) {
        newPatient.setName(patient.getName());
        newPatient.setCpf(patient.getCpf());
        newPatient.setCns(patient.getCns());
        newPatient.setBirthDate(patient.getBirthDate());
        newPatient.setPhones(patient.getPhones());
        newPatient.setPrograms(patient.getPrograms());
    }

}
