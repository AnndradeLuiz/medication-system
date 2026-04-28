package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;
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
    public ResponseEntity<List<PatientResponseDTO>> findAll(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "status", defaultValue = "ativos") String status) {

        List<Patient> list = service.search(query, status);
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

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<Void> toggleStatus(@PathVariable String id) {
        service.toggleStatus(id);
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
    public ResponseEntity<List<ProgramCategoryEnum>> findProgram(@PathVariable String id) {
        Patient patient = service.findById(id);
        List<ProgramCategoryEnum> dtoList = patient.getPrograms();
        return ResponseEntity.ok().body(dtoList);
    }

}