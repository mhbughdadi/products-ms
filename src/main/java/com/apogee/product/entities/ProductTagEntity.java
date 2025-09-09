package com.apogee.product.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@DiscriminatorValue(value = "PRODUCT")
public class ProductTagEntity extends TagAssignmentEntity {

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false, referencedColumnName = "id")
    private ProductEntity product;

}
