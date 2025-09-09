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
@DiscriminatorValue(value = "CATEGORY")
public class CategoryTagEntity extends TagAssignmentEntity {

    @ManyToOne
    @JoinColumn(name = "categories_id", nullable = false)
    private CategoryEntity category;

}
