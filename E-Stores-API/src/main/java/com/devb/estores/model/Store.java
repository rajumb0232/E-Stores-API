package com.devb.estores.model;

import com.devb.estores.enums.TopCategory;
import com.devb.estores.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String storeId;
    private String storeName;
    private TopCategory topCategory;
    private String logoLink;
    private String about;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
}
