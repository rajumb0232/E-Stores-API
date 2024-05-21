package com.devb.estores.model;

import com.devb.estores.enums.SubCategory;
import com.devb.estores.enums.TopCategory;
import com.devb.estores.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String typeId;
    @Enumerated(EnumType.STRING)
    private TopCategory topCategory;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    private String typeName;
}
