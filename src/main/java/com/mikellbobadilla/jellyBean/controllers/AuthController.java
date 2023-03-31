package com.mikellbobadilla.jellyBean.controllers;

import com.mikellbobadilla.jellyBean.dto.AuthRecord;
import com.mikellbobadilla.jellyBean.dto.JwtRecord;
import com.mikellbobadilla.jellyBean.dto.RegisterRecord;
import com.mikellbobadilla.jellyBean.jwt.JwtToken;
import com.mikellbobadilla.jellyBean.user.Role;
import com.mikellbobadilla.jellyBean.user.User;
import com.mikellbobadilla.jellyBean.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtToken jwtToken;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtRecord> authenticate(@RequestBody AuthRecord auth) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(auth.username(), auth.password());

        authenticationManager.authenticate(authentication);

        User user = userRepository.findByUsername(auth.username())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        String token = jwtToken.createToken(user);

        return ResponseEntity.ok(new JwtRecord(token));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtRecord> register(@RequestBody RegisterRecord register){
        User newUser = User.builder()
                .firstName(register.firstName())
                .lastName(register.lastName())
                .username(register.username())
                .email(register.email())
                .password(encoder.encode(register.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);
        String token = jwtToken.createToken(newUser);

        return ResponseEntity.ok(new JwtRecord(token));
    }
}
