package com.devb.estores.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attempt {
    private LocalDateTime lastAttemptedAt;
}
