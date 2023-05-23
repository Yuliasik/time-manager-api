package com.timemanager.user.settings;

import com.timemanager.user.settings.dto.UserSettingsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Controller to handle API-operations with {@link UserSettings} entity.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settings")
public class SettingsController {

  private final SettingsService settingsService;

  /**
   * GET /settings request handler to return all UserSettings.
   *
   * @return List<UserSettings> object and 200 Http status.
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<UserSettings> getAllSettingsByUserId() {
    return ResponseEntity.ok(settingsService.getAllSettingsByUserId());
  }

  /**
   * PUT /settings request handler to update UserSettings object.
   * Should contain id field as identifier which UserSettings object will be updated.
   *
   * @param userSettingsDto - UserSettings object with modified fields to be updated.
   * @return updated UserSettings object.
   */
  @PutMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  public UserSettings update(@RequestBody @NotNull UserSettingsDto userSettingsDto) {
    return settingsService.update(userSettingsDto);
  }

}

