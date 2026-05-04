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
    private final TokenService tokenService;

    public AuthService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO authenticate(LoginRequestDTO dto) {
        Employee emp = employeeRepository.findByCpfOrRegistration(dto.login())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        if (!passwordEncoder.matches(dto.password(), emp.getPassword())) {
            throw new IllegalArgumentException("Senha inválida.");
        }
        String token = tokenService.generateToken(emp);
        return new LoginResponseDTO(emp, token);
    }

}
