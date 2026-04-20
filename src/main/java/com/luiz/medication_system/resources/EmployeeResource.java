package com.luiz.medication_system.resources;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dto.EmployeeDTO;
import com.luiz.medication_system.dto.EmployeeInsertDTO;
import com.luiz.medication_system.services.EmployeeService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        List<Employee> list = service.findAll();
        List<EmployeeDTO> dtoList = list.stream().map(EmployeeDTO::new).toList();
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeDTO> findById(@PathVariable String id) {
        Employee employee = service.findById(id);
        return ResponseEntity.ok().body(new EmployeeDTO(employee));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@RequestBody EmployeeInsertDTO employeeInsertDTO) {
        Employee employee = service.fromDto(employeeInsertDTO);
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
    public ResponseEntity<Void> update(@RequestBody EmployeeInsertDTO employeeInsertDTO, @PathVariable String id) {
        Employee employee = service.fromDto(employeeInsertDTO);
        employee.setId(id);
        service.update(employee);
        return ResponseEntity.noContent().build();
    }

}