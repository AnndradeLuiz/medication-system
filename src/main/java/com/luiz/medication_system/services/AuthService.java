package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dto.LoginRequestDTO;
import com.luiz.medication_system.dto.LoginResponseDTO;
import com.luiz.medication_system.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        Employee emp = employeeRepository.findByCpfOrRegistration(dto.login())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Usuário não encontrado.");
                });
        if (!passwordEncoder.matches(dto.password(), emp.getPassword())) {
            throw new IllegalArgumentException("Senha inválida.");
        }
        return new LoginResponseDTO(emp.getId(), emp.getName());
    }

}
