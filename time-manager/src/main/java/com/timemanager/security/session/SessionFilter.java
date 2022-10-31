package com.timemanager.security.session;

import com.timemanager.security.currentuser.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class SessionFilter extends OncePerRequestFilter {

    private final SessionRegistry sessionRegistry;
    private final CurrentUserService currentUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        final String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (sessionId == null || sessionId.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String username = sessionRegistry.getUsernameForSession(sessionId);
        if (username == null) {
            chain.doFilter(request, response);
            return;
        }

        final UserDetails currentUser = currentUserService.loadUserByUsername(username);

        final UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        currentUser, null, currentUser.getAuthorities()
                );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        chain.doFilter(request, response);
    }

}
