package com.timemanager.security.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionHolderDto {
    private String sessionId;
    private Long userId;
}
