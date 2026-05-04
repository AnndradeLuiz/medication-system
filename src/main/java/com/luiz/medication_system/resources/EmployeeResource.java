package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dto.EmployeeResponseDTO;
import com.luiz.medication_system.dto.EmployeeRequestDTO;
import com.luiz.medication_system.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeResource {

    private final EmployeeService service;

    public EmployeeResource(EmployeeService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EmployeeResponseDTO>> findAll() {
        List<Employee> list = service.findAll();
        List<EmployeeResponseDTO> dtoList = list.stream().map(EmployeeResponseDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeResponseDTO> findById(@PathVariable String id) {
        Employee employee = service.findById(id);
        return ResponseEntity.ok().body(new EmployeeResponseDTO(employee));
    }

    @PreAuthorize("hasAnyRole('ADM_TI', 'ENF_GERENTE')")
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody EmployeeRequestDTO employeeDTO) {
        Employee employee = service.fromDto(employeeDTO);
        employee = service.insert(employee);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody EmployeeRequestDTO employeeDTO, @PathVariable String id) {
        Employee employee = service.fromDto(employeeDTO);
        employee.setId(id);
        service.update(employee);
        return ResponseEntity.noContent().build();
    }

}