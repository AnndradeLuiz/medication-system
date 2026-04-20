package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dto.InclusionProgramDTO;
import com.luiz.medication_system.dto.PatientDTO;
import com.luiz.medication_system.dto.PatientInsertDTO;
import com.luiz.medication_system.services.PatientService;
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
    public ResponseEntity<List<PatientDTO>> findAll() {
        List<Patient> list = service.findAll();
        List<PatientDTO> dtoList = list.stream().map(PatientDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PatientDTO> findById(@PathVariable String id) {
        Patient patient = service.findById(id);
        return ResponseEntity.ok().body(new PatientDTO(patient));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody PatientInsertDTO patientInsertDTO) {
        Patient patient = service.fromDto(patientInsertDTO);
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
    public ResponseEntity<Void> update(@RequestBody PatientInsertDTO patientInsertDTO, @PathVariable String id) {
        Patient patient = service.fromDto(patientInsertDTO);
        patient.setId(id);
        service.update(patient);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/{id}/programs", method = RequestMethod.GET)
    public ResponseEntity<List<InclusionProgramDTO>> findProgram(@PathVariable String id) {
        Patient patient = service.findById(id);
        List<InclusionProgramDTO> dtoList = patient.getPrograms().stream()
                .map(InclusionProgramDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}