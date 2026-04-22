package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.MedicalSupply;
import com.luiz.medication_system.dto.LotResponseDTO;
import com.luiz.medication_system.dto.MedicalSupplyRequestDTO;
import com.luiz.medication_system.dto.MedicalSupplyResponseDTO;
import com.luiz.medication_system.services.MedicalSupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/medical-supplies")
public class MedicalSupplyResource {

    private final MedicalSupplyService service;

    public MedicalSupplyResource(MedicalSupplyService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<MedicalSupplyResponseDTO>> findAll() {
        List<MedicalSupply> list = service.findAll();
        List<MedicalSupplyResponseDTO> dtoList = list.stream().map(MedicalSupplyResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<MedicalSupplyResponseDTO> findById(@PathVariable String id) {
        MedicalSupply medicalSupply = service.findById(id);
        return ResponseEntity.ok().body(new MedicalSupplyResponseDTO(medicalSupply));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody MedicalSupplyRequestDTO medicalDto) {
        MedicalSupply medicalSupply = service.fromDto(medicalDto);
        medicalSupply = service.insert(medicalSupply);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(medicalSupply.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody MedicalSupplyRequestDTO medicalDto, @PathVariable String id) {
        MedicalSupply medicalSupply = service.fromDto(medicalDto);
        medicalSupply.setId(id);
        service.update(medicalSupply);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/{id}/Lots", method = RequestMethod.GET)
    public ResponseEntity<List<LotResponseDTO>> findProgram(@PathVariable String id) {
        MedicalSupply medicalSupply = service.findById(id);
        List<LotResponseDTO> dtoList = medicalSupply.getLots().stream()
                .map(LotResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}