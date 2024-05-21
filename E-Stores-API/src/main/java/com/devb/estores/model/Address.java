package com.devb.estores.model;

import com.devb.estores.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String addressId;
    private String addressLine1;
    private String addressLine2;
    private String areaVillage;
    private String cityDistrict;
    private String state;
    private String country;
    private int pincode;

    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER)
    private List<Contact> contacts;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
