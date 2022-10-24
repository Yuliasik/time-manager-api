package com.timemanager.security;

import com.timemanager.exception.UserAlreadyExistException;
import com.timemanager.security.session.SessionHolderDTO;
import com.timemanager.security.session.SessionRegistry;
import com.timemanager.user.UserDTO;
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
    public ResponseEntity<SessionHolderDTO> login(@RequestBody UserDTO user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        final String sessionId = sessionRegistry.registerSession(user.getUsername());
        final SessionHolderDTO response = new SessionHolderDTO(sessionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<SessionHolderDTO> register(@RequestBody UserDTO user) {
        final String username = user.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException(username);
        } else {
            UserEntity userEntity = userDtoToUserEntityWithEncrypt(user);
            userRepository.save(userEntity);
        }

        return login(user);
    }

    private UserEntity userDtoToUserEntityWithEncrypt(UserDTO userDTO) {
        return UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();
    }

}
