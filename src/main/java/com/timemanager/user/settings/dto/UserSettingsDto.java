package com.timemanager.user.settings.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSettingsDto {
    private Long id;
    private Integer durationHour;
    private Integer bufferPercentage;

}
