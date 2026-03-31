package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@EqualsAndHashCode
public class CategoryClosureId {

    @Column(name = "descendant_id")
    private Long descendantId;

    @Column(name = "ancestor_id")
    private Long ancestorId;
}
