package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dto.*;
import com.luiz.medication_system.services.MedicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/medications")
public class MedicationResource {

    private final MedicationService service;

    public MedicationResource(MedicationService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MedicationDTO>> findAll() {
        List<Medication> list = service.findAll();
        List<MedicationDTO> dtoList = list.stream().map(MedicationDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MedicationDTO> findById(@PathVariable String id) {
        Medication medication = service.findById(id);
        return ResponseEntity.ok().body(new MedicationDTO(medication));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody MedicationInsertDTO medicationDto) {
        Medication medication = service.fromDto(medicationDto);
        medication = service.insert(medication);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(medication.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody MedicationInsertDTO medicationDto, @PathVariable String id) {
        Medication medication = service.fromDto(medicationDto);
        medication.setId(id);
        service.update(medication);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/{id}/Lots", method = RequestMethod.GET)
    public ResponseEntity<List<LotDTO>> findProgram(@PathVariable String id) {
        Medication medication = service.findById(id);
        List<LotDTO> dtoList = medication.getLots().stream()
                .map(LotDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}