package com.apogee.product.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@NamedQueries({
        @NamedQuery(
                name = "CategoryClosureEntity.findAllDescendants",
                query = "SELECT c.id.descendantId FROM CategoryClosureEntity c WHERE c.id.ancestorId = :ancestorId AND c.depth > 0"
        ),
        @NamedQuery(
                name = "CategoryClosureEntity.findAllAncestors",
                query = "SELECT c.id.ancestorId FROM CategoryClosureEntity c WHERE c.id.descendantId = :descendantId AND c.depth > 0"
        ),
        @NamedQuery(name = "CategoryClosureEntity.findMainCategories",
                query = "SELECT c.id.ancestorId FROM CategoryClosureEntity c WHERE c.depth = 0 "
        ),@NamedQuery(name = "CategoryClosureEntity.findParentDepth",
                query = "SELECT c.depth FROM CategoryClosureEntity c WHERE c.id.descendantId = : descendantId "
        ),
}
)
@Setter
@Getter
@Entity(name = "CategoryClosureEntity")
@Table(name = "category_closure")
public class CategoryClosureEntity {

    @EmbeddedId
    private CategoryClosureId id;

    @Column(name = "depth", nullable = false)
    private Integer depth;
}
