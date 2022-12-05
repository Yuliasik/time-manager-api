package com.timemanager.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
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

    @NotNull
    private Long userId;

    @NotBlank
    private String title;
    @NotBlank

    @Column(columnDefinition = "text", length = 500)
    private String description;

    @NotNull
    private Duration approximatePerformanceTime;

    @NotNull
    private LocalDate performanceDate;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskState state;

}
