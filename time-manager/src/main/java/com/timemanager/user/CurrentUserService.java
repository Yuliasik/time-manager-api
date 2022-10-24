package com.timemanager.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::entityToCurrentUser)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Failed to find user with username " + username)
                );
    }

    private CurrentUser entityToCurrentUser(UserEntity userEntity) {
        return new CurrentUser(userEntity.getUsername(), userEntity.getPassword());
    }
}
