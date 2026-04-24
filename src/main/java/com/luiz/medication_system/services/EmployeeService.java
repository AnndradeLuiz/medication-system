package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dto.EmployeeRequestDTO;
import com.luiz.medication_system.dto.EmployeeResponseDTO;
import com.luiz.medication_system.repository.EmployeeRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

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

    public Employee fromDto(EmployeeRequestDTO employeeRequestDTO) {
        String password = encryptPassword(employeeRequestDTO.password());

        return new Employee(
                null,
                employeeRequestDTO.name(),
                employeeRequestDTO.cpf(),
                employeeRequestDTO.registration(),
                password,
                employeeRequestDTO.position(),
                employeeRequestDTO.status()
        );
    }

    private void updateData(Employee newEmployee, Employee employee) {
        if (employee.getName() != null) {
            newEmployee.setName(employee.getName());
        }
        if (employee.getCpf() != null) {
            newEmployee.setCpf(employee.getCpf());
        }
        if (employee.getRegistration() != null) {
            newEmployee.setRegistration(employee.getRegistration());
        }
        if (employee.getPosition() != null) {
            newEmployee.setPosition(employee.getPosition());
        }
        if (employee.getStatus() != null) {
            newEmployee.setStatus(employee.getStatus());
        }
    }

}
