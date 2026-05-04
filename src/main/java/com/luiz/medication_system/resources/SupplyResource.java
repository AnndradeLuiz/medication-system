package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Supply;
import com.luiz.medication_system.dto.LotRequestDTO;
import com.luiz.medication_system.dto.SupplyLotResponseDTO;
import com.luiz.medication_system.dto.SupplyRequestDTO;
import com.luiz.medication_system.dto.SupplyResponseDTO;
import com.luiz.medication_system.services.SupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/supplies")
public class SupplyResource {

    private final SupplyService service;

    public SupplyResource(SupplyService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SupplyResponseDTO>> findAll() {
        List<Supply> list = service.findAll();
        List<SupplyResponseDTO> dtoList = list.stream().map(SupplyResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SupplyResponseDTO> findById(@PathVariable String id) {
        Supply supply = service.findById(id);
        return ResponseEntity.ok().body(new SupplyResponseDTO(supply));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody SupplyRequestDTO medicalDto) {
        Supply supply = service.fromDto(medicalDto);
        supply = service.insert(supply);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(supply.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody SupplyRequestDTO medicalDto, @PathVariable String id) {
        Supply supply = service.fromDto(medicalDto);
        supply.setId(id);
        service.update(supply);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/{id}/lots", method = RequestMethod.GET)
    public ResponseEntity<List<SupplyLotResponseDTO>> findLots(@PathVariable String id) {
        Supply supply = service.findById(id);
        List<SupplyLotResponseDTO> dtoList = supply.getLots().stream()
                .map(SupplyLotResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}/lots", method = RequestMethod.POST)
    public ResponseEntity<Void> addLots(
            @PathVariable String id,
            @RequestBody List<LotRequestDTO> lotsDto) {

        service.addLots(id, lotsDto);

        return ResponseEntity.noContent().build();
    }

}
