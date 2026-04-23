package me.calebeoliveira.springaopexample.service;

import java.util.List;

public interface JwtTokenService {
    String generateToken(String username, Long userId, List<String> roles);
}
