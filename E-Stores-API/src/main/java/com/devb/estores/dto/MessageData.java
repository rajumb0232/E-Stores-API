package com.devb.estores.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String to;
    private String subject;
    private Date sentDate;
    private String text;
}
