package com.timemanager.user.settings;

import com.timemanager.exception.EntityNotFoundException;
import com.timemanager.security.currentuser.CurrentUser;
import com.timemanager.user.UserEntity;
import com.timemanager.user.UserRepository;
import com.timemanager.user.settings.dto.UserSettingsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service layout to work with {@link SettingsRepository}.
 */
@Service
@RequiredArgsConstructor
public class SettingsService {

  private final SettingsRepository settingsRepository;
  private final SettingsValidator settingsValidator;
  private final UserRepository userRepository;
  private final SettingsMapper settingsMapper;

  /**
   * Method to get all {@link UserSettings}.
   *
   * @return List<UserSettings> object and 200 Http status.
   */
  public UserSettings getAllSettingsByUserId() {
    return settingsRepository.findByUserId(
        getCurrentUser().getId()
    );
  }

  /**
   * Method to update task if exist or else throw errors.
   *
   * @param userSettingsDto {@link UserSettingsDto} to update.
   * @return updated {@link UserSettings}
   */
  public UserSettings update(UserSettingsDto userSettingsDto) {
    final UserSettings userSettings = settingsMapper.fromDto(userSettingsDto);
    userSettings.setUser(getCurrentUser());
    Long settingsId = userSettings.getId();
    if (settingsId == null) {
      throw new EntityNotFoundException("Settings", settingsId);
    }
    settingsValidator.handleValidate(userSettings);
    return settingsRepository.save(userSettings);
  }

  /**
   * Method to create setting with default values.
   *
   * @return updated {@link UserSettings}
   */
  public UserSettings createWithDefaultSettingValue(UserEntity userEntity) {
    final UserSettings userSettings = UserSettings.builder()
        .durationHour(8)
        .bufferPercentage(10)
        .user(userEntity)
        .build();
    return settingsRepository.save(userSettings);
  }

  private UserEntity getCurrentUser() {
    final CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.getByUsername(user.getUsername());
  }

}
