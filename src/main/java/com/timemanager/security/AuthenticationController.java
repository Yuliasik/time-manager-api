package com.timemanager.security;

import com.timemanager.security.session.SessionHolderDTO;
import com.timemanager.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<SessionHolderDTO> login(@RequestBody UserDTO user) {
        final SessionHolderDTO response = authenticationService.login(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<SessionHolderDTO> register(@RequestBody UserDTO user) {
        final SessionHolderDTO response = authenticationService.register(user);
        return ResponseEntity.ok(response);
    }


}
