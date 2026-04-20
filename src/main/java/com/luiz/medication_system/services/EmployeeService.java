package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dto.EmployeeInsertDTO;
import com.luiz.medication_system.repository.EmployeeRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Employee findById(String id) {
        Optional<Employee> employee = repository.findById(id);
        return employee.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public Employee insert(Employee employee) {
        return repository.insert(employee);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(Employee employee) {
        Employee newEmployee = findById(employee.getId());
        updateData(newEmployee, employee);
        repository.save(newEmployee);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Employee fromDto(EmployeeInsertDTO employeeInsertDTO) {
        String password = encryptPassword(employeeInsertDTO.password());
        return new Employee(
                employeeInsertDTO.id(), employeeInsertDTO.name(),
                employeeInsertDTO.registration(), password,
                employeeInsertDTO.position(), employeeInsertDTO.status()
        );
    }

    private void updateData(Employee newEmployee, Employee employee) {
        newEmployee.setName(employee.getName());
        newEmployee.setRegistration(employee.getRegistration());
        newEmployee.setPosition(employee.getPosition());
        newEmployee.setStatus(employee.getStatus());
    }

}
