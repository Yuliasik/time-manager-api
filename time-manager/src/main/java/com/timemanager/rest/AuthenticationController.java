package com.timemanager.rest;

import com.timemanager.dto.SessionHolder;
import com.timemanager.dto.UserDTO;
import com.timemanager.session.SessionRegistry;
import com.timemanager.user.UserEntity;
import com.timemanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<SessionHolder> login(@RequestBody UserDTO user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        final String sessionId = sessionRegistry.registerSession(user.getUsername());
        final SessionHolder response = new SessionHolder(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<SessionHolder> register(@RequestBody UserDTO user) {
        UserEntity userEntity = userDtoToUserEntityWithEncrypt(user);
//        TODO: Validator for user: is user by username not exist
        userRepository.save(userEntity);
        return login(user);
    }

    private UserEntity userDtoToUserEntityWithEncrypt(UserDTO userDTO) {
        return UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
    }

}
