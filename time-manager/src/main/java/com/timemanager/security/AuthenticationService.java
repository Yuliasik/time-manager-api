package com.timemanager.security;

import com.timemanager.exception.AuthorizationException;
import com.timemanager.exception.UserAlreadyExistException;
import com.timemanager.security.session.SessionHolderDTO;
import com.timemanager.security.session.SessionRegistry;
import com.timemanager.user.UserDTO;
import com.timemanager.user.UserEntity;
import com.timemanager.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Service layout to work with {@link UserRepository}.
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final SessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;

    public SessionHolderDTO login(@RequestBody UserDTO user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        if (isUserNotExist(username) || isIncorrectPassword(password, username)) {
            throw new AuthorizationException();
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        final String sessionId = sessionRegistry.registerSession(username);
        final UserEntity byUsername = userRepository.getByUsername(username);
        return new SessionHolderDTO(sessionId, byUsername.getId());
    }

    public SessionHolderDTO register(@RequestBody UserDTO user) {
        final String username = user.getUsername();

        if (Boolean.TRUE.equals(userRepository.existsByUsername(username))) {
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

    private boolean isUserNotExist(String username) {
        return Boolean.FALSE.equals(userRepository.existsByUsername(username));
    }

    private boolean isIncorrectPassword(String password, String username) {
        return !passwordEncoder.matches(password, userRepository.getPasswordByUsername(username));
    }

}
