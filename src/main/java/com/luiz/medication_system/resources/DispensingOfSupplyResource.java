package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.DispensingOfSupply;
import com.luiz.medication_system.dto.DispensingOfSupplyRequestDTO;
import com.luiz.medication_system.dto.DispensingOfSupplyResponseDTO;
import com.luiz.medication_system.services.DispensingOfSupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/dispensing-supplies")
public class DispensingOfSupplyResource {

    private final DispensingOfSupplyService service;

    public DispensingOfSupplyResource(DispensingOfSupplyService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DispensingOfSupplyResponseDTO>> findAll() {
        List<DispensingOfSupply> list = service.findAll();
        List<DispensingOfSupplyResponseDTO> dtoList = list.stream().map(DispensingOfSupplyResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DispensingOfSupplyResponseDTO> findById(@PathVariable String id) {
        DispensingOfSupply entity = service.findById(id);
        return ResponseEntity.ok().body(new DispensingOfSupplyResponseDTO(entity));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody DispensingOfSupplyRequestDTO entityDto) {
        DispensingOfSupply entity = service.fromDto(entityDto);
        entity = service.insert(entity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    // Método somente para testes, será apagado
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Acredito que está função será removida, pois não me faz sentido dar update no registro das saídas
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody DispensingOfSupplyRequestDTO entityDto, @PathVariable String id) {
        DispensingOfSupply entity = service.fromDto(entityDto);
        entity.setId(id);
        service.update(entity);
        return ResponseEntity.noContent().build();
    }

}