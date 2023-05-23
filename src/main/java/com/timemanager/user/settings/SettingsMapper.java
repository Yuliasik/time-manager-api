package com.timemanager.user.settings;

import com.timemanager.user.settings.dto.UserSettingsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingsMapper {

  UserSettings fromDto(UserSettingsDto userSettingsDto);

}
