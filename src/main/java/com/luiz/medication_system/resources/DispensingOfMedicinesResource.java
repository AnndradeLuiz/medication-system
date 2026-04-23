package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.DispensingOfMedicines;
import com.luiz.medication_system.dto.DispensationItemResponseDTO;
import com.luiz.medication_system.dto.DispensingOfMedicinesResponseDTO;
import com.luiz.medication_system.dto.DispensingOfMedicinesRequestDTO;
import com.luiz.medication_system.services.DispensingOfMedicinesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/dispensations")
public class DispensingOfMedicinesResource {

    private final DispensingOfMedicinesService service;

    public DispensingOfMedicinesResource(DispensingOfMedicinesService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DispensingOfMedicinesResponseDTO>> findAll() {
        List<DispensingOfMedicines> list = service.findAll();
        List<DispensingOfMedicinesResponseDTO> dtoList = list.stream().map(DispensingOfMedicinesResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DispensingOfMedicinesResponseDTO> findById(@PathVariable String id) {
        DispensingOfMedicines entity = service.findById(id);
        return ResponseEntity.ok().body(new DispensingOfMedicinesResponseDTO(entity));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody DispensingOfMedicinesRequestDTO entityDto) {
        DispensingOfMedicines entity = service.fromDto(entityDto);
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
    public ResponseEntity<Void> update(@RequestBody DispensingOfMedicinesRequestDTO entityDto, @PathVariable String id) {
        DispensingOfMedicines entity = service.fromDto(entityDto);
        entity.setId(id);
        service.update(entity);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public ResponseEntity<List<DispensationItemResponseDTO>> findItems(@PathVariable String id) {
        DispensingOfMedicines item = service.findById(id);
        List<DispensationItemResponseDTO> dtoList = item.getItems().stream()
                .map(DispensationItemResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}