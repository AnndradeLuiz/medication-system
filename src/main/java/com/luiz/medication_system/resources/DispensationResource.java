package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Dispensation;
import com.luiz.medication_system.dto.DispensationItemResponseDTO;
import com.luiz.medication_system.dto.DispensationResponseDTO;
import com.luiz.medication_system.dto.DispensationRequestDTO;
import com.luiz.medication_system.services.DispensationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/dispensations")
public class DispensationResource {

    private final DispensationService service;

    public DispensationResource(DispensationService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DispensationResponseDTO>> findAll() {
        List<Dispensation> list = service.findAll();
        List<DispensationResponseDTO> dtoList = list.stream().map(DispensationResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DispensationResponseDTO> findById(@PathVariable String id) {
        Dispensation entity = service.findById(id);
        return ResponseEntity.ok().body(new DispensationResponseDTO(entity));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody DispensationRequestDTO entityDto) {
        Dispensation entity = service.fromDto(entityDto);
        entity = service.insert(entity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody DispensationRequestDTO entityDto, @PathVariable String id) {
        Dispensation entity = service.fromDto(entityDto);
        entity.setId(id);
        service.update(entity);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public ResponseEntity<List<DispensationItemResponseDTO>> findItems(@PathVariable String id) {
        Dispensation item = service.findById(id);
        List<DispensationItemResponseDTO> dtoList = item.getItems().stream()
                .map(DispensationItemResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(dtoList);
    }

}