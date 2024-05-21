package com.devb.estores.model;

import com.devb.estores.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String contactId;
    private String contactName;
    private long contactNumber;
    private boolean isPrimary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Address address;
}
