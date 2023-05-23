package com.timemanager.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timemanager.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
  private UserEntity user;

  @NotNull
  @Column(name = "user_id")
  private Long userId;

  @NotBlank
  private String title;
  @NotBlank

  @Column(columnDefinition = "text", length = 500)
  private String description;

  @NotNull
  private String approximatePerformanceTime;

  @NotNull
  private LocalDate performanceDate;
  @NotNull
  private LocalDate startDate;
  @NotNull
  private LocalDate endDate;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskState state;

  @NotNull
  @Min(1)
  @Max(5)
  private Integer priority;

}
