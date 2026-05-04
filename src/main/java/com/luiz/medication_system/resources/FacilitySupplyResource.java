package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.FacilitySupply;
import com.luiz.medication_system.dto.FacilitySupplyRequestDTO;
import com.luiz.medication_system.dto.FacilitySupplyResponseDTO;
import com.luiz.medication_system.dto.LotRequestDTO;
import com.luiz.medication_system.dto.SupplyLotResponseDTO;
import com.luiz.medication_system.services.FacilitySupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(value = "supply-facilities")
public class FacilitySupplyResource {

    private final FacilitySupplyService service;

    public FacilitySupplyResource(FacilitySupplyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FacilitySupplyResponseDTO>> findAll() {
        List<FacilitySupplyResponseDTO> list = service.findAll()
                .stream().
                map(FacilitySupplyResponseDTO::new).
                toList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FacilitySupplyResponseDTO> findById(@PathVariable String id){
        FacilitySupply supply = service.findById(id);
        return ResponseEntity.ok().body(new FacilitySupplyResponseDTO(supply));
    }

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody FacilitySupplyRequestDTO supplyDto) {
        FacilitySupply supply = service.fromDto(supplyDto);
        supply = service.insert(supply);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(supply.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@RequestBody FacilitySupplyRequestDTO supplyDto, @PathVariable String id) {
        FacilitySupply supply = service.fromDto(supplyDto);
        supply.setId(id);
        service.update(supply);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/lots")
    public ResponseEntity<List<SupplyLotResponseDTO>> findLots(@PathVariable String id) {
        FacilitySupply supply = service.findById(id);
        List<SupplyLotResponseDTO> listDto = supply.getLots().
                stream().
                map(SupplyLotResponseDTO::new).
                toList();
        return ResponseEntity.ok().body(listDto);
    }

    @PostMapping(value = "/{id}/lots")
    public ResponseEntity<Void> addLots(@RequestBody List<LotRequestDTO> lotDto, @PathVariable String id) {
        service.addLots(id, lotDto);
        return ResponseEntity.noContent().build();
    }
}
