package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dto.InclusionProgramRequestDTO;
import com.luiz.medication_system.dto.InclusionProgramResponseDTO;
import com.luiz.medication_system.dto.PatientResponseDTO;
import com.luiz.medication_system.dto.PatientRequestDTO;
import com.luiz.medication_system.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
public class PatientResource {

    private final PatientService service;

    public PatientResource(PatientService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        List<Patient> list = service.findAll();
        List<PatientResponseDTO> dtoList = list.stream().map(PatientResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable String id) {
        Patient patient = service.findById(id);
        return ResponseEntity.ok().body(new PatientResponseDTO(patient));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        Patient patient = service.fromDto(patientRequestDTO);
        patient = service.insert(patient);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patient.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody PatientRequestDTO patientRequestDTO, @PathVariable String id) {
        Patient patient = service.fromDto(patientRequestDTO);
        patient.setId(id);
        service.update(patient);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/{id}/programs", method = RequestMethod.GET)
    public ResponseEntity<List<InclusionProgramResponseDTO>> findProgram(@PathVariable String id) {
        Patient patient = service.findById(id);
        List<InclusionProgramResponseDTO> dtoList = patient.getPrograms().stream()
                .map(InclusionProgramResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}