package com.timemanager.security;

import com.timemanager.security.session.SessionHolderDto;
import com.timemanager.user.dto.UserDto;
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
    public ResponseEntity<SessionHolderDto> login(@RequestBody UserDto user) {
        final SessionHolderDto response = authenticationService.login(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<SessionHolderDto> register(@RequestBody UserDto user) {
        final SessionHolderDto response = authenticationService.register(user);
        return ResponseEntity.ok(response);
    }


}
