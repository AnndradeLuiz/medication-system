package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.InclusionProgram;
import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dto.PatientRequestDTO;
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

    public List<Patient> findAllStatus() {
        return repository.findByStatusTrue();
    }

    public List<Patient> search(String keyword, String status) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if ("todos".equalsIgnoreCase(status)) {
            return hasKeyword ? repository.searchAllByKeyword(keyword) : repository.findAll();
        }

        else if ("inativos".equalsIgnoreCase(status)) {
            return hasKeyword ? repository.searchInactiveByKeyword(keyword) : repository.findByStatusFalse();
        }

        else {
            return hasKeyword ? repository.searchActiveByKeyword(keyword) : repository.findByStatusTrue();
        }
    }

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient findById(String id) {
        Optional<Patient> patient = repository.findById(id);
        return patient.orElseThrow(() -> new ObjectNotFoundException("Paciente não encontrado."));
    }

    public Patient insert(Patient patient) {
        patient.setStatus(true);
        return repository.insert(patient);
    }

    public void update(Patient patient) {
        Patient newPatient = findById(patient.getId());
        updateData(newPatient, patient);
        repository.save(newPatient);
    }

    public void toggleStatus(String id) {
        Patient patient = findById(id);
        patient.setStatus(!patient.getStatus());
        repository.save(patient);
    }

    public Patient fromDto(PatientRequestDTO patientDto) {
        Patient patient = new Patient(null, patientDto.name(), patientDto.cpf(), patientDto.cns(), patientDto.birthDate(), null, patientDto.external());

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
        if (patient.getName() != null) {
            newPatient.setName(patient.getName());
        }
        if (patient.getCpf() != null) {
            newPatient.setCpf(patient.getCpf());
        }
        if (patient.getCns() != null) {
            newPatient.setCns(patient.getCns());
        }
        if (patient.getBirthDate() != null) {
            newPatient.setBirthDate(patient.getBirthDate());
        }
        if (patient.getExternal() != null) {
            newPatient.setExternal(patient.getExternal());
        }
        if (patient.getPhones() != null && !patient.getPhones().isEmpty()) {
            newPatient.setPhones(patient.getPhones());
        }
        if (patient.getPrograms() != null && !patient.getPrograms().isEmpty()) {
            newPatient.setPrograms(patient.getPrograms());
        }
    }

}
