package me.calebeoliveira.springaopexample.controller;

import lombok.RequiredArgsConstructor;
import me.calebeoliveira.springaopexample.model.LoginRequest;
import me.calebeoliveira.springaopexample.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String token = jwtTokenService.generateToken(
                loginRequest.username(),
                loginRequest.userId(),
                List.of("USER")
        );

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody LoginRequest loginRequest) {
        String token = jwtTokenService.generateToken(
                loginRequest.username(),
                loginRequest.userId(),
                List.of("USER", "ADMIN")
        );

        return ResponseEntity.ok(Map.of("token", token));
    }
}
